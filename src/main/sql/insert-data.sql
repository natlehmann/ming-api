INSERT INTO Role (id,name) VALUES (1,'administrator');
INSERT INTO Role (id,name) VALUES (2,'report');

INSERT INTO User (id,username,password,language,apiKey) VALUES (1,'admin','00f7e396bbfc69e41bb288cd7689952238894e1fbd8dfe248d6923ff66271cc8935e80c0c10a2f00', 'es', '${test.apiKey}');
INSERT INTO User (id,username,password,language,apiKey) VALUES (2,'${test.username}','334f49e13981647eafbf04ea0d37bdd931263bd82bdbe8cc938de471cdca1cf2d325581961996406', 'en', '${test.apiKey}');

INSERT INTO User_Role (user_id,role_id) VALUES (1,1);
INSERT INTO User_Role (user_id,role_id) VALUES (2,2);


