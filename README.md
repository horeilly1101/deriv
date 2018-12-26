# deriv

An open source derivative calculator REST API. (In progress!)

## Expression
Definition: An Expression is the all-encompassing data structure that allows us to put functions
together and take their derivatives. Every function is an implementation of an Expression -- this
is the key design detail that glues the project together. It is implemented by

- *Mult*: a mult is a list of expressions, multiplied together
- *Div*: a div is a numerator and a denominator
- *Add*: an add is a list of expressions, added together
- *Log*: a log is a base and a result (i.e. log(base, result))
- *Power*: a power is a base and an exponent (i.e. base ^ exponent)
- *Trig*: a trig is a trig function name and an expression
- *Constant*: a constant is an Double (as painful as it may be to see, arbitrary constants are technically variables)
- *Variable*: a string name (e.g. "x", "y", etc.)

The above classes allow deriv to differentiate just about any function you can think of. (The only functions not
available are integrals, inverse functions, and more obscure functions, but these may all be added later on.) It's
interesting to note that the hardest part of this project has been simplifying the expressions before they're
instantiated. The design, derivatives, evaluations -- all of that was easy compared to the simplifications.

For examples of how to use these classes, see the provided unit tests.

## Parser
Coming soon!

## Server
Coming soon!