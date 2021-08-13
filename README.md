# 美食地圖

要執行前的注意事項(有人想到可以在自己補充) 
1.記得要自己去資料庫role表格新增 normal,company,admin 共3筆資料 
  insert into roles values ('normal'),('company'),('admin')
2.到資料庫companyDetail的設計->把[userphoto]欄位改成varbinary(max)[ddl的自動生成會是varbinary(255)] 
3.註冊完後要記得到userAccount手動把enable欄位變成1才能登入,之後要改 
---------------------------------------------------------------
4. 請將埠號都改成433
5. 請打開ngrok，然後登入ngrok的網站(https://dashboard.ngrok.com/login)
6. 在ngrok上先打taskkill /f /im ngrok.exe
7. 再把你網站上的authtoken複製貼到ngrok上面(在第2點Connect your account從斜線後複製，不要複製到斜線)
8. 貼完後再打 ngrok http 433  
9. 出現online就代表連線成功
10. 然後複製第二個Forwarding的==>(例如:https://ce90e75b1f39.ngrok.io 每次連線都會變不要複製這個)
11. ==================line功能 =========================
12. ====你輸入↓================得到↓============
13. 我要尋找餐廳=======>收到，請問您要搜尋甚麼餐廳? 
14. 台北市大安區=======>5家餐廳
---------------------------------------------------------------
15. 到資料庫eventList的設計->把[EventPicture]欄位改成varbinary(max)[ddl的自動生成會是varbinary(255)]
16. 到資料庫restaurantMessageBox、restaurantReplyMessage的設計->把[Time]欄位改成datetime2(7) 
