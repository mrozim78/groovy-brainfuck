package com.github.wololock

import spock.lang.Specification

class BrainfuckSpec extends Specification {

    def "should output 0 byte"() {
        given:
        final program = "."
        final expected = new String([0] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should increment the cell value"() {
        given:
        final program = "++++."
        final expected = new String([4] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should decrement the cell value"() {
        given:
        final program = "++++---."
        final expected = new String([1] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should move data pointer to the right"() {
        given:
        final program = "++++>++."
        final expected = new String([2] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should move data pointer to the left"() {
        given:
        final program = ">++++>+++<-.<++."
        final expected = new String([3,2] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should interpret - opcode with unsigned byte overflow"() {
        given:
        final program = "+---."
        final expected = new String([254] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should interpret >,<,+,-,. opcodes"() {
        given:
        final program = "++.>+++.>+.<++.<-."
        final expected = new String([2,3,1,5,1] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should accept 3 chars and increment each by 2"() {
        given:
        final program = ",++.>,++.>,++."
        final input = "abc"
        final expected = "cde"

        when:
        final result = new Brainfuck(program).process(input)

        then:
        result == expected
    }

    def "should interpret loop"() {
        given:
        final program = "++++++++[>+++++++<-]>++.>+++++[>+++++++++<-]>.>+++++[>++++++++<-]>+."
        final expected = ":-)" // new String([58,45,41] as char[])

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should interpret Hello World program"() {
        given:
        final program = "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++."
        final expected = "Hello World!\n"

        when:
        final result = new Brainfuck(program).process()

        then:
        result == expected
    }

    def "should accept input until chr(255)"() {
        given:
        final program = ",+[-.,+]"
        final input = "Lorem ipsum" + ((char) 255) + " dolor sit amet"
        final expected = "Lorem ipsum"

        when:
        final result = new Brainfuck(program).process(input)

        then:
        result == expected
    }

    def "should accept input until chr(0)"() {
        given:
        final program = ",[.[-],]"
        final input = "Groovy" + ((char) 0) + " Rocks!"
        final expected = "Groovy"

        when:
        final result = new Brainfuck(program).process(input)

        then:
        result == expected
    }

    def "should multiply two numbers"() {
        given:
        final program = ",>,<[>[->+>+<<]>>[-<<+>>]<<<-]>>."
        final input = new String([12,6] as char[])
        final expected = new String([12 * 6] as char[])

        when:
        final result = new Brainfuck(program).process(input)

        then:
        result == expected
    }
}
