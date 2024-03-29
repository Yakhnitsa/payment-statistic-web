1. Подключение к серверу:
    `ssh yri@myapp.ukrtranslogistic.com.ua -p 822` +  ввод пароля
    
2. Создание и регистрация ключей ssh  
2.1. Создаем ключ ssh
    !! На локальной машине
     - генерируем ключи: `ssh-keygen -t rsa`
     - копируем публичные ключи на сервер: `ssh-copy-id yri@myapp.ukrtranslogistic.com.ua -p 822`
    !!! Profit, теперь доступ возможен без пароля по ssh ключу.    
       
2.2. Танцы с бубном:
  - Создаем ключи на локальном компьютере:
  `ssh-keygen` - производит генерацию ключей по пути ~/.ssh/id_rsa.pub
  
   - На сервере создаем директорию:
    `~/.ssh` и в ней создаем файл `touch ~/.ssh/authorized_keys`
    
   - Проверяем уровни доступа для директории:
    `ls -la ~/.ssh/authorized_keys`
   - Настраиваем необходимые уровни доступа для директорий:
   `chmod 700 ~/.ssh` - для папки ssh
   `chmod 600 ~/.ssh/authorized_keys` - для файла authorized_keys
   - Копируем файл ~/.ssh/id_rsa.pub на сервер
   `scp ~/.ssh/id_rsa.pub -p 822 yri@myapp.ukrtranslogistic.com.ua:~/.ssh/authorized_keys` !!! Не точно        
       
       
       
3. Установка и настройка приложений:
3.1 Установка java        
       -  Проверяем установленную версию, система сама предложит скрипты для запуска установки
       `java -verion`
        
      - Устанавливаем java 8    
         `sudo apt install openjdk-8-jre-headless`   
3.2. Настройка my-sql
    Проверка доступа
    `mysql -u root -p `
    Создание нового пользователя
    `MariaDB [(none)]>CREATE USER 'developer'@'localhost' IDENTIFIED BY '{password};'`
    Создание и настройка новой базы данных
    `MariaDB [(none)]> CREATE DATABASE db_utl_web_app;`
    `MariaDB [(none)]> GRANT ALL PRIVILEGES ON db_utl_web_app.* TO 'developer'@'localhost' WITH GRANT OPTION;`
4. Копирование файлов
    Проверяем копирование файлов:
    `scp target/test.txt -P822 yri@myapp.ukrtranslogistic.com.ua:~/test` - Копирование получилось )))
5. Меняем properties на девелоперские и простые.


    #database dependencies
    
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_utl_web_app?serverTimezone=UTC&useUnicode=true
    spring.datasource.username=developer
    spring.datasource.password=${MYSQL_PASS}
    
    server.port=8080

6. Настраиваем environment variables
6.1. Проверка настроек:
    `printenv`
6.2. Запись настроек, требуемых в приложении:

spring.datasource.password=${MYSQL_PASS} - `export MYSQL_PASS={MySQL pass}`
service.mail.password=${MAIL_PASSWORD} - `export MAIL_PASSWORD={mail_pass}`    

7. Пишем скрипт деплоя    
    Запись bash скриптов [инструкция](https://habr.com/ru/company/ruvds/blog/325522/)
    Убиение процесса [инструкция](https://pingvinus.ru/note/ps-kill-killall)
    Создаем файл скрипта `deploy/utl2_site_deploy.sh`
    Подробнее о происходящем:
    - Поиск приложения, которое использует порт
    
    - Ликвидация старого процесса java:
    `netstat -tulpn | grep ':8443'`  - поиск приложения, которое использует порт
    `sudo lsof -t -i:8443` - PID приложения, которое использует порт.
    `kill -9 `lsof -t -i:8443`` - ликвидация процесса, который использует порт
        
    - Запуск jar файла на сервере
     `java -Dfile.encoding=UTF8 -jar payment-statistic-web-0.0.1-SNAPSHOT.jar --mail_password={pass} --MYSQL_PASSWORD={pass}`
    - Запуск с логированием в файл 
      `nohup java -Dfile.encoding=UTF8 -jar payment-statistic-web-0.0.1-SNAPSHOT.jar > logfile.txt --mail_password={pass} --MYSQL_PASSWORD={pass} &`   
    
    Запуск скрипта:
    `./deploy/utl2_site_deploy.sh`
!!! Первые проблемы на сервере, flyway не работает с maria_db v 10.1, нужна версия 10.2
[Инструкция по обновлению](https://mariadb.com/kb/en/upgrading-from-mariadb-101-to-mariadb-102/)  
[решение](https://downloads.mariadb.org/mariadb/repositories/#distro=Ubuntu&distro_release=bionic--ubuntu_bionic&mirror=host-europe&version=10.4)  
Обновление легко происходит простой установкой поверх старой версии

8*. Запуск приложения с изменением пропертей:
`java -jar myproject.jar --spring.config.name=myproject`