package org.example.dto;

import java.util.List;

public class PrintableProperty {

    private final String name;
    private final List<String> values;

    public PrintableProperty(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", values:" + values +
                '}';
    }
}
