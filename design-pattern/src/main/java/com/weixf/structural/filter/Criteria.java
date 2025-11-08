package com.weixf.structural.filter;

import java.util.List;

/**
 *
 *
 * @since 2022-08-25
 */
public interface Criteria {

    List<Person> meetCriteria(List<Person> persons);

}
