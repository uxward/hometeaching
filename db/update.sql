##Family Note
create table familynote (
	id int auto_increment primary key
	,familyid int
	,note varchar(500)
	,visiblerole varchar(50)
	,userid int
	,created datetime
	,updated datetime
);