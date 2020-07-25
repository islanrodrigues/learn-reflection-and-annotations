package com.islan.reflectonandannotations;

import com.islan.reflectonandannotations.model.User;

public class MainApplication {

	public static void main(String[] args) {
		User user = new User("Islan", 23, "94100260083");

		System.out.println(user.toString());
	}

}
