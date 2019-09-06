package edu.escuelaing.arem.controller;

import edu.escuelaing.arem.service.HttpServer;

/**
 * @Author Ana Rincon
 * This class is a controller who invoke the HTTP Server to initialize it and start listening
 */
public class Controller {

    /**
     * The main of the class invoke the HTTP Server
     * @param args
     */
    public static void main(String[] args) {

        try {
            HttpServer httpServer = new HttpServer();
            httpServer.initialize();
            httpServer.listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
