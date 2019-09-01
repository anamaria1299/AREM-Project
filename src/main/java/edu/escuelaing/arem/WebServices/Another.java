package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

import java.io.File;

@Web("Service")
public class Another {

    @Web("holi")
    public static String holi() {
        File file = new File("../resources/Pages/hello.html");
        return "HOLI";
    }

    @Web("chao")
    public static String chao() {
        File file = new File("../resources/Pages/hello.html");
        return "CHAO";
    }

}
