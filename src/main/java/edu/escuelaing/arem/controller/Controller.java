package edu.escuelaing.arem.controller;

import edu.escuelaing.arem.service.HttpServer;

public class Controller {

    public static void main(String[] args) {

        HttpServer httpServer = new HttpServer();
        httpServer.initialize();
        httpServer.listen("apps/hello");
        httpServer.listen("apps/bye");
        httpServer.listen("apps/holi");
        httpServer.listen("apps/chao");
    }
}
