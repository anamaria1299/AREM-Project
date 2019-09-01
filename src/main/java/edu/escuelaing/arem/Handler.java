package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public interface Handler {

    String process(HashMap<String, String> queries) throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
