package com.codeit.bean.setting;

import org.springframework.stereotype.Component;

@Component
public class SomeBean {
    private String someProperty = "SomeBeanValue";
    public String getSomeProperty() {
        return someProperty;
    }
}