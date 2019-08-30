package edu.escuelaing.arem.service;

import edu.escuelaing.arem.ClassesName;
import edu.escuelaing.arem.HandlerImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class HttpServer {

    private static ServerSocket serverSocket = null;
    private static boolean next = true;
    private static Socket clientSocket = null;
    private HashMap<String, HandlerImpl> handlerHashMap = new HashMap<String, HandlerImpl>();

    public void addWebMethods(String className) {
        try {
            Class<?> requestClass = Class.forName(className);
            Method[] allMethods = requestClass.getDeclaredMethods();
            for(Method m: allMethods) {
                if(m.isAnnotationPresent(Web.class)) {
                    handlerHashMap.put(m.getName(), new HandlerImpl(m, requestClass));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

        List<Class<?>> classes = ClassesName.getClassesInPackage("edu.escuelaing.arem.WebServices");
        System.out.println(classes);
//        String[] classNames= {"edu.escuelaing.arem.WebServices.WebService","edu.escuelaing.arem.WebServices.Another"};
//        for(String s: classNames) {
//            addWebMethods(s);
//        }
    }

    public void listen(String method) {

        if(method.contains("apps/")) {
            try {
                handlerHashMap.get(method.substring(5, method.length())).process();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

//    public void listen() {
//
//        try {
//            serverSocket = new ServerSocket(8080);
//            int counter = 0;
//            while(next) {
//                counter++;
//                System.out.println("Ready to recipe ...");
//                clientSocket = serverSocket.accept();
//
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
