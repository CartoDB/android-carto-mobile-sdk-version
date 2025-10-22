package com.carto.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to prevent ProGuard/R8 from obfuscating annotated classes.
 * This ensures that JNI reflection and other runtime lookups continue to work.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface DontObfuscate {
}

