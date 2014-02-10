webapp for home and visiting teaching
============

###### user functionality
- login with credentials
- edit login credentials
- view and edit own family information (contact info, family members & contact info)
- view own companionship and home teaching families
- record and edit visits for families by month
- provide feedback

###### admin functionality (in addition to user functionality)
- view, add, and edit all families for assigned organization(s)
- view, add, and edit all companionships for assigned organization(s)
- view, add, and edit all users for assigned organization(s) - note:  never have access to user passwords, which are salted and hashed in db
- view consolidated visit history for specified number of months
- view dashboards
- view feedback

###### languages
- java
- html5
- css3
- javascript

###### technologies/frameworks/libraries
- spring & spring mvc for the overall framework
- hibernate & mysema querydsl for querying
- apache commons for lots of stuff
- jquery for front end manipulation/validation/etc
- bootstrap 3 for responsive ui
- datatables for ui tables
- d3.js for data visualization
- bcrypt for security

###### setup
- if using mysql db, use the tables.sql file under the db folder for the necessary tables
- regardless of db type, update the app-local and app-prod .properties files with your db connection info
