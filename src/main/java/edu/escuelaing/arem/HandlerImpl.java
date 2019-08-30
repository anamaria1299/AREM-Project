package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerImpl implements Handler{

    private Method method;
    private Class<?> requestClass;

    public HandlerImpl(Method method, Class<?> requestClass) {
        this.method = method;
        this.requestClass = requestClass;
    }

    public void process() throws IllegalAccessException, InstantiationException, InvocationTargetException {

        System.out.println(method.invoke(requestClass.newInstance()));
    }
}
