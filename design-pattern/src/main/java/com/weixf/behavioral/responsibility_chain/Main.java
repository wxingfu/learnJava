package com.weixf.behavioral.responsibility_chain;

/**
 *
 *
 * @since 2022-08-23
 */
public class Main {
    public static void main(String[] args) {
        Processor p1 = new ProcessorImpl1(null);
        Processor p2 = new ProcessorImpl2(p1);
        p2.process("something happened");


        NewProcessor<String> p3 = param -> System.out.println("processor 3 is processing:" + param);
        NewProcessor<String> p4 = param -> System.out.println("processor 4 is processing:" + param);
        p3.andThen(p4).process("something happened");
    }
}
