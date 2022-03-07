package org.example.dto;

import java.util.List;

public class PrintableNode {
    private final String name;
    private final List<PrintableProperty> printableProperty;

    public PrintableNode(String name, List<PrintableProperty> printableProperty) {
        this.name = name;
        this.printableProperty = printableProperty;
    }

    public String getName() {
        return name;
    }

    public List<PrintableProperty> getPrintableProperty() {
        return printableProperty;
    }

    @Override
    public String toString() {
        return "{" +
                "name:'" + name + '\'' +
                ", property:" + printableProperty +
                '}';
    }
}
