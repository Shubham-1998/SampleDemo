package com.sample.example.sampledemo;

public class SampleData {

    private String name, age;

    public SampleData()
    {
        //default constructor
    }

    public SampleData(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
