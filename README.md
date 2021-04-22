# PepperNoodles

要執行前的注意事項(有人想到可以在自己補充)
1.記得要自己去資料庫role表格新增 normal,company,admin 共3筆資料
  insert into roles values ('normal'),('company'),('admin')
2.到資料庫companyDetail的設計->把[userphoto]欄位改成varbinary(max)[ddl的自動生成會是varbinary(255)]
3.註冊完後要記得到userAccount手動把enable欄位變成1才能登入,之後要改
