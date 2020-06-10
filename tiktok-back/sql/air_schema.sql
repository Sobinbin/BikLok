drop database if exists tiktok;
create database tiktok default character set utf8mb4 collate utf8mb4_unicode_ci;

grant all privileges on tiktok.* to 'air'@'%' identified by 'tiktok123456';
flush privileges;