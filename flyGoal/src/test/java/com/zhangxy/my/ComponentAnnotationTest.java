package com.zhangxy.my;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentAnnotationTest {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(ComponentAnnotationTest.class);
        annotationConfigApplicationContext.refresh();
        final InjectClass injectClass = annotationConfigApplicationContext.getBean(InjectClass.class);
        injectClass.print();
    }

    @MyComponent
    public static class InjectClass {
        public void print() {
            System.out.println("hello world");
        }
    }
}