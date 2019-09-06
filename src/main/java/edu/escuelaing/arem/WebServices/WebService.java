package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Author Ana Rincon
 * The example of class where its methods will be public into the HTTP Server
 */
@Web("Service")
public class WebService {

    /**
     * Give an HTML file
     * @return Return the HTML like string
     * @throws IOException
     */
    @Web("hello")
    public static String hello() throws IOException {

        return getStringBuilderResult("src/main/resources/Pages/hello.html");
    }

    /**
     * Give an HTML file saying hello to an specific person
     * @param parameters Queries that are needed
     * @return Return the HTML like string
     * @throws IOException
     */
    @Web("helloName")
    public static String helloName(HashMap<String, String> parameters) throws IOException {

        String result;
        if(!parameters.containsKey("name")) return  getStringBuilderResult("src/main/resources/Pages/invalidParameters.html");
        result = getStringBuilderResult("src/main/resources/Pages/hello.html");
        result = result.replaceAll("HELLO!", "Hello "+ parameters.get("name") + "!");
        return result;
    }

    /**
     * Read HTML File and convert it into a string
     * @param path The path of the current HTML requested
     * @return Return the HTML like string
     * @throws IOException
     */
    private static String getStringBuilderResult(String path) throws IOException {

        FileReader reader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        reader.close();
        return result.toString();
    }

    /**
     * Give bye to a request
     * @return String saying bye
     */
    @Web("bye")
    public static String bye() {

        return "bye";
    }

}
