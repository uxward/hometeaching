a java webapp for tracking home and visiting teaching
============

user functionality:
1.  login with credentials
2.  edit login credentials
3.  view and edit own family information (contact info, family members & contact info)
4.  view own companionship and home teaching families
5.  record and edit visits for families by month
6.  provide feedback

admin functionality (in addition to user functionality)
1.  view, add, and edit all families for assigned organization(s)
2.  view, add, and edit all companionships for assigned organization(s)
3.  view, add, and edit all users for assigned organization(s) - note:  never have access to user passwords, which are salted and hashed in db
3.  view consolidated visit history for specified number of months
4.  view dashboards
5.  view feedback

technologies/frameworks:
1.  spring & spring mvc for the overall framework
2.  hibernate & mysema querydsl for querying
3.  apache commons for lots of stuff
4.  bootstrap 3 for responsive ui
5.  datatables for ui tables
6.  d3.js for data visualization
7.  bcrypt for security

setup:
1. if using mysql db, use the tables.sql file under the db folder for the necessary tables
2. regardless of db type, update the app-local and app-prod .properties files with your db connection info
