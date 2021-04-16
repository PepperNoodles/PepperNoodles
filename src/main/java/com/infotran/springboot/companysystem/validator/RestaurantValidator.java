package com.infotran.springboot.companysystem.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.infotran.springboot.commonmodel.Restaurant;

@Component
public class RestaurantValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		System.out.println(clazz.getName());
		boolean b = Restaurant.class.isAssignableFrom(clazz);
		return b;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Restaurant restaurant = (Restaurant) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantName", "restaurant.restaurantName.not.empty",
				"餐廳名稱不能空白(預設值)");
		if (restaurant.getRestaurantName().length() < 1) {
			errors.rejectValue("restaurantName", "", "餐廳名稱不能小於1個字元");
		}

	}
}
