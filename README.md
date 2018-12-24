# deriv

An open source derivative calculator REST API. (In progress!)

## Expression
Definition: An Expression is the all-encompassing data structure that allows us to put functions
together and take their derivatives. Every function is an implementation of an Expression -- this
is the key design detail that glues the project together. It is implemented by

- *Add*: an add is a list of expressions, added together
- *Mult*: a mult is a list of expressions, multiplied together
- *Log*: a log is a base and a result (i.e. log(base, result))
- *Power*: a power is a base and an exponent (i.e. base ^ exponent)
- *Trig*: a trig is a trig function name and an expression
- *Constant*: a constant is an Double (as painful as it may be to see, arbitrary constants are technically variables)
- *Variable*: a string name (e.g. "x", "y", etc.)

At this point, I've finished designing and implementing how expressions can be created and put together,
written many of the necessary unit tests, and described the language I expect the parser to follow. All
I have left to do is implement the recursive descent parser and put together a bare bones server. Coming soon!