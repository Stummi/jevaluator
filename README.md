# jevaluator

jevaluator is a java library for parsing mathematical expressions.

## Quick usage

code says more than words, so heres a very basic usage example for an static expression:

```java
Evaluator evaluator = new SimpleEvaluator();
Map<String, Double> variables = Collections.singletonMap("x", 42D);
Double result = evaluator.evaluate("23 + x", variables);
System.out.println(result); // 65.0
```

the Evaluator will compile an xpression String into an expression instance, the method called above is just a
convience call for this little bit more verbose way:

```java
Evaluator evaluator = new SimpleEvaluator();
Expression expr = evaluator.parseExpression("23 + x");
Map<String, Double> variables = Collections.singletonMap("x", 42D);
Double result = expr.run(variables);
System.out.println(result); // 65.0
```

## What is supported

* basic mathematical operations: +, -, \*, / following their precedences
* brackets, like in ```2 * (3 + x) - (5 * (3+y))```
* user defined functions, like in ```2 * sin(1+x)``` or ```2 * max(x, y)```
  * variadic functions are supported too
  * currently, functions cannot be defined using the expression syntax, but only by binding java methods to the evaluator
* There are two Evaluator Implementations, SimpleEvaluator and ASMEvaluator (see below for details)
* Currently, two specialized Interfaces for expressions with no or a single variable are provided (see below)
* Accessing the name of Variables required by a given Expression

## Some implementation details

* An expression will be parsed into a AST. This AST will be transformed into a list of instructions for a simple stack machine.
* After that, this instruction list can either run in a emulated stack machine (SimpleEvaluator) or compiled into java bytecode (ASMEvaluator)
* Every value within a expression is a native double, this has some implications:
  * calculations are subject of double rounding precision
  * currently, running a expression with a map which has a variable is not defined will result in a ugly NPE. This is subject to be changed later

## Extended Topics

### Choosing SimpleEvaluator vs ASMEvaluator

Currently you can choose between two Evaluator Implementations

#### SimpleEvaluator

The SimpleEvaluator compiles an Expression into a List of Instructions, represented as java classes

As a simple demonstration, lets take the expression ```2*(x+5)+3``` for an example.

The evaluator will create an InstructionList instance from this, simillar to this one

```java
InstructionList list = new InstructionList( //   command
    new PushConstantInstruction(2),     // push 2 to the stack
    new PushVariableInstruction("x"),   // load variable x and push it to the stack
    new PushConstantInstruction(5),     // push 5 to the stack
    new AddInstruction(),               // replace the last two values on the stack with their sum
    new MulInstruction(),               // replace the last two values on the stack with their product
    new PushConstantInstruction(3),     // push 3 to the stack
    new AddInstruction()                // replace the last two values on the stack with their sum
);
```

This InstructionList could be turned into a Expression instance (using ```InstructionListExpression```), but for now lets see what actually happens "under the hood" if we would run this InstructionListExpression:

```java
Map<String, Double> variables = Collections.singletonMap("x", 3D);
EvaluationContext context = new EvaluationContext(variables);
list.run(context);
Double result = context.getStack().pop();
System.out.println(result);
```

Every Instruction has its own run(EvaluationContext) method, InstructionList.run(EvaluationContext) just calls run for every Instruction it contains.

#### ASMEvaluator

The ASMEvaluator will transform the Expression in a InstructionList as above, but instead of wrapping this List into a InstructionListExpression, the InstructionList is used to generate JVM ByteCode, which will be loaded through an own ClassLoader. Lets take the InstructionList from above for an Example how to use it Internally:

```java
ASMClassLoader asmLoader = new ASMClassLoader();
Class<? extends Expression> exprClass = asmLoader.createExpressionClass(list);
Expression e = exprClass.newInstance();
Map<String, Double> variables = Collections.singletonMap("x", 3D);
System.out.println(e.run(variables));
```

The bytecode created by the ASMClassLoader equates about this java code:

```java
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.stummi.evaluator.expression.SingleVarExpression;

public class JITExpression$1 implements SingleVarExpression {
    public double run(double x) {
        return 2D * (x + 5D) + 3D;
    }

    public Double run(Map<String, Double> map) {
        return run(map.get("x"));
    }

    public List getRequiredVariables() {
        return Arrays.asList(new String[] { "x" });
    }
}

```

Note that the classname "JITExpression$1" is assigned by the ASMClassLoader automatically. A class with this name cannot be compiled normally, but only when generating bytecode directly. Also the ASMClassLoader decided to implement SingleVarExpression instead of just Expression based on the fact theres only one variable in this expression. There will always be one method taking all variables needed by the expression as native doubles, and implementing the actual expression (fullfying SingleVarExpression for one variable or StaticExpression for no variables) and one method taking a ```Map<String, Double>``` and delegating to the other one (fullfying Expression)

#### Conclusion

From a stability point of view, both evaluators are currently considered feature complete by me (Everything listed in "What is supported") and have both are about equally stable. However while developing it may be easier to debug the SimpleEvaluator than the ASMEvaluator.

From a performance point of view, the ASMEvaluator Expressions is clearly faster for a invocation, but takes a bit longer to compile, so its a good choice if you want to compile an expression once and run it often. The SimpleEvaluator Expression runs slower but has a better compile time, so its a good choice for expressions only runned once or few times.

### Specialized Interfaces

* At this time JEvaluator provides specialized Interfacs for
  * Expressions with no variables (like ```2*(3+5)```)
  * Expressions with exactly one variable (like ```2*(3+x)```)
* This Interfaces provides method for invoking the expression by giving no or this one Argument.
* The Expression instance given by the evaluator is guaranteed to be castable to the specialized Interface if the expression meets the requirement
* The SingleVarExpression interface will give you a small performance benefit, when using with the ASM-Evaluator

```java
ASMEvaluator evaluator = new ASMEvaluator();
Expression expr;

expr = evaluator.parseExpression("2*(x+5)");
SingleVarExpression svarExpression = (SingleVarExpression)expr;
System.out.println(svarExpression.run(3));

expr = evaluator.parseExpression("2*(8+5)");
StaticExpression staticExpression = (StaticExpression)expr;
System.out.println(staticExpression.run());
```

### Metadata from the Expression

The Expression interface allows you to query the required Variables by this Expression. This can be usefull when working with user provided expressions.

```java
Expression expr = new SimpleEvaluator().parseExpression("2*x + y + x");
System.out.println(expr.getRequiredVariables()); // prints [x, y]
```

The required Variables will always have the order of their first appearance in the expression

## Some TODOs

A not complete list of TODOs
 * Implementing the JDK's ScriptEngine interface
 * Extending the specialized interface functionality by allowing the user to provide an own interface to be implemented
 * Convience helper to make adding functions to the evaluator easier
