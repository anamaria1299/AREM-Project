package edu.escuelaing.arem.controller;

import edu.escuelaing.arem.service.HttpServer;

public class Controller {

    public static void main(String[] args) {

        HttpServer httpServer = new HttpServer();
        httpServer.initialize();
        httpServer.listen();
    }
}
