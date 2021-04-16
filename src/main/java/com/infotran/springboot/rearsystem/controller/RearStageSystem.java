package com.infotran.springboot.rearsystem.controller;

import java.io.File;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.shoppingmall.model.Product;
import com.infotran.springboot.shoppingmall.service.ShoppingMallService;

@Controller
public class RearStageSystem {

	String imageRootDirectory = "D:\\_SpringBoot\\image";
	
	File imageFolder = null; 
	
	@Autowired
	ShoppingMallService service;
	
//	@Autowired
//	Product product;
	
	
	@PostMapping(value="/insproduct",consumes={"multipart/mixed","multipart/form-data","application/json"})
	public @ResponseBody Map<String,String> Registure( @RequestPart("productinfo")String toString,@RequestPart("file")MultipartFile file,HttpServletResponse response) throws Exception{
		System.out.println("1");
		Map<String, String> map = new HashMap<>();
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		Product product = new Product();
		product.setProductName(dispatch.get("productName"));
		product.setProductPrice(Integer.parseInt(dispatch.get("productPrice")));
		product.setDescription(dispatch.get("description"));
		product.setQuantity(Integer.parseInt(dispatch.get("quantity")));
		
		String originalFilename = file.getOriginalFilename();
		String extFilename = ""; 
		if (originalFilename.length() > 0 && originalFilename.lastIndexOf(".") > -1) {
			extFilename = originalFilename.substring(originalFilename.lastIndexOf("."));
			System.out.println(extFilename);
		}
		if (file != null && !file.isEmpty()) {
			try {
				byte[] b = file.getBytes();
				Blob blob = new SerialBlob(b);
				product.setProductImage(blob);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
			}
		}
		try {
			Product p = service.save(product);
			if(p != null) {
				map.put("success", "新增成功");
				response.setStatus(HttpServletResponse.SC_CREATED);
			}else {
				map.put("fail", "帳號重複");
			}
		}catch (Exception e) {
			map.put("fail2", e.getMessage());
		}
		
		try {
			File fileplace = new File(imageFolder, "UserImage_" + product.getProductName() + extFilename);
			file.transferTo(fileplace);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
		}
		return map;
	}
	
	
	
}
