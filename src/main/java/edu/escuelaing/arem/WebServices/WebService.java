package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

@Web("Service")
public class WebService {

    @Web("hello")
    public static String hello() throws IOException {

        return getStringBuilderResult("src/main/resources/Pages/hello.html");
    }

    @Web("helloName")
    public static String helloName(HashMap<String, String> parameters) throws IOException {

        String result;
        if(!parameters.containsKey("name")) return  getStringBuilderResult("src/main/resources/Pages/invalidParameters.html");
        result = getStringBuilderResult("src/main/resources/Pages/hello.html");
        result = result.replaceAll("HELLO!", "Hello "+ parameters.get("name") + "!");
        return result;
    }

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

    @Web("bye")
    public static String bye() {

        return "bye";
    }

}
