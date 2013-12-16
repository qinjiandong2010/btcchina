package test.impl;

import test.IHelloWorld;

public class HelloWorldImpl implements IHelloWorld {

	@Override
	public String sayHi(String name) {
		System.out.println("sayHello is called by " + name);
		return "Hello " + name;
	}

}
