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

* basic mathematical operations: +, -, *, / following their precedences
* brackets, like in ```2 * (3 + x) - (5 * (3+y))```
* user defined functions, like in ```2 * sin(1+x)``` or ```2 * max(x, y)```
  * variadic functions are supported too
  * currently, functions cannot be defined using the expression syntax, but only by binding java methods to the evaluator
* There are two Evaluator Implementations
  * SimpleEvaluator, which evaluates into a instruction list, which gets ran by an own rudimentary "VM"
  * ASMEvaluator, creates javabytecode implementing the Expression Interface, using ASM.
  
## Some TODOs

A not complete list of TODOs
 * Implementing the JDK's ScriptEngine interface
