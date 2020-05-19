package com.github.wololock

import groovy.transform.CompileStatic

@CompileStatic
final class Brainfuck {

    private final String program

    Brainfuck(String program) {
        this.program = program
    }

    String process(String input = "") {
        return ""
    }

    static void main(String[] args) {
        println new Brainfuck(args[0]).process(args.length > 1 ? args[1] : "")
    }
}
