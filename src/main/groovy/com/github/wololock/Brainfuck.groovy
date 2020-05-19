package com.github.wololock

import groovy.transform.CompileStatic
import groovy.transform.TailRecursive

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@CompileStatic
final class Brainfuck {

    private final String program

    Brainfuck(String program) {
        this.program = program
    }

    String process(String input = "") {
        return step(0,0, Memory.empty(), Input.from(input), Output.empty())
    }

    @TailRecursive
    private String step(int ip, int dp, Memory memory, Input input, Output output) {
        if (ip >= program.length()) {
            return output.text
        }

        switch (program.charAt(ip)) {
            case '>':
                return step(ip + 1, dp + 1, memory, input, output)
            case '<':
                return step(ip + 1, dp - 1, memory, input, output)
            case '+':
                return step(ip + 1, dp, memory.inc(dp), input, output)
            case '-':
                return step(ip + 1, dp, memory.dec(dp), input, output)
            case '.':
                return step(ip + 1, dp, memory, input, output.append(memory.read(dp)))
            case ',':
                return step(ip + 1, dp, memory.write(dp, input.head()), input.tail(), output)
            case '[':
                int jump = memory.read(dp) == 0 ? jumpForward(ip) : ip + 1
                return step(jump, dp, memory, input, output)
            case ']':
                int jump = memory.read(dp) > 0 ? jumpBack(ip) : ip + 1
                return step(jump, dp, memory, input, output)
            default:
                return step(ip + 1, dp, memory, input, output)
        }
    }

    int jumpForward(int ip) {
        int seek = 1
        while (seek > 0) {
            switch (program.charAt(++ip)) {
                case '[': seek++; break
                case ']': seek--; break
            }
        }
        return ip + 1
    }

    int jumpBack(int ip) {
        int seek = 1
        while (seek > 0) {
            switch (program.charAt(--ip)) {
                case ']': seek++; break
                case '[': seek--; break
            }
        }
        return ip + 1
    }

    @CompileStatic
    static final class Memory {

        private final ConcurrentMap<Integer,Byte> storage

        private Memory(Map map) {
            this.storage = new ConcurrentHashMap<>(map)
        }

        static Memory empty() {
            return new Memory([:])
        }

        char read(int idx) {
            return storage.getOrDefault(idx, Byte.MIN_VALUE) + 128 as char
        }

        Memory inc(int idx) {
            final copy = new ConcurrentHashMap<>(storage)
            copy.compute(idx) { _, val -> (val ?: Byte.MIN_VALUE) + 1 as byte }
            return new Memory(copy)
        }

        Memory dec(int idx) {
            final copy = new ConcurrentHashMap<>(storage)
            copy.compute(idx) { _, val -> (val ?: Byte.MIN_VALUE) - 1 as byte }
            return new Memory(copy)
        }

        Memory write(int idx, char val) {
            final copy = new ConcurrentHashMap<>(storage)
            copy.put(idx, val - 128 as byte)
            return new Memory(copy)
        }
    }

    @CompileStatic
    static final class Input {
        private final String str

        Input(String str) {
            this.str = str
        }

        static Input from(String s) {
            return new Input(s)
        }

        char head() {
            return str.take(1).toCharacter() as char
        }

        Input tail() {
            return new Input(str.drop(1))
        }
    }
    
    @CompileStatic
    static final class Output {
        private final String text
        private Output(String str) {
            this.text = str
        }

        static Output empty() {
            return new Output("")
        }

        Output append(char c) {
            return new Output(text + c)
        }
    }

    static void main(String[] args) {
        println new Brainfuck(args[0]).process(args.length > 1 ? args[1] : "")
    }
}
