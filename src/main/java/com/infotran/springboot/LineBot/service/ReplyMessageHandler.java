package com.infotran.springboot.LineBot.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.restaurantSearchSystem.service.RestSearchService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.TextMessage.Emoji;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.response.BotApiResponse;

@Service
public class ReplyMessageHandler {
	
	@Autowired
	RestSearchService restservice;
	
	final LineMessagingClient client = LineMessagingClient
	        .builder("ww5P0UIpl9jxB+a+dkO44euiFLfNhbUW+uqNjUue+dk/evwcYs/7ySV6iwwatmQwZExhR3polWKVDIJHxXMSehCExemJ4RRny0uFpYdWgwsp+Mi+643shY6fzzH/Ttqbn9iHPB8xm7GAlg7UH/klIwdB04t89/1O/w1cDnyilFU=")
	        .build();
	
	private static final String showAddress_Text = "我要尋找餐廳";
	private static final String orderAddress_Text = "台北市大安區";
	private static final String bookRestaurant_Text = "我要訂位";
	private ReplyMessage replyMessage = null;
	private boolean isRestNameExist = false;
	
	@SuppressWarnings("null")
	public BotApiResponse reply(MessageEvent<TextMessageContent> event)throws IOException{
		
		 String receivedMessage = event.getMessage().getText();
	     String replyToken = event.getReplyToken();
	     BotApiResponse botApiResponse = null;
	     
	     isRestNameExist = restservice.ifThisNameExists(receivedMessage);
	     
	     if (isRestNameExist) {
	    	 System.out.println("1");
	    	List<Action> actions = new ArrayList<Action>();
	    	URI imageSource = null;
			try {
				imageSource = new URI("https://e6a8bee006a9.ngrok.io/PepperNoodles/src/main/webapp/images/line/chef-cat.jpg");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			PostbackAction postbackaction = PostbackAction.builder()
														  .label("ReserveForTenMinutes")
														  .data("action=reserve")
														  .build();
			actions.add(postbackaction);
			ButtonsTemplate btnTemp = ButtonsTemplate.builder()
//									    	 	     .imageAspectRatio("square")
//									    	 	     .thumbnailImageUrl(imageSource)
									    	 	     .text(receivedMessage+"已幫您定位成功")
									    	 	     .actions(actions)
									    	 	     .build();
	    	TemplateMessage tempmessage = new TemplateMessage("無法顯示",btnTemp);
	    	System.out.println("here"+tempmessage);
	    	replyMessage = new ReplyMessage(replyToken,tempmessage);
	    	try {
				botApiResponse = client.replyMessage(replyMessage).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
	    	
	     }else {
	    	 switch (receivedMessage) {
				case showAddress_Text:
					List<Emoji> emojis = new ArrayList<Emoji>();
					Emoji emoji = Emoji.builder().index(14).productId("5ac1bfd5040ab15980c9b435").emojiId("002").build();
					emojis.add(emoji);
					TextMessage textMessage = TextMessage.builder().text("收到，請問您要搜尋甚麼餐廳?$").emojis(emojis).build();
					replyMessage = new ReplyMessage(replyToken,textMessage);
					try {
						botApiResponse = client.replyMessage(replyMessage).get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					break;
				case orderAddress_Text:
					//String addrName= "台北市大安區";
					System.out.println("receivedMessage="+receivedMessage);
					List<Restaurant> restlist = restservice.findAddressNameLike(receivedMessage);
					List<Message> locationlist = new ArrayList<Message>();
					int size = 5;
					for (int i=0;i<size;i++) {
						String resname = restlist.get(i).getRestaurantName();
						String address = restlist.get(i).getRestaurantAddress();
							BigDecimal longitude = restlist.get(i).getLongitude();
						Double dlong = longitude.doubleValue();
							BigDecimal latitude = restlist.get(i).getLatitude();
						Double dlat = latitude.doubleValue();
						LocationMessage localmessage = LocationMessage.builder()
																	  .title(resname)
																	  .address(address)
																	  .latitude(dlat)
																	  .longitude(dlong)
																	  .build();
						locationlist.add(localmessage);
					}
					replyMessage = new ReplyMessage(replyToken,locationlist);
					try {
						botApiResponse = client.replyMessage(replyMessage).get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					break;
				case bookRestaurant_Text:
					TextMessage textMessageForWhichRest = TextMessage.builder().text("好的，請問您要訂哪家餐廳?(請輸入餐廳名)").build();
					replyMessage = new ReplyMessage(replyToken,textMessageForWhichRest);
					try {
						botApiResponse = client.replyMessage(replyMessage).get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
	    	 }
	     }
	     return botApiResponse;
	}
	
	
	public BotApiResponse reply(PostbackEvent event)throws IOException{
		BotApiResponse botApiResponse = null;
		String replyToken = event.getReplyToken();
		String data = event.getPostbackContent().getData();
		System.out.println("data====>>>"+data);
		TextMessage textMessage = TextMessage.builder().text("已為您保留").build();
		replyMessage = new ReplyMessage(replyToken,textMessage);
		try {
			botApiResponse = client.replyMessage(replyMessage).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return botApiResponse;
	}
	
	
	
}
