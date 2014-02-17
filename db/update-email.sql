alter table family add familymoved bit;
alter table family add recordsmoved bit;

update family set familymoved = false where familymoved is null;
update family set recordsmoved = false where recordsmoved is null;
commit;