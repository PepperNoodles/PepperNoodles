package com.infotran.springboot.companysystem.validator;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;

@Component
public class RestaurantValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean b = Restaurant.class.isAssignableFrom(clazz);
		return b;
	}
		/*supports()方法回傳一個boolean值，表示是否支援對所傳入的物件進行驗證，
		只有在傳回true的情況下，才會使用validate()方 法進行驗證工作，
		在validate()方法的參數中，obj表示傳入的表單物件，您可以對它進行一些驗證，如果有錯誤的話，
		可以使用Errors的 reject()或rejectValue()等方法加入錯誤訊息，在後續的處理中，
		若errors物件中包括錯誤訊息，會回到getViewPage ()所設定的頁面。*/

	@Override
	public void validate(Object target, Errors errors) {
		Restaurant restaurant = (Restaurant) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantName", "restaurant.restaurantName.not.empty",
				"名稱不能空白(預設值)");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantAddress", "",
				"地址不能空白(預設值)");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "restaurantContact", "",
				"聯絡方式不能空白(預設值)");
		if (restaurant.getRestaurantName().length() < 1) {
			errors.rejectValue("restaurantName", "", "餐廳名稱不能小於1個字元");
		}
		if (restaurant.getRestaurantContact().length() < 5) {
			errors.rejectValue("restaurantContact", "", "聯絡方式不能小於5個字元");
		}
		Set<FoodTag> foodtag = restaurant.getFoodTag();
		
		if (foodtag.isEmpty()) {
			errors.rejectValue("foodTag","", "必須挑選合格的Tag");
		}

	}
}
