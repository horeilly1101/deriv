# deriv 

> An open source derivative calculator REST API (and Java library). Check out the [frontend](https://www.github.com/horeilly1101/deriv-frontend).

[![Build Status](https://travis-ci.com/horeilly1101/deriv.svg?branch=master)](https://travis-ci.com/horeilly1101/deriv)
[![codecov](https://codecov.io/gh/horeilly1101/deriv/branch/master/graph/badge.svg)](https://codecov.io/gh/horeilly1101/deriv)
[![Known Vulnerabilities](https://snyk.io/test/github/horeilly1101/deriv/badge.svg)](https://snyk.io/test/github/horeilly1101/deriv)

## In Development

I began this project wanting to make a symbolic derivative calculator that could differentiate arbitrary functions of 
the form f : R^n -> R. And I did that. But after taking some time to reflect on the project, I realized that
I actually ended up setting the groundwork for something far more interesting. Why limit myself to functions 
just of the form described above? Why not try to differentiate vector-valued functions, or, better yet, 
tensor-valued functions?

So that's what I'm working on right now. Wish me luck.

## Server

The server is built using [Spark](http://sparkjava.com/), and you can run the server by running 
[Server.java](src/main/java/com/deriv/server/Server.java) in the server package. The server will then be available
at `localhost:4567`. There are **three** different requests you can make. The first is
`localhost:4567/differentiate/:expression/:var`, where `:expression` is the expression to be differentiated 
**(e.g. x^2 &ast; ln(x))** and `:var` is the variable that `:expression` should be differentiated with respect to 
**(e.g. x)**. This request returns a JSON object of the form

    { 
        "data" :   
            {  
                "expression" : expression,
                "result" : differentiated expression,
                "var" : var
            }
    }
    
The second is `localhost:4567/evaluate/:expression/:var/:val`, where `:expression` is the expression to be 
evaluated **(e.g. x^2 &ast; ln(x))**, `:var` is the variable that should be evaluated **(e.g. x)**, and `:val` is the number 
that `:expression` should be evaluated with **(e.g. 5)**. This request returns a JSON object of the form
              
    { 
        "data" :   
            {  
                "expression" : expression,
                "result" : evaluated expression,
                "var" : var,
                "val" : val
            }
    }
    
And the third is `localhost:4567/simplify/:expression`, where `:expression` is the expression to be simplified
**(e.g. x^2 + 4x &ast; x + 4/x &ast; x^3)**. This request returns a JSON object of the form

    { 
        "data" :   
            {  
                "input" : input expression,
                "result" : simplified expression
            }
    }
    
If the given arguments in a URL are invalid, an error JSON object will be returned in all cases.
    
**Note:** you should be careful to use the proper ASCII encoding references when writing your expressions in the URL.
For example, you can't use "/" to represent division when querying the server, as that is a reserved character.
Instead, use %2F, its ASCII encoding reference. You can find a comprehensive list of URL encoding references
[here](https://www.w3schools.com/tags/ref_urlencode.asp), but I've put the important ones (for this project) down
below.

In particular, you should not allow forward slashes, brackets, carrots, or blank spaces in your URLs.

|    Symbol   | URL Code |
|:-----------:|----------|
|      /      |    %2F   |
|      ^      |    %5E   |
|      [      |    %5B   |
|      ]      |    %5D   |
| blank space |    %20   |

## Library

There are two main ways to use this project as a Java library.

### Calculator

Check out `com.deriv.expression.Calculator`. If you instantiate a `Calculator` object, you can evaluate,
differentiate, and simplify expressions by just providing a string representation of an expression. If you decide
to do this, you will find it helpful to be as specific as possible with your string expressions (i.e. place 
parentheses liberally), as they can quickly become ambiguous.

```
// Instantiate a Calculator object
Calculator calc = new Calculator();

// Example 1: prints "(2 * x)"
System.out.println(calc.differentiate("x^2", "x").get());

// Example 2: prints "8"
System.out.println(calc.evaluate("3x + 2", "x", "2").get());

// Example 3: prints "(sin(x) / x + (cos(x) * ln(x)))"
System.out.println(calc.differentiate("sin(x)ln(x)", "x").get());

// Example 4: prints "sin(x ^ 2)"
System.out.println(calc.evaluate("sin(x)", "x", "x^2").get());
```

And since most methods on `Calculator` return an `Optional<Expression>`, you have access to the `Expression` API
to do with what you wish.

### Static Constructors

You can also create expressions with the available static constructors. See the docs and unit tests for 
available constructors and more instructions on how to use them.

```
// Example 1: 3x
mult(constant(3), x());

// Example 2: x^2 + x + 1
add(poly(x(), 2), x(), multID());

// Example 3: sin(y^x)
sin(power(var("y"), x()));

// Example 4: ln(5) + e^z
add(ln(constant(5)), power(e(), var("z")));
```

## Design

### Polymorphic Expressions

Definition: **Expression** is the data structure that allows us to put functions together and take their 
derivatives. Every function is an implementation of an Expression—this is the key design detail that glues 
the project together. It is implemented by

- *Mult*: a mult is a list of expressions, multiplied together
- *Add*: an add is a list of expressions, added together
- *Log*: a log is a base and a result (i.e. log(base, result))
- *Power*: a power is a base and an exponent (i.e. base ^ exponent)
- *Trig*: a trig is a trig function name and an expression
- *Constant*: a constant is an int (unfortunately, arbitrary constants are technically variables)
- *Variable*: a string name (e.g. "x", "y", etc.)
- *Tensor*: a tensor is a list of expressions

The above classes allow deriv to differentiate just about any function you can think of. (The only functions not
available are integrals, inverse functions, and more obscure functions, but these may all be added later on.) It's
interesting to note that the hardest part of this project has been simplifying the expressions before they're
instantiated. The design, derivatives, evaluations—all of that was easy compared to the simplification stage.

### Differentiation Algorithms

For the most part, I used the standard recursive algorithms that you learn in an introductory calculus class (e.g.
product rule, linearity), but there were a few cases where I had to derive nonstandard algorithms to compute
derivatives, to ensure as much generality as possible. That being noted, I doubt there is anything revolutionary 
in my approach.

Beyond the above, there are a few cases where I decided to optimize with parallelism. For instance, I opted for 
parallel streams whenever linearity was necessary (e.g. when differentiating the sum of expressions). However,
the most interesting use of parallelism can be found in `com.deriv.expression.Mult`, where I use Fork/Join
to compute the derivatives of products in parallel. There were also a few other places where I tested parallel 
algorithms, but found that they did not really improve the runtime. For simplicity, I used sequential algorithms 
in such cases.

### Caching

Checkout the `steps` branch if you would like to use an (outdated) version of this project that provides derivative
steps and caching. Currently, the `master` branch does not support this, as I have had to reorganize the project
to account for Tensors.

Note that, when I say caching, I'm referring to the storing of derivatives that have already been computed, which is 
more complicated than it first appears. Most of the differentiation algorithms used in this project recursively 
compute the derivatives of composed expressions using a divide and conquer approach. So it's not enough to simply
store the result whenever we call `differentiate()` in the calculator or the server—we need to store the results
throughout the entire process, and then combine them "on the way back up" the recursion. At some point I will update
this to work with Tensors, but it's not a priority.

### Parsing

The scanner is built using [jflex](http://jflex.de/manual.html), and the parser is built using 
[CUP](http://jflex.de/manual.html). You can find the grammar rules for expressions in 
[FlexScanner.jflex](src/main/jflex/com/deriv/parser/FlexScanner.jflex) and 
[CupParser.cup](src/main/cup/com/deriv/parser/CupParser.cup). Given the style of these two files, the 
grammar should be pretty easy to understand, even if you aren't familiar with jflex or CUP.

The order of operations should work as intended, as should smaller details like adding and subtracting
negative numbers and using implicit multiplication. Also, multiplication, division, addition, and 
subtraction are left associative, while exponentiation is right associative. This property of the 
grammar is meant to increase clarity.

## Notes

- Dependencies are handled with Maven.
- All code was written in IntelliJ IDEA.
- *Effective Java* by Joshua Bloch inspired many of the design decisions, as did Rice University's COMP 215 and 310.
- Uses [JSON-Java](https://github.com/stleary/JSON-java) to create JSON objects.
- I built a [frontend to interact with the API](https://www.github.com/horeilly1101/deriv-frontend).