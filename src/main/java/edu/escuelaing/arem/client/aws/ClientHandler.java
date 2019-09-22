package edu.escuelaing.arem.client.aws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class ClientHandler implements Runnable {

    private URL url;

    ClientHandler(URL url) {

        this.url = url;
    }

    @Override
    public void run() {

        Long start = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream()))) {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                // System.out.println(inputLine);;
            }
        } catch (IOException x) {
            System.err.println(x);
        }

        System.out.println("SPEND TIME:"+ (System.currentTimeMillis()-start));
    }
}
