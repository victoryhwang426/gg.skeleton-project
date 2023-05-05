insert into `member` (`user_id`, `user_name`) select * from CSVREAD('classpath:data/user.csv');
update `member` set `created_at` = current_date, `modified_at` = current_date;

insert into `product` (`product_id`,`product_name`,`price`) select * from CSVREAD('classpath:data/product.csv');
update `product` set `created_at` = current_date, `modified_at` = current_date;

insert into `purchase` (`product_id`,`user_id`,`price`) select * from CSVREAD('classpath:data/purchase.csv');
update `purchase` set `created_at` = current_date, `modified_at` = current_date;
update `purchase` set `year_of_created_at` = 2023, `month_of_created_at` = 3;