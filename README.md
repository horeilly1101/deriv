# deriv 
[![Build Status](https://travis-ci.com/horeilly1101/deriv.svg?branch=master)](https://travis-ci.com/horeilly1101/deriv)
[![codecov](https://codecov.io/gh/horeilly1101/deriv/branch/master/graph/badge.svg)](https://codecov.io/gh/horeilly1101/deriv)

An open source derivative calculator REST API (and Java library). Now with steps!

## Server

The server is built using [Spark](http://sparkjava.com/), and you can run the server by running 
[Server.java](src/main/java/com/deriv/server/Server.java) in the server module. The server will then be available
at localhost:4567. There are two different requests you can make. The first is
`localhost:4567/differentiate/:expression/:var`, where "expression" is the expression to be differentiated 
(e.g. x^2 * ln(x)) and "var" is the variable that "expression" should be differentiated with respect to (e.g. x). 
This request returns a JSON object of the form

    { 
        "data" :   
            {  
                "expression" : expression,
                "result" : differentiated expression,
                "var" : var,
                "steps" : [ 
                            steps 
                          ]
            }
    }
    
And the second is `localhost:4567/evaluate/:expression/:var/:val`, where "expression" is the expression to be 
evaluated (e.g. x^2 * ln(x)), "var" is the variable that should be evaluated (e.g. x), and "val" is the number 
that "expression" should be evaluated with (e.g. 5). This request returns a JSON object of the form
              
    { 
        "data" :   
            {  
                "expression" : expression,
                "result" : evaluated expression,
                "var" : var,
                "val" : val, 
            }
    }
    
If the given arguments in a URL are invalid, an error JSON object will be returned.
    
NOTE: You should be careful to use the proper ASCII encoding references when writing your expressions in the URL.
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

## Polymorphic Design

Definition: **Expression** is the data structure that allows us to put functions together and take their 
derivatives. Every function is an implementation of an Expression -- this is the key design detail that glues 
the project together. It is implemented by

- *Mult*: a mult is a list of expressions, multiplied together
- *Add*: an add is a list of expressions, added together
- *Log*: a log is a base and a result (i.e. log(base, result))
- *Power*: a power is a base and an exponent (i.e. base ^ exponent)
- *Trig*: a trig is a trig function name and an expression
- *Constant*: a constant is an Integer (unfortunately, arbitrary constants are technically variables)
- *Variable*: a string name (e.g. "x", "y", etc.)

The above classes allow deriv to differentiate just about any function you can think of. (The only functions not
available are integrals, inverse functions, and more obscure functions, but these may all be added later on.) It's
interesting to note that the hardest part of this project has been simplifying the expressions before they're
instantiated. The design, derivatives, evaluations -- all of that was easy compared to the simplification stage.

For examples of how to use these classes, see the provided unit tests.

## Parser

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

- The Calculator class is built with memoization EVERYWHERE, so recomputing anything in this class can be
done in constant time. Also, since the caches are implemented as ConcurrentHashMaps, you can run multiple 
operations on the same Calculator object in parallel, and the results will be stored as usual.
- If you want to use this project as a library, you should focus your attention on the Calculator class.
- The Expression functionality was built using the Composite Design Pattern.
- Dependencies are handled with Maven.
- All code was written in IntelliJ IDEA.
- +96% of the lines of code in expression are covered by unit tests.
- Overall code coverage would be higher, but a significant portion of the "uncovered" code was generated by
jflex. Since this is difficult to test, and jflex is already quite well tested, I have decided this is not an issue.
- Uses [JSON-Java](https://github.com/stleary/JSON-java) to create JSON objects.
- I built a really messy frontend to interact with the API. Feel free to email me if you want see it.