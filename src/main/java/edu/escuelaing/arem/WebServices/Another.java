package edu.escuelaing.arem.WebServices;

import edu.escuelaing.arem.service.Web;

public class Another {

    @Web("holi")
    public static String holi() {
//        File file = new File("../resources/Pages/hello.html");
//        file.getParentFile().mkdirs();
        return "HOLI";
    }

    @Web("chao")
    public static String chao() {
//        File file = new File("../resources/Pages/hello.html");
//        file.getParentFile().mkdirs();
        return "CHAO";
    }

}
