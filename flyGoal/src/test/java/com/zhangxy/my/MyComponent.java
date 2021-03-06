package com.zhangxy.my;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

@Retention(RUNTIME)
@Target({TYPE, METHOD })
@Documented
@Component
public @interface MyComponent {
    String value() default "123";
}
