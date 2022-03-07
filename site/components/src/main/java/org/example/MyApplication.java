package org.example;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class MyApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(InternalServerErrorException.class);
        /* add your additional JAX-RS classes here */
        return classes;
    }
}