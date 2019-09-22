package edu.escuelaing.arem.service;

import edu.escuelaing.arem.HandlerImpl;
import org.reflections.Reflections;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author Ana Rincon
 * This is a HTTP Server class
 */
public class HttpServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private HashMap<String, HandlerImpl> handlerHashMap = new HashMap<String, HandlerImpl>();

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(50);

    /**
     * Initialize the server with all the methods that the webServices contains with annotation @WEB
     */
    public void initialize() {

        Reflections reflections = new Reflections("edu.escuelaing.arem.WebServices");
        Set<Class<? extends Object>> allClasses = reflections.getTypesAnnotatedWith(Web.class);
        System.out.println(allClasses.toString());
        for(Class c: allClasses) {
            addWebMethods(c.getName());
        }
    }

    /**
     * Add method from specific class into the handlerHashMap
     * @param className The classname require to get the methods
     */
    private void addWebMethods(String className) {

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

    /**
     * Start listening the HTTP Server
     */
    public void listen() {

        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ready to receive ...");
        while(true) {
            ClientHandler clientHandler = null;
            try {
                clientHandler = new ClientHandler(serverSocket.accept(), handlerHashMap);
                executor.execute((clientHandler));
            } catch (IOException e) {
                continue;
            }
        }

    }

    /**
     * Get a port where the application will be listening
     * the default port is 8080 but this can be also getting from an environment variable
     * @return The port
     */
    private int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8080;
    }
}
