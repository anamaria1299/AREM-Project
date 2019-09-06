package edu.escuelaing.arem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class HandlerImpl implements Handler{

    private Method method;
    private Class<?> requestClass;

    /**
     * Constructor
     * @param method Method that needs to be invoke for the request
     * @param requestClass The request class of the given method
     */
    public HandlerImpl(Method method, Class<?> requestClass) {
        this.method = method;
        this.requestClass = requestClass;
    }

    /**
     * Process a request
     * @param queries The possible queries that the method needs
     * @return An string that contains the HTML file requested
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public String process(HashMap<String, String> queries) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        if(!queries.isEmpty()) return (String) method.invoke(requestClass.newInstance(), queries);
        return (String) method.invoke(requestClass.newInstance());

    }
}
