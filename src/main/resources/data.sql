INSERT INTO users (id,is_moderator,reg_time,name,email,password,code,photo) VALUES (1,1,'1610576432','Евгений','ekocaba2@mail.ru','$2a$10$aUhNBrFqhYiyV5XjVFRh1uwQGX4XGa0w7UyTaQbRM2HeKz2cL5nZi',null,null);

INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (1,1,'ACCEPTED',1,1,'1610576432','Вычислительная модель Пролог-программы','<p><font style="vertical-align: inherit;"><font style="vertical-align: inherit;">Вопрос был отредактирован, чтобы отразить истинную проблему. </font></font></p> <pre class="lang-java s-code-block hljs"><code><span class="hljs-keyword">import</span> org.jsoup.parser.Parser;<font></font><font><p>New answer;The hardspace, ie. entity &nbsp; (Unicode character NO-BREAK SPACE U+00A0 ) can in Java be represented by the character <code>\u00a0,</code> thus code becomes, where <code>str</code> is the string gotten from the <code>text()</code> method</p></br></font>String str1 = Parser.unescapeEntities(<span class="hljs-string">"last week,&amp;nbsp;Ovokerie Ogbeta"</span>, <span class="hljs-keyword">false</span>);<font></br></font>String str2 = Parser.unescapeEntities(<span class="hljs-string">"Entered&amp;nbsp;&amp;raquo; Here"</span>, <span class="hljs-keyword">false</span>);<font></br></font>System.out.println(str1 + <span class="hljs-string">" "</span> + str2);</code></pre>',124532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (2,1,'ACCEPTED',1,1,'1610576432','Как удалить файл в Java','<p>Нет глупостей, просто выполните<strong>File.delete ()</strong>, чтобы удалить файл, он вернет логическое значение, чтобы указать состояние операции удаления; истина, если файл удален; ложь, если не удалось. Последний выбор File.deleteOnExit() - это, конечно, метод ввода-вывода Java, который мы пытаемся избежать. Завершение работы - это то, что происходит за кулисами, когда вы вызываете вышеупомянутый метод. Но параметр DELETE_ON_CLOSE - это чистый Java NIO.Вместо того, чтобы удалять произвольные файлы, Java NIO предполагает, что вас интересует только удаление файлов, которые вы открываете. Поэтому методы, которые создают новый поток, такой как Files.newOutputStream(), могут, возможно, взять несколько OpenOptions, где вы можете ввести StandardOpenOption.DELETE_ON_CLOSE. Что это значит, это удалить файл, как только поток будет закрыт (либо вызовом .close(), либо выходом JVM).</p> ',1532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (3,1,'ACCEPTED',1,1,'1105644434','Hello world','Hello world3!',12532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (4,1,'ACCEPTED',1,1,'1610576433','Программа','Привет world4!',15332);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (5,1,'ACCEPTED',1,1,'1610576432','Программа112вф','Hello world4!',154532);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (6,0,'NEW',null,1,'1105644434','Физик123а','Hello world6!',12453);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (7,1,'NEW',null,1,'1610576433','Нет лайков','Опять же физика!',12422);
INSERT INTO posts (id,is_active,moderation_status,moderator_id,user_id,time,title,text,view_count) VALUES (8,1,'NEW',1,1,'1105644434','Проверка времени','Физика!',422);

INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (1,null,1,1,'16029446621','Вау');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (2,null,1,1,'16029446621','Круто');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (3,1,1,1,'16029446621','Ничего не понимаю');
INSERT INTO post_comments (id,parent_id,post_id,user_id,time,text) VALUES (4,null,1,1,'16029446621','Сколько времени ?');

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (1,1,1,'1610576432',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (3,1,2,'1610576432',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (5,1,3,'1610576432',1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (7,1,5,'1610576432',-1);

INSERT INTO post_voters (id,user_id,post_id,time,value) VALUES (9,1,4,'1610576432',1);

INSERT INTO tags (id,name) VALUES (1,'КТО Я');
INSERT INTO tags (id,name) VALUES (2,'ПРОГРАММА');
INSERT INTO tags (id,name) VALUES (3,'ТЭГ3');
INSERT INTO tags (id,name) VALUES (4,'ТЭГ4');
INSERT INTO tags (id,name) VALUES (5,'ПРОГРАММА НОВАЯ');
INSERT INTO tags (id,name) VALUES (6,'ТЭГ6');

INSERT INTO tag_2_post (post_id,tag_id) VALUES (1,2);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (1,3);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (1,4);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (3,4);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (4,1);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (4,2);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (4,5);
INSERT INTO tag_2_post (post_id,tag_id) VALUES (2,5);

