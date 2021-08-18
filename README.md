# 美食地圖 - 呼叫美食地圖胡椒MAP！！

## 專案介紹
#### 由於職場上班族工作繁忙以及雙薪家庭的普及，外食人口日益增多，在此如何選擇自己要吃什麼是現代人的一大問題，以此為緣由開發讓使用者們與餐廳店家互動平台。

## 主要功能

|  系統  |    子功能    | 介紹 | 
|:------:|:----------:|:------------:|
|  地圖  |    搜尋餐廳、附近搜尋、名稱搜尋、標籤搜尋、使用Line查詢餐廳    | 使用OSM+leaflet地圖，利用覆蓋層標示餐廳使用line bot 自動回覆使用者查詢的餐廳 | 
|  使用者  |    好友系統、聊天系統、會員註冊、個資調整、餐廳收藏、留言按讚   | 建立好友關係、新增好友聊天室，利用spring boot websocket進行好友聊天、JavaMail信箱驗證、圖形驗證碼驗證、興趣標籤註冊、忘記密碼、個資修改、興趣標籤、修改消費者喜愛餐廳加入與刪除、已加入收藏的功能判斷、使用者留言區增刪改查、留言回覆按讚、按讚顯示 | 
|  企業  |    企業會員註冊、修改    | 企業會員註冊，使用Java Script做前端判斷格式是否吻合、基本資料修改、密碼修改並加密 | 
|  餐廳  |    餐廳基本資料、資料檢查、餐廳標籤、營業時間、菜單新增刪除、餐廳活動建立修改刪除、餐廳評論、評論回覆與刪除   | 前端JS格式檢查、餐聽地址轉換座標、AJAX驗證資料庫地址註冊、Server端Validator檢查。使用jQuery Twitter/Typeahead 和 Bloodhound做餐廳標籤的輸入和關鍵字搜尋。營業時間的增刪查改、前端+Server端檢查輸入資料、AJAX存取。使用AJAX的技術完成餐廳評論以及針對該筆評論做回覆的功能，顯示畫面時能夠區分該則留言or回覆留言的人是餐廳還是一般會員。判斷登入者，讓評論訊息僅能發表人才能刪除。 | 
|  商城  |    購物車、訂單、結帳、綠界金流串接、訂單保留、及自動取消未付款訂單、商品銷售報表    | 根據使用者興趣、使用者輸入、商品類別、標籤、價格進行篩選及分頁、金流串接、自訂義線程池並實現訂單定時功能、訂單建立使用snowflake算法產生全局唯一id、使用Chart.js產生月份總業績報表 | 
|  後台  |    餐廳、一般會員、企業會員、寄信通知、訊息通知    | 後臺查詢餐廳以及修改餐廳、查詢一般或企業會員、違規停權、給於違規停權者寄送停權與開通權限通知的信件、訪客利用聯絡我們功能與後臺系統管理者進行訊息留言與提問 | 
|  安全  |    Spring Security、reCAPTCHA 機器人驗證    | Spring Security 系統串接、角色權限設定、聯絡我們防機器人提交訊息驗證 | 

## 使用技術
![技術圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/EEIT23.pptx.jpg "技術圖")

## 專案ERD圖
![erd圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/erd.jpg "ERD圖")

## 網頁範例(例: 首頁、地圖、商城)
![首頁圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E9%A6%96%E9%A0%81.jpg "首頁圖")

![地圖圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E5%9C%B0%E5%9C%96.jpg "地圖圖")

![商城圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E5%95%86%E5%9F%8E.jpg "商城圖")




---
## 執行前注意事項(小組成員觀看) 
1. 記得要自己去資料庫role表格新增 normal,company,admin 共3筆資料 
   1. ``` insert into roles values ('normal'),('company'),('admin') ```
2. 到資料庫companyDetail的設計->把[userphoto]欄位改成varbinary(max)[ddl的自動生成會是varbinary(255)] 
3. 註冊完後要記得到userAccount手動把enable欄位變成1才能登入,之後要改 
4. 請將埠號都改成`443`
5. 請打開ngrok，然後登入ngrok的網站(https://dashboard.ngrok.com/login)
6. 在ngrok上先打 **taskkill /f /im ngrok.exe**
7. 再把你網站上的authtoken複製貼到ngrok上面(在第2點Connect your account從斜線後複製，不要複製到斜線)
8. 貼完後再打 ngrok http 443  
9. 出現online就代表連線成功
10. 然後複製第二個Forwarding的==>(例如:https://ce90e75b1f39.ngrok.io 每次連線都會變不要複製這個)
11. 到資料庫eventList的設計->把[EventPicture]欄位改成varbinary(max)[ddl的自動生成會是varbinary(255)]
12. 到資料庫restaurantMessageBox、restaurantReplyMessage的設計->把[Time]欄位改成datetime2(7) 
13. #### LineBot 測試功能

|  功能  |    負責人    | 輸入 | 得到  |
|:------:|:----------:|:------------:|:------------:|
|  LineBot  |  chris  | `我要尋找餐廳` | 收到，請問您要搜尋甚麼餐廳? |
|  LineBot  |  chris  | `台北市大安區` | 5家餐廳地址 |


