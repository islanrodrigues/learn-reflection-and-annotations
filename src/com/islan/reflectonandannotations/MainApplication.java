package com.islan.reflectonandannotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;

import com.islan.reflectonandannotations.annotation.MinAge;
import com.islan.reflectonandannotations.model.User;

public class MainApplication {

	public static void main(String[] args) {
		User user = new User("Islan", LocalDate.of(2002, 7, 25), "94100260083");
		
		System.out.println(user.toString());
		
		try {
			if (minAgeValidator(user)) {
				System.out.println("** Age above minimum age **");
				
			} else {
				System.out.println("** Age below minimum age **");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean minAgeValidator(Object entity) throws Exception {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			if (field.isAnnotationPresent(MinAge.class)) {
				MinAge minAge = field.getAnnotation(MinAge.class);
				
				int minAgeValue = minAge.value();
				field.setAccessible(true);
				LocalDate birthDate = (LocalDate) field.get(entity);
				LocalDate now = LocalDate.now();
				
				return Period.between(birthDate, now).getYears() >= minAgeValue;
			}
		}
		
		return false;
	}

}
