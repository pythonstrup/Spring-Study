FROM mysql:8.0.35

ENV MYSQL_ROOT_PASSWORD=qwer1234!!
ENV MYSQL_DATABASE=shop
ENV MYSQL_USER=ddd
ENV MYSQL_PASSWORD=qwer1234!!

EXPOSE 3306
# 이미지 빌드: docker build -t mysql-image -f ./mysql.dockerfile .
# 첫 실행(컨테이너 생성): docker run -d --name 'ddd-mysql' -p 3308:3306 mysql-image
# 컨네이너 생성 이후 실행: docker start ddd-mysql

# 이후 가이드: https://velog.io/@dab2in/Docker-Mysql-Intellij
# 도커 컨테이너 터미널 접속
# mysql -u root -p
# mysql> create user 'ddd'@'%' identified by 'qwer1234!!';
# mysql> grant all privileges on *.* to 'ddd'@'%';
# mysql> flush privileges;