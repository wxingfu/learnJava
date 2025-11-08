package com.weixf.freemarker;

/**
 *
 *
 * @since 2022-10-10
 */
public class IntHashtableEntry {
    int hash;
    int key;
    Object value;
    IntHashtableEntry next;

    protected Object clone() {
        IntHashtableEntry entry = new IntHashtableEntry();
        entry.hash = hash;
        entry.key = key;
        entry.value = value;
        entry.next = (next != null) ? (IntHashtableEntry) next.clone() : null;
        return entry;
    }
}
