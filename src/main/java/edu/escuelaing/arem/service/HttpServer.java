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

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

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
            executor.submit(() -> {
                try {
                    processingRequests(serverSocket.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void processingRequests(Socket clientSocket) {

        String path = null;
        try {
            path = getPath(clientSocket.getInputStream());
            URL url = new URL("http://host" + path);
            HashMap<String, String> queries = handleUrl(url);
            resolvingRequest(url.getPath(), queries, clientSocket.getOutputStream());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * Get the path of a given request
     * @param inputStream Input require to parse the path
     * @return The current path
     * @throws IOException If an exception happens it is necessary to know it and throws it to another method that will
     * support it
     */
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

    /**
     * Handle the url to know if this one has any query
     * @param url The requires url to handle the request
     * @return The queries that the current request has
     * @throws MalformedURLException Throws an exception if is a malformed exception
     */
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

    /**
     * Method to resolve the request and know if it is an html page or other kind of resource
     * @param method The method that is being requested
     * @param queries The queries that the request can have
     * @param outputStream Necessary to write the response
     */
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

    /**
     * Get the image resource given the name of the resource and the outputStream
     * @param outputStream Given to write the response
     * @param resource Given to know the resource name
     */
    private void getImage(OutputStream outputStream, String resource) {

        PrintWriter response = new PrintWriter(outputStream, true);
        BufferedImage image= null;
        try {
            image = ImageIO.read(new File(System.getProperty("user.dir"),"src/main/resources/Images"+resource));
            DataOutputStream dataOutputStream = getImageLikeBytes(outputStream, image);
            response.println(dataOutputStream.toString());
        } catch (IOException e) {
            HTMLNotFound(response);
        }
    }

    /**
     * Get the image like bytes to write the response
     * @param outputStream Given to write the response
     * @param image The image that is being requested
     * @return The data output stream
     * @throws IOException throws it if some exception occurred
     */
    private DataOutputStream getImageLikeBytes(OutputStream outputStream, BufferedImage image) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeBytes("HTTP/1.1 200 OK\r\n"+"Content-Type: image/png\r\n");
        dataOutputStream.writeBytes("Content-Length: "+ imageByte.length);
        dataOutputStream.writeBytes("\r\n\r\n");
        dataOutputStream.write(imageByte);
        dataOutputStream.close();
        return dataOutputStream;
    }

    /**
     * Handle the HTML pages calling to the handler class
     * @param method The given method needed to know what the client is looking for
     * @param outputStream Given to write the response
     * @param queries Given if the request has any query
     * @throws Exception
     */
    private void handleHTMLPages(String method, OutputStream outputStream, HashMap<String, String> queries) throws Exception {

        String realMethod = method.substring(6, method.length());
        if(!handlerHashMap.containsKey(realMethod)) {
            HTMLNotFound(new PrintWriter(outputStream, true));
        }
        String htmlResponse = handlerHashMap.get(realMethod).process(queries);
        PrintWriter response = new PrintWriter(outputStream, true);
        headerHTMLHeader(response);
        response.println(htmlResponse);
        response.flush();
        response.close();
    }

    /**
     * Build the header to an HTML page
     * @param response when it is going to write the header
     */
    private void headerHTMLHeader(PrintWriter response) {

        response.println("HTTP/1.1 200 OK\r\n"+"Content-Type: text/html\r\n"+"\r\n");
    }

    /**
     * Build an HTML page when the request is not found
     * @param response when it is going to write the header
     */
    private void HTMLNotFound(PrintWriter response) {

        headerHTMLHeader(response);
        writeHTML("src/main/resources/Pages/notFound.html", response);
        response.flush();
        response.close();
    }

    /**
     * Write the HTML file into the response
     * @param pathFile The path of the HTML requested
     * @param response when it is going to write the header
     */
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
