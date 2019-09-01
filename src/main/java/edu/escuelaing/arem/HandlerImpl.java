package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class HandlerImpl implements Handler{

    private Method method;
    private Class<?> requestClass;

    public HandlerImpl(Method method, Class<?> requestClass) {
        this.method = method;
        this.requestClass = requestClass;
    }

    public String process(HashMap<String, String> queries) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        if(!queries.isEmpty()) return (String) method.invoke(requestClass.newInstance(), queries);
        return (String) method.invoke(requestClass.newInstance());

    }
}
