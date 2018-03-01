package com.i5i58.live.aop.http;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Lee on 2017/4/11.
 */

@Target({METHOD})
@Retention(RUNTIME)
public @interface HttpResultCut {
        String value() default "success";
}
