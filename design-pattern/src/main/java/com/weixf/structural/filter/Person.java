package com.weixf.structural.filter;

/**
 *
 *
 * @since 2022-08-25
 */
public class Person {

    private String name;
    private String gender;
    private String maritalStatus;

    public Person() {
    }

    public Person(String name, String gender, String maritalStatus) {
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

}
