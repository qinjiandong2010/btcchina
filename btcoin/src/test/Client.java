package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Client {
	public static void main(String[] args) {  
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-client.xml");  
        IHelloWorld helloWorld = (IHelloWorld) context.getBean("helloWorldClient");  
        System.out.println(helloWorld.sayHi("Test"));  
    } 
}
