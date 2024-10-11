The objective of our project was to create a compiler for a programming language called Deca, which has a syntax similar to Java. We used various tools such as Java, Maven, ANTLR, JUnit, Bash in the development process. The Deca compiler project included the following stages:

- Generate abstract syntax trees from Deca source code using ANTLR.
- Implement semantic analysis to ensure the validity of Deca source files.
- Produce executable code in assembly language for the target machine.

Currently, this project targets an **abstract machine**, which can be emulated using a program called `ima` to execute compiled source files.

## How to Build

To compile the project, use the following command:

```bash
mvn compile
```

## How to Compile Source Files

To compile a source file, use:

```bash
./src/main/bin/decac path/to/fileName.deca
```

This command will generate a corresponding assembly file (.ass) at the specified path:
```bash
path/to/fileName.ass
```

## How to Execute Compiled Files

To execute the compiled .ass file using the ima emulator:

```bash
./bin/ima path/to/fileName.ass
```
