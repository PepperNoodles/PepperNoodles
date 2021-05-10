package com.infotran.springboot.userAccsystem.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.infotran.springboot.commonmodel.ReCaptchaResponse;

@Controller
public class UserAccountViewController {
	
	
	@Value("${recaptcha.secret}")
	private String recaptchaSecret;
	
	@Value("${recaptcha.url}")
	private String recaptchaServerURL;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	private boolean verifyReCAPTCHA(String gRecatchaResponse) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("secret", recaptchaSecret);
		map.add("response", gRecatchaResponse);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
		
//		ResponseEntity<String> responseOR = restTemplate.postForEntity(recaptchaServerURL, request, String.class);
//		System.out.println(responseOR);
		ReCaptchaResponse response = restTemplate.postForObject(recaptchaServerURL, request, ReCaptchaResponse.class);

		System.out.println("===========================================================================================");
		System.out.println("success: " + response.isSuccess());
		System.out.println("hostname: " + response.getHostname());
		System.out.println("challenge timestamp: " + response.getChallenge_ts());

		if(response.getErrorCodes()!=null) {
			for(String error: response.getErrorCodes()) {
				System.out.println("\t" + error);
			}
		}
		
		return response.isSuccess();

	}

	
	
	//聯絡我們
	@GetMapping(path="/contactUs")
	public String goContactUspage(Model model) {
		return "contactus";
	}
	
	
	//發出訊息
	@PostMapping(path="/sendContact")
	public String sendContactUspage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String gRecatchaResponse = request.getParameter("g-recaptcha-response");
		
		if(!verifyReCAPTCHA(gRecatchaResponse)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);

		}
		return "AfterSendContactMessage";

	}
	
	//驗證recaptcha ajax
	@GetMapping(path="/recaptchaajaxcheck")
	@ResponseBody
	public String recaptchaAjaxCheck(@RequestParam(value = "token") String token,HttpServletResponse response) throws IOException {
		
		
		if(!verifyReCAPTCHA(token)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return "機器人是在傳屁傳!";

		}
		return "不是機器人hen~棒唷(ﾉ>ω<)ﾉ";

	}




}
