package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

public class WebService {

    @Web("hello")
    public static String hello() {
//        File file = new File("../resources/Pages/hello.html");
//        file.getParentFile().mkdirs();
        return "hola";
    }

    @Web("bye")
    public static String bye() {
//        File file = new File("../resources/Pages/hello.html");
//        file.getParentFile().mkdirs();
        return "bye";
    }

}
