alter table person add visitingteacher bit;
alter table assignment add visitingteaching bit;
update person set visitingteacher = false;
update assignment set visitingteaching = false;
alter table companion add visitingteaching bit;
update companion set visitingteaching = false;
alter table personcompanion add visitingteaching bit;
update personcompanion set visitingteaching = false;
alter table visit add visitingteaching bit;
update visit set visitingteaching = false;
alter table visit drop index assignmentid;
alter table visit add unique index uniqueEachMonth (assignmentid, month, year, visitingteaching);
alter table visit add organizationid int;
update visit v, person p , personcompanion pc, companion c, assignment a
set v.organizationid = p.organizationid
where p.id = pc.personid and pc.companionid = c.id and a.companionid = c.id and v.assignmentid = a.id;
commit;



alter table assignment add organizationid int;
alter table companion add organizationid int;
alter table personcompanion add organizationid int;
update visit v, person p , personcompanion pc, companion c, assignment a
set a.organizationid = p.organizationid
where p.id = pc.personid and pc.companionid = c.id and a.companionid = c.id and v.assignmentid = a.id;
update visit v, person p , personcompanion pc, companion c, assignment a
set c.organizationid = p.organizationid
where p.id = pc.personid and pc.companionid = c.id and a.companionid = c.id and v.assignmentid = a.id;
update visit v, person p , personcompanion pc, companion c, assignment a
set pc.organizationid = p.organizationid
where p.id = pc.personid and pc.companionid = c.id and a.companionid = c.id and v.assignmentid = a.id;
commit;