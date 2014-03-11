alter table person add user bit;
update person set user = hometeacher;
commit;