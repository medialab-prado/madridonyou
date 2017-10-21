package com.madridonyou.micro.login.test;

public class StupidTests {

	public static void main(String[] args) {

		String x = "         ";
		System.out.println(x.length());
		
		x = x.replaceAll("\\s+","");
		System.out.println(x.length());
	}

}
