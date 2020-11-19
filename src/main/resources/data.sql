INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (1,'2020-08-29T18:24:59.748118500','123dd','123dd');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (2,'2020-08-29T18:23:59.748118500','241gg','241gg');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (3,'2020-08-29T18:21:59.748118500','24r4f','24r4f');
INSERT INTO captcha_codes (id,time,code,secret_code) VALUES (4,'2020-08-29T18:22:59.748118500','awd1d','awd1d');

INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (1,null,1,1,'16029446621','Вау');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (2,null,2,2,'16029446621','Круто');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (3,1,1,4,'16029446621','Ничего не понимаю');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (4,null,1,1,'16029446621','Сколько времени ?');

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (1,2,1,'2020-01-29T18:22:51.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (2,2,1,'2020-02-29T18:22:55.748118500',-1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (3,2,2,'2020-02-29T18:22:55.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (4,2,2,'2020-02-29T18:22:55.748118500',-1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (5,1,3,'2020-03-29T18:22:52.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (6,2,3,'2020-01-29T18:22:55.748118500',-1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (7,1,5,'2020-01-29T18:22:55.748118500',-1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (8,2,5,'2020-01-29T18:22:55.748118500',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (9,4,4,'2020-01-29T18:22:55.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (10,1,4,'2020-01-29T18:22:55.748118500',1);
INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (11,2,4,'2020-01-29T18:22:55.748118500',1);

INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (1,1,2);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (2,1,3);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (3,1,4);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (4,3,4);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (5,4,1);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (6,4,2);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (7,4,5);
INSERT INTO tag_2_post (id,post_id,tag_id) VALUES (8,2,5);

INSERT INTO tags (id,name) VALUES (1,'Кто я');
INSERT INTO tags (id,name) VALUES (2,'Программа');
INSERT INTO tags (id,name) VALUES (3,'тэг3');
INSERT INTO tags (id,name) VALUES (4,'тэг4');
INSERT INTO tags (id,name) VALUES (5,'Программа новая');
INSERT INTO tags (id,name) VALUES (6,'тэг6');

INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (1,1,'2020-08-29T18:22:51.748118500','Nick','ekocaba@mail.ru','1263','d22wd2','photo1');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (2,0,'2020-08-29T18:22:52.748118500','Max','ya.kocaba@yandex.ua','1253','ddwd2','photo5');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (3,1,'2020-08-29T18:22:53.748118500','Bella','kocaba@sfedu.ru','1243','dw23d2','photo2');
INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (4,0,'2020-08-29T18:22:54.748118500','Max','ekocaba@gmail.com','122','dw5d2','photo');

INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (1,1,'ACCEPTED',4,1,'1602944662186','Философия','Что я!',124532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (2,1,'ACCEPTED',4,2,'1602944662186','Физра','А что если!',1532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (3,1,'ACCEPTED',4,3,'1602944662186','Hello world','Hello world3!',12532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (4,1,'ACCEPTED',4,2,'1602944662186','Программа','Привет world4!',15332);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (5,1,'ACCEPTED',4,1,'1034640000','Программа112вф','Hello world4!',154532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (6,0,'NEW',4,1,'1034640000','Физик123а','Hello world6!',12453);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (7,1,'ACCEPTED',4,1,'1602192294','Нет лайков','Опять же физика!',12422);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (8,1,'ACCEPTED',1,1,'1602192194','Проверка времени','Физика!',422);