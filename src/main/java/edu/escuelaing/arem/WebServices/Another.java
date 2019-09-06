package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

import java.io.File;

/**
 * @Author Ana Rincon
 * The example of class where its methods will be public into the HTTP Server
 */
@Web("Service")
public class Another {

    /**
     * Give bye to a request
     * @return String saying hello
     */
    @Web("holi")
    public static String holi() {
        File file = new File("../resources/Pages/hello.html");
        return "HOLI";
    }

    /**
     * Give bye to a request
     * @return String saying bye in spanish
     */
    @Web("chao")
    public static String chao() {
        File file = new File("../resources/Pages/hello.html");
        return "CHAO";
    }

}
