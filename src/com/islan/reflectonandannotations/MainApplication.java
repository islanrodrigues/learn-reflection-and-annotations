package com.islan.reflectonandannotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;

import com.islan.reflectonandannotations.annotation.CPF;
import com.islan.reflectonandannotations.annotation.MinAge;
import com.islan.reflectonandannotations.model.User;

public class MainApplication {

	public static void main(String[] args) {
		User user = new User("Islan", LocalDate.of(2002, 7, 25), "78652976104");
		
		System.out.println(user.toString());
		
		try {
			
			if (minAgeValidator(user)) {
				System.out.println("** Age above minimum age **");
				
			} else {
				System.out.println("** Age below minimum age **");
			}
			
			if (cpfValidator(user)) {
				System.out.println("** CPF valid **");
			
			} else {
				System.out.println("** CPF invalid **");
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
		
		throw new RuntimeException("@MinAge Annotation not found in the entity.");
	}
	
	
	public static boolean cpfValidator(Object entity) throws Exception {
		Class<?> clazz = entity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			if (field.isAnnotationPresent(CPF.class)) {
				
				field.setAccessible(true);
				String cpf = (String) field.get(entity);
				
				return isCPFValid(cpf);
			}
		}
		
		throw new RuntimeException("@CPF Annotation not found in the entity.");
		
	}
	
	
	public static boolean isCPFValid(String cpf) {
		if (cpf.isEmpty() || cpf.length() != 11) {
			return false;
		}
		
		for (Character digit : cpf.toCharArray()) {
			if (!Character.isDigit(digit)) {
				return false;
			}
		}
		
		//-------- FIRST DIGIT VALIDATION --------
		//-- First step
		Integer sum = 0;
		Integer auxCount = 10;
		
		for (int i=0; i <= 8; i++) {
			sum = sum + (cpf.charAt(i) - '0') * auxCount;
			auxCount--;
		}
		
		//-- Second step
		Integer quotient = (sum * 10) % 11;
		Integer firstDigitVerifier = cpf.charAt(9) - '0';
		
		quotient = quotient.intValue() == 10 ? 0 : quotient;
		
		if (!firstDigitVerifier.equals(quotient)) {
			return false;
		}
		
		//-------- SECOND DIGIT VALIDATION --------
		//-- First step
		sum = 0;
		auxCount = 11;
				
		for (int i=0; i <= 9; i++) {
			sum = sum + (cpf.charAt(i) - '0') * auxCount;
			auxCount--;
		}
		
		//-- Second step
		quotient = (sum * 10) % 11;
		Integer secondDigitiVerifier = cpf.charAt(10) - '0';
		
		quotient = quotient.intValue() == 10 ? 0 : quotient;
		
		if (!secondDigitiVerifier.equals(quotient)) {
			return false;
		}
		
		return true;
	}

}
