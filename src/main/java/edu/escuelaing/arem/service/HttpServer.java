package edu.escuelaing.arem.service;

import edu.escuelaing.arem.HandlerImpl;
import org.reflections.Reflections;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class HttpServer {

    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private HashMap<String, HandlerImpl> handlerHashMap = new HashMap<String, HandlerImpl>();

    public void initialize() {

        Reflections reflections = new Reflections("edu.escuelaing.arem.WebServices");
        Set<Class<? extends Object>> allClasses = reflections.getTypesAnnotatedWith(Web.class);
        System.out.println(allClasses.toString());
        for(Class c: allClasses) {
            addWebMethods(c.getName());
        }
    }

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

    public void listen() {

        while(true) {
            try {
                serverSocket = new ServerSocket(getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Ready to receive ...");
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = null;
            try {
                path = getPath(clientSocket.getInputStream());
                URL url = new URL("http://host" + path);
                HashMap<String, String> queries = handleUrl(url);
                resolvingRequest(url.getPath(), queries, clientSocket.getOutputStream());
                clientSocket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8080;
    }

    private String  getPath(InputStream inputStream) throws IOException {

        inputStream.mark(0);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine = null ;
        while((inputLine=in.readLine())!=null) {

            System.out.println(inputLine);
            if (!in.ready()) break;
            if (inputLine.contains("GET")) {
                String [] get = inputLine.split(" ");
                return get[1];
            }
        }
        HTMLNotFound(new PrintWriter(clientSocket.getOutputStream(), true));
        return "";
    }

    private HashMap<String, String> handleUrl(URL url) throws MalformedURLException {

        HashMap<String, String> queries = new HashMap<>();
        if(url.getQuery() != null) {
            String [] parameters = url.getQuery().split("&");
            String[] keyValue = null;
            for(String s: parameters) {
                keyValue = s.split("=");
                queries.put(keyValue[0], keyValue[1]);
            }
        }
        return queries;
    }

    private void resolvingRequest(String method, HashMap<String,String> queries, OutputStream outputStream) {

        if(method.contains("/apps/")) {
            try {
                handleHTMLPages(method, outputStream, queries);
            } catch (Exception e) {
                HTMLNotFound(new PrintWriter(outputStream, true));
            }
        } else {
            getImage(outputStream, method);
        }
    }

    private void getImage(OutputStream outputStream, String resource) {

        PrintWriter response = new PrintWriter(outputStream, true);
        BufferedImage image= null;
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir"),"src/main/resources/Images"+resource));
            response.println("HTTP/1.1 200 OK\r\n");
            response.println("Content-Type: image/png\r\n");
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            HTMLNotFound(response);
        }
    }

    private void handleHTMLPages(String method, OutputStream outputStream, HashMap<String, String> queries) throws Exception {

        String realMethod = method.substring(6, method.length());
        if(!handlerHashMap.containsKey(realMethod)) {
            HTMLNotFound(new PrintWriter(outputStream, true));
        }
        String htmlResponse = handlerHashMap.get(realMethod).process(queries);
        PrintWriter response = new PrintWriter(outputStream, true);
        headerHTMLAccepted(response);
        response.println(htmlResponse);
        response.flush();
        response.close();
    }

    private void headerHTMLAccepted(PrintWriter response) {

        response.println("HTTP/1.1 200 OK\r\n");
        response.println("Content-Type: text/html\r\n");
    }

    private void HTMLNotFound(PrintWriter response) {

        response.println("HTTP/1.1 200 OK\r\n");
        response.println("Content-Type: text/html\r\n");
        writeHTML("src/main/resources/Pages/notFound.html", response);
        response.flush();
        response.close();
    }

    private void writeHTML(String pathFile, PrintWriter response) {

        try {
            FileReader reader = new FileReader(pathFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while((line = bufferedReader.readLine()) != null) {
                response.println(line + "\r\n");
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {

            System.err.println("Exception trying to get the file "+ pathFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
