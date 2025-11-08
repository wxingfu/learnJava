package com.weixf.behavioral.responsibility_chain;

/**
 *
 *
 * @since 2022-08-23
 */
public class ProcessorImpl2 extends AbstractProcessor {

    public ProcessorImpl2(Processor next) {
        super(next);
    }

    @Override
    public void process(String param) {
        System.out.println("processor 2 is processing:" + param);
        if (getNextProcessor() != null) {
            getNextProcessor().process(param);
        }
    }
}
