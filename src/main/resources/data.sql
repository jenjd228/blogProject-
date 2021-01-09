

INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (1,null,1,1,'16029446621','Вау');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (2,null,2,1,'16029446621','Круто');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (3,1,1,1,'16029446621','Ничего не понимаю');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (4,null,1,1,'16029446621','Сколько времени ?');

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (1,1,1,'1577836800000',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (3,1,2,'1577836800000',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (5,1,3,'1577836800000',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (7,1,5,'1577836800000',-1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (9,1,4,'1577836800000',1);


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

INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUE (1,1,'1577836800000','Евгений','ekocaba2@mail.ru','12345678',null,null);


INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (1,1,'ACCEPTED',1,1,'1577836800000','Философия','Что я!',124532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (2,1,'ACCEPTED',1,1,'1577836800005','Физра','А что если!',1532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (3,1,'ACCEPTED',1,1,'1609459200030','Hello world','Hello world3!',12532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (4,1,'ACCEPTED',1,1,'1609459204000','Программа','Привет world4!',15332);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (5,1,'ACCEPTED',1,1,'1104537600030','Программа112вф','Hello world4!',154532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (6,0,'NEW',1,1,'1104537604000','Физик123а','Hello world6!',12453);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (7,1,'ACCEPTED',1,1,'1581552000000','Нет лайков','Опять же физика!',12422);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (8,1,'ACCEPTED',1,1,'1136073600000','Проверка времени','Физика!',422);