INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (1,'2020-08-29T18:24:59.748118500','123dd','123dd');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (2,'2020-08-29T18:23:59.748118500','241gg','241gg');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (3,'2020-08-29T18:21:59.748118500','24r4f','24r4f');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (4,'2020-08-29T18:22:59.748118500','awd1d','awd1d');

INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (1,2,1,1,'2020-01-29T18:22:51.748118500','Вау');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (2,2,2,2,'2020-08-29T18:12:56.748118500','Круто');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (3,1,3,4,'2020-07-29T18:13:49.748118500','Ничего не понимаю');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (4,1,1,1,'2020-08-29T18:12:59.748118500','Сколько времени ?');

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (1,1,1,'2020-01-29T18:22:51.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (2,2,2,'2020-02-29T18:22:55.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (3,1,3,'2020-03-29T18:22:52.748118500',-1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (4,2,3,'2020-01-29T18:22:55.748118500',-1);

INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (1,1,3);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (2,2,3);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (3,3,3);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (4,3,3);

INSERT INTO tags (id,name) VALUES (1,'тэг1');
INSERT INTO tags (id,name) VALUES (2,'тэг2');
INSERT INTO tags (id,name) VALUES (3,'тэг3');
INSERT INTO tags (id,name) VALUES (4,'тэг4');

INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (1,1,'2020-08-29T18:22:51.748118500','Nick','ekocaba@mail.ru','1263','d22wd2','awdawd32');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (2,0,'2020-08-29T18:22:52.748118500','Max','ya.kocaba@yandex.ua','1253','ddwd2','awdawd32');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (3,1,'2020-08-29T18:22:53.748118500','Bella','kocaba@sfedu.ru','1243','dw23d2','awdawd32');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (4,0,'2020-08-29T18:22:54.748118500','Max','ekocaba@gmail.com','122','dw5d2','awdawd32');

INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (1,1,'NEW',4,1,'2020-08-29T18:22:50.748118500','Физика','Hello world1!',124532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (2,0,'NEW',4,2,'2020-08-29T18:22:59.748118500','Физра','Hello world2!',1532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (3,1,'NEW',4,3,'2020-08-29T18:22:57.748118500','Книга','Hello world3!',12532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (4,0,'NEW',4,2,'2020-08-29T18:22:55.748118500','Программа','Hello world4!',1534532);