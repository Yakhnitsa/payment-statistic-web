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
    `MariaDB [(none)]> GRANT ALL PRIVILEGES db_utl_web_app.* TO 'developer'@'localhost' WITH GRANT OPTION;`
4. Копирование файлов
    Проверяем копирование файлов:
    `scp target/test.txt -P822 yri@myapp.ukrtranslogistic.com.ua:~/test` - Копирование получилось )))
5. Меняем properties на девелоперские и простые.


    #database dependencies
    
    spring.jpa.hibernate.ddl-auto=update
    spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_utl_web_app
    spring.datasource.username=developer
    spring.datasource.password=${MYSQL_PASS}
    
    server.port=8080

6. Настраиваем environment variables
6.1. Проверка настроек:
    `printenv`
6.2. Запись настроек, требуемых в приложении:

spring.datasource.password=${MYSQL_PASS} - 
service.mail.password=${MAIL_PASSWORD} -     

7. Пишем скрипт деплоя    