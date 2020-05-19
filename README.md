# Brainfuck Interpreter in Groovy

https://en.wikipedia.org/wiki/Brainfuck

Run "Hello, World!" program:

```
$ ./gradlew --no-daemon run --args="++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++."
```

Run all unit tests:

```
$ ./gradlew --no-daemon test
```

Clone empty project to implement your own interpreter:

```
$ git clone --single-branch --branch clean https://github.com/wololock/groovy-brainfuck
```