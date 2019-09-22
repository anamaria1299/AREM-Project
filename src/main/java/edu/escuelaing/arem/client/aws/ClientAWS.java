package edu.escuelaing.arem.client.aws;

import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ClientAWS {

    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {

        URL url = new URL(args[0]);
        for(int i= 0; i < 200; i++) {
            ClientHandler clientHandler = new ClientHandler(url);
            executor.execute(clientHandler);
        }
    }
}
