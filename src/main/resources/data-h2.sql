insert into `member` (`user_id`, `user_name`) select * from CSVREAD('classpath:data/user.csv');
insert into `product` (`product_id`,`product_name`,`price`) select * from CSVREAD('classpath:data/product.csv');
insert into `purchase` (`purchase_no`,`user_id`,`product_id`,`price`) select * from CSVREAD('classpath:data/purchase.csv');