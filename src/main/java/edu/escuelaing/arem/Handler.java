package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;

public interface Handler {

    void process() throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
