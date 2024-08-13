#유저생성
create user 'amatdang'@'%' identified by 'amatdang';
show databases;
# 데이터베이스 생성
create database myseatdangdb;
# 데이터베이스 권한 부여
grant all privileges on myseatdangdb.* to 'amatdang'@'%';
# 부여된 권한 보기
show grants for 'amatdang'@'%';