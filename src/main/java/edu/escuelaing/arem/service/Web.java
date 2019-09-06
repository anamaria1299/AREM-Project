package edu.escuelaing.arem.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author Ana Rincon
 * The annotation class to the framework
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Web {

    String value();
}
