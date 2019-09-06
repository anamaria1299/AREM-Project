package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @Author Ana Rincon
 * Interface to handle the HTTP Server requests
 */
public interface Handler {

    String process(HashMap<String, String> queries) throws IllegalAccessException, InstantiationException, InvocationTargetException;
}
