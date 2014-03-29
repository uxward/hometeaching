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