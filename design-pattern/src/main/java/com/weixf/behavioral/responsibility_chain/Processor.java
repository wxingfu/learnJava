package com.weixf.behavioral.responsibility_chain;

/**
 *
 *
 * @since 2022-08-23
 * 核心思想：每个处理环节，都有一个“指针”指向下一个处理者，类似链表一样。
 */
public interface Processor {
    Processor getNextProcessor();

    void process(String param);
}
