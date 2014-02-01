##Insert users and role

INSERT INTO users (personid, username, password, enabled)
VALUES (50, 'guest', 'guest', true);
 
INSERT INTO role (userid, role)
VALUES (1, 'hometeacher');

commit;