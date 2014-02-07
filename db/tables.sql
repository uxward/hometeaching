##Family

create table family (
	id int auto_increment primary key
	,familyname varchar(30)
	,address varchar(100)
	,phonenumber varchar(20)
	,familystatusid int
	,organizationid int
	,hometeach bit
	,created datetime
	,updated datetime
);

##Person

create table person(
	id int auto_increment primary key
	,firstname varchar(30)
	,age int
	,female bit
	,headofhousehold bit
	,familyid int
	,organizationid int
	,phonenumber varchar(20)
	,email varchar(50)
	,hometeacher bit
	,created datetime
	,updated datetime
	,foreign key (familyid) 
        references family(id)
        on delete cascade
);

##Companion

create table companion(
	id int auto_increment primary key
	,active bit
	,created datetime
	,updated datetime
);

##Person Companion

create table personcompanion(
	id int auto_increment primary key
	,personid int
	,companionid int
	,active bit
	,created datetime
	,updated datetime
	,foreign key (personid) 
        references person(id)
        on delete cascade
	,foreign key (companionid) 
        references companion(id)
        on delete cascade
);

##Assignment

create table assignment(
	id int auto_increment primary key
	,companionid int
	,familyid int
	,active bit
	,created datetime
	,updated datetime
	,foreign key (companionid) 
        references companion(id)
        on delete cascade
	,foreign key (familyid) 
        references family(id)
        on delete cascade
);

##Visit

create table visit (
	id int auto_increment primary key
	,assignmentid int
	,familyid int
	,visited bit
	,notes varchar(400)
	,month int
	,year int
	,visitdate datetime
	,created datetime
	,updated datetime
	,foreign key (assignmentid) 
        references assignment(id)
        on delete cascade
    unique (assignmentid, month, year)
);

##Users
create table users (
  id int auto_increment primary key,
  personid int,
  username varchar(50),
  password varchar(100),
  enabled bit,
  reset bit,
  lastlogin datetime,
  email varchar(50)
  unique (username)
);


##User Role
create table userrole (
	id int auto_increment primary key,
    userid int,
    role varchar(50)
);

##Status
create table status (
	id int auto_increment primary key,
    status varchar(50)
);

##Feedback
create table feedback(
	id int auto_increment primary key
	,resolved bit
	,userid int
	,priorityid int
	,notes varchar(500)
	,created datetime
	,updated datetime
);

##Organization
create table organization (
	id int auto_increment primary key,
    organization varchar(50)
);

##User Organization
create table userorganization(
	id int auto_increment primary key,
	userid int,
	organizationid int
);

##Family Organization
create table familyorganization(
	id int auto_increment primary key,
	familyid int,
	organizationid int
);

commit;