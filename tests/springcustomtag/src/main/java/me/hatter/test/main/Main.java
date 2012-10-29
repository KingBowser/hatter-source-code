package me.hatter.test.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        String xml = "classpath:me/hatter/test/main/test.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { xml });
        System.out.println(context.getBean("testCustom"));
    }
}
