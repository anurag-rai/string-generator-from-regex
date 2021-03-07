# string-generator-from-regex

## Regular Expression

### Grammar

The formal language grammar is as seen on [Brics](https://www.brics.dk/automaton/doc/index.html). The implementation of this library does not scale to the full grammar.

### Operator Precedence

Althought the grammar indirectly specifies it, [POSIX doc](https://www.boost.org/doc/libs/1_56_0/libs/regex/doc/html/boost_regex/syntax/basic_extended.html#boost_regex.syntax.basic_extended.operator_precedence) makes it very clear.

## Approaches
### Popular Algorithmic Approaches

1. Shunting Yard as described [here](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
2. Coverting to a [Reverse Polish Notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation) and then interpreting it

### NFA To DFA Approach

1. Thompsons Algorithm [Wiki](https://en.wikipedia.org/wiki/Thompson%27s_construction), [Denis Khyashif blog](https://deniskyashif.com/2019/02/17/implementing-a-regular-expression-engine/), [University of Texas Notes](https://lambda.uta.edu/cse5317/spring01/notes/node9.html)
2. Kleens Algorithms [Brief explanation](https://www.geeksforgeeks.org/kleenes-theorem-in-toc-part-1/)
3. Recursive Descent - Not explored

### Existing Libraries
1. [Xeger](https://code.google.com/archive/p/xeger/) uses Brics Automaton

## Implementation

This library takes a hybrid approach of parsing the regular expression, creating a generator engine and then using that engine as a base structure to generate random strings.

### Caveats
1. Only printable ASCII characters allowed as specified [here](http://facweb.cs.depaul.edu/sjost/it212/documents/ascii-pr.htm)
2. Backreference beyond 9 E.g. \10, is not implemented. This makes the backreference logic simpler to implement without ambiguity as described [here](https://www.rexegg.com/regex-capture.html#beyond9)

### Design

#### Structures

##### Token and Entity

We define that a Regular Expression (RegEx) is composed of `Token`s. An operator like `|` is a Token. If a `Token` is responsible for one or more character in the result then we call it an `Entity`. An `Entity` like "[a-z]" is also a Token.

##### StringGenerator

`Entity`'s can have a behaviour of generating strings. E.g. An Entity "abc" generates the String "abc" every-time. An entity like "a|b" generates either "a" or "b".

##### Repeater

A repeater in a Regex signifies repetition. E.g. "{2,3}" which signifies that the previous `Entity` needs to be repeated a minimum of 2 and a maximum of 3 times. Another example is "+", which signifies that minimum 1 and maximum infinite repetitions are needed.

##### DataEntity

To build a generator from RegExp, we parse the String and create `Entity`ies as atomic as possible. E.g. "abc", will give 3 `Entity`ies - "a", "b" and "c". We will call these `DataEntity`ies. Generating a result from a `DataEntity` is simple. We store all possible characters that this `DataEntity` can produce. E.g. a `DataEntity` "a" will only produce "a", but a `DataEntity` "[a-z]" can produce 26 different results. Since storing the values as `Character` might be restrictive, we store the possible values as `Integer`.

##### AndEntity

An expression like "abc" is called an `AndEntity`. This structure has a list of `DataEntity`ies. Generating a value from an `AndEntity` generates all values from its `DataEntity` serially in order.

##### OrEntity

An expression like "a|b|c" is called a `OrEntity`. This structure has a list of `DataEntity`ies. Generating a value from a `OrEntity` randomly picks one value in **equal probability** from the `DataEntity`

##### BackRefEntity

An expression like "\1" is called a `BackRefEntity`. This structure will hold a reference the the `Entity` that is supposed to be referenced. When generating a result, it will always generate the same result as the referenced `Entity`.

#### Builder

Now that the structures are decided, it is sufficient enough for a RegEx to be expressend as a combination of these `Token`s. The `RegExpBuilder` does exactly that with the help of `RepeaterBuilder`.

## Library

The project is Java-based with no external dependencies. Maven is used as a build tool.

### Test

```
mvn test
```

### Build/Package

```
mvn package
```

### Execute JAR file

```
java -jar stringGenerator-0.0.1-SNAPSHOT.jar
```

### Application

The application works on an infinite loop. On each loop user can specify
1. Regex
2. Number of strings that need to be generated from the regex

Typing EXIT on regex will quit the application
