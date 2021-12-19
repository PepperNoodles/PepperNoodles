# 美食地圖 - 呼叫美食地圖胡椒MAP！！
<div>
 <img src="https://komarev.com/ghpvc/?username=PepperNoodles&label=Profile%20views&color=red&style=flat" alt="PepperNoodles" />
 <img src="https://img.shields.io/github/languages/code-size/PepperNoodles/PepperNoodles" alt="PepperNoodles" />
 </div> 
 <br>
 <div>
 <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
   <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white" />
   <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" />
   <img src="https://img.shields.io/badge/Leaflet-199900?style=for-the-badge&logo=Leaflet&logoColor=white" />
   <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" />
   <img src="https://img.shields.io/badge/-linebot-brightgreen?style=for-the-badge&logo=line&logoColor=white" />
   <img src="https://img.shields.io/badge/Microsoft%20SQL%20Server-CC2927?style=for-the-badge&logo=microsoft%20sql%20server&logoColor=white" />
   
   <img src="https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white" />
   <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" />
   <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" />
   <img src="https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E" />
   <img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white" />
   <img src="https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white" />
</div>
  
  
---

 <h2 ><img src="https://img.icons8.com/office/30/000000/training.png"/> &nbsp專案介紹: </h2>
 
### 由於職場上班族工作繁忙以及雙薪家庭的普及，外食人口日益增多，在此如何選擇自己要吃什麼是現代人的一大問題，以此為緣由開發讓使用者們與餐廳店家互動平台。

---

<h2 ><img src="https://img.icons8.com/offices/30/000000/content.png"/>&nbsp主要功能 </h2>

|  系統  |    子功能    | 介紹 | 
|:------:|:----------:|:------------|
|  地圖  |    搜尋餐廳、附近搜尋<br>名稱搜尋、標籤搜尋<br>使用Line查詢餐廳    | 1. 使用`OSM`+`leaflet`地圖，利用覆蓋層標示餐廳<br>2. 使用`line bot` 自動回覆使用者查詢的餐廳 | 
|  使用者  |    好友系統、聊天系統<br>會員註冊、個資調整<br>餐廳收藏、留言按讚   | 1. 建立好友關係、新增好友聊天室，利用`spring boot websocket`進行好友聊天<br>2. JavaMail信箱驗證、圖形驗證碼驗證、興趣標籤註冊、忘記密碼、個資修改、修改消費者喜愛餐廳加入與刪除<br>3. 使用者留言區增刪改查、留言回覆按讚、按讚顯示 | 
|  企業  |    企業會員註冊、修改    | 1. 企業會員註冊，使用`JavaScript`做前端判斷格式是否吻合<br>2. 基本資料修改、密碼修改並加密 | 
|  餐廳  |    餐廳基本資料、標籤<br>營業時間、菜單新增刪除<br>餐廳活動建立修改刪除<br>評論回覆與刪除   | 1. JS格式檢查<br>2. 地址轉換座標、驗證資料庫地址註冊<br>3. Server端`Validator`檢查<br>4.使用`jQuery Twitter/Typeahead` 和 `Bloodhound` 做餐廳標籤的輸入和關鍵字搜尋<br>5. 營業時間的增刪查改、前端+Server端檢查輸入資料<br>6. `AJAX`發送請求建構餐廳評論及針對該筆評論做回覆，並判斷回覆留言的人是餐廳還是一般會員<br> 7. 判斷登入者，讓評論訊息僅能發表人才能刪除。 | 
|  商城  |    購物車、訂單<br>結帳、綠界金流串接<br>訂單保留、及自動取消未付款訂單<br>商品銷售報表  | 1. 根據使用者興趣、使用者輸入、商品類別、標籤、價格進行篩選及分頁<br>2. 金流串接、自訂義線程池並實現訂單定時功能<br>3. 訂單建立產生全局唯一id並使用`Chart.js`產生月份總業績報表 | 
|  後台  |    餐廳、一般會員<br>企業會員、寄信通知<br>訊息通知    | 1. 後臺餐廳查詢修改<br>2. 查詢一般或企業會員、違規停權、寄送停權與開通權限的信件<br>3. 聯絡我們功能:使用者可對系統進行留言與提問 | 
|  安全  |    `Spring Security`<br>`reCAPTCHA` 機器人驗證    | 1. `Spring Security` 系統串接、角色權限設定、聯絡我們防機器人提交訊息驗證 | 

---
<h2 > 🔧 Technologies & Tools </h2>
  <img src="https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/EEIT23.pptx.jpg">
<!-- ![技術圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/EEIT23.pptx.jpg "技術圖") -->

1. 安裝軟體對應版本

|  軟體  |  版本  |  
|:------:|:--------:|
|  SpringBoot  | `2.4.4`   | 
|  JDK  | `11.0.10`   | 
|  MS SQLServer  | `MS SQLServer2019`  | 

2. 相關Maven依賴 

|  套件  |  版本  |
|:------:|:--------:|
|  mssql-jdbc  | `8.4.1.jre8` |  
|  javax.mail  | `1.6.2` |  
| spring-boot-starter-security | `2.4.4` |  
| spring-security-taglibs | `5.4.5` |  
|  line-bot-spring-boot  | `4.3.0` |  
|  tomcat-embed-jasper  | `9.0.44` |  
|  hutool-all  | `5.6.3` | 
|  jquery  | `3.5.1` |
|  bootstrap  | `4.6.0` |
|  jquery-ui  | `1.12.1` |

---

## 專案ERD圖
![erd圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/erd.jpg "ERD圖")

---

## 網頁範例(例: 首頁、地圖、商城)
![首頁圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E9%A6%96%E9%A0%81.jpg "首頁圖")

![地圖圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E5%9C%B0%E5%9C%96.jpg "地圖圖")

![商城圖](https://github.com/PepperNoodles/PepperNoodles/blob/master/src/main/webapp/images/%E5%95%86%E5%9F%8E.jpg "商城圖")


<!-- ## 執行前注意事項(小組成員觀看) 
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
|  LineBot  |  chris  | `台北市大安區` | 5家餐廳地址 | -->


