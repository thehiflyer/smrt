package se.smrt.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SmrtProtocol {
	public static final String DEFAULT_NAME = "";

	String value() default DEFAULT_NAME;
}