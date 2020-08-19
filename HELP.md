# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

## Прикручивание Webpack к приложению:
- установка yarn https://yarnpkg.com/lang/ru/docs/install/#windows-stable
- установка с установленным NPM npm install -g yarn
- проверка версии yarn --version
- инициализация yarn init
- установка vue и библиотек- yarn add vue vue-resource
- установка девелоперских зависимостей: yarn add -D webpack webpack-cli webpack-dev-server babel-loader @babel/core @babel/preset-env vue-loader vue-template-compiler
- редактируем [файл](package.json):
    добавляем скрипт:
      "scripts": {
        "start": "webpack-dev-server"
      },
- добавляем в корень файл настройки Webpack [webpack.config.js](webpack.config.js)
    Настраиваем webpack:
        devServer: {
            contentBase: './dist',
            compress: true,
            port: 8000, - порт трансляции js кода
            allowedHosts: [
                'localhost:9000' - порт основного приложения, с которого разрешены запросы.
            ]
        },
- Редактируем файл index.html для приема javascript из сервера:
    <script src="http://localhost:8000/main.js"></script>
- Стартуем девелоперский сервер
    npm start    
    
- Устанавливаем axios для отправки запросов на сервер:
    yarn add axios    

## Установка и настройка Vue router
    - Установка через консоль
        yarn add vue-router   
    
    Настройка Vue-router router/router
    Делаем импорты страниц
        import DailyStatistic from 'pages/DailyStatistic.vue'
        import Payments from 'pages/PaymentsList.vue'
    Прописываем пути:
        routes:[
                {
                    path: '/',
                    component: Home
                },
                {
                    path: '/daily-statistic',
                    component: DailyStatistic
                },
                {
                    path: '/payments',
                    component: Payments
                }
            ]    
    Импорт роутера в main файле и в коренной экземпляр Vue
        import router from 'router/router'
        
        new Vue({
                el:'#app',
                router, - передача роутера в экземлпяр Vue
                render: a => a(App)
        })
## Настройка центрального хранилища данных:
    Установка Vuex
        yarn add vuex       
    Настройка хранилища store/store
    
    Импорт хранилища в корневой экземпляр Vue
        import store from 'store/store'
        new Vue({
                el:'#app',
                router,
                store, - Запиливание хранилища в приложение.
                render: a => a(App)
        });  
        
        
## Подготовка приложения к деплою
   
   - Вытягиваем файлы из static в корень приложения (в папке static будет только собранный main.js)
   
   - Устанавливаем приложение для зборки
    
    yarn add -D webpack-merge
   
   -  Разделяем файл webpack.config.js на несколько конфигураций dev и prod
        создаем файлы:
        webpack.common.js - общая конфигурация                   
        webpack.dev.js - конфигурация для деплоя                  
        webpack.prod.js - конфигурация для продакшена   
        
   - Устанавливаем сборщик мусора и добавляем в prod конфигурацию
    
    yarn add -D clean-webpack-plugin   
    
    const { CleanWebpackPlugin } = require('clean-webpack-plugin'); 
       
       plugins: [
           new CleanWebpackPlugin()
       ],      
    
   - Настраиваем файл сборки package.json
        "scripts": {
          "start": "webpack-dev-server --open --config webpack.dev.js",
          "build": "webpack --config webpack.prod.js"
        },        
        
        
## Добавляем поддержку графиков:
   - Установка [vue-chartjs](https://vue-chartjs.org/ru/guide/#%D1%81%D0%BE%D0%B7%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D0%B0%D1%88%D0%B5%D0%B3%D0%BE-%D0%BF%D0%B5%D1%80%D0%B2%D0%BE%D0%B3%D0%BE-%D0%B3%D1%80%D0%B0%D1%84%D0%B8%D0%BA%D0%B0)       
   Установка посредством yarn: `yarn add vue-chartjs chart.js`    
   
   - Создаем линейный график:
   
   
       <script>
           import {Line} from 'vue-chartjs' - Импорт компонента из библиотеки
       
           export default {
               extends: Line,
               name: "DailyExpensesChart"
           }
       </script>  
       
   ### Настройка форматирования в графике:
   В options добавляем следующую настройку:
   
   
    options{
    ....
   
        tooltips: {
            callbacks: {
                label(tooltipItem, data) {
                   // Get the dataset label.
                   const label = data.datasets[tooltipItem.datasetIndex].label;
           
                   // Format the y-axis value.
                   const value = tooltipItem.yLabel;
           
                   return `${label}: ${value}`;
                }
            }        
        }
    }   
    
   - подключаем модуль для форматирования чисел
    `$ npm install numeral`
    Импортируем в приложение
    `import numeral from 'numeral'`
    Форматируем где это нужно:
    var string = numeral(1000).format('0,0');
    
    
## Настраиваем spring security
   Добавляем зависимости:
           
    <!--security configuration-->
   
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
   Настраиваем пользователя User implements UserDetails и Role implements GrantedAuthority
   
   Настраиваем EncriptionConfig, создаем конфигурацию с одним бином:
       
       @Bean
       public PasswordEncoder getPasswordEncoder(){
           return new BCryptPasswordEncoder(8);
       } 
   Настраиваем конфигурацию SpringSecurity WebSecurityConfig
    ....
   Настраиваем хранилище сущностей и кодировку паролей
    
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);
        } 
        
   Требуется UserService, и UserReop добавляем
   
   Добавляем страницу логирования:
   //templates/users/login
   И настраиваем WebMvcConfig для работы с данными страницами:
   
        @Override
        public void addViewControllers(ViewControllerRegistry registry){
            registry.addViewController("/login").setViewName("users/login");
        }
   
   Добавляем нового пользователя прямиком в БД:
   Ставим active-true
   Пароль: шифруем через [шифровщик](https://bcrypt-generator.com/)  
   Все, теперь эта хрень должна работать.
   
## Включаем csrf protection в post запросах:
   В шаблоне thymeleaf включаем csrf token
   
    <meta th:name="_csrf" th:content="${_csrf.token}"/>
    <meta th:name="_csrf_header" th:content="${_csrf.headerName}"/>  
   Получаем данные на стороне клиента
     `const CSRF_TOKEN = $("meta[name='_csrf']").attr("content")`
   Прикладываем данные к форме 
    
    formData.append("_csrf", CSRF_TOKEN);
           axios.post('/api/upload-multiple',
               formData, {
                   headers: {
                       'Content-Type': 'multipart/form-data'
                   }
   
               }
           )
   Либо вкладываем в заголовок:
        
        axios({
            method:.....
            url: ....
            data: .....
            headers:{
                'X-CSRF-Token': csrfToken
            }       
            
            
## Переход на https [инструкция](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/)     
- Создаем сертификат:
    `keytool -genkeypair -alias payment-statistic -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore D:\payment-statistic-keystore.p12 -validity 3650 -storepass password`
- Проверка сертификата:
    `keytool -list -v -storetype pkcs12 -keystore D:\payment-statistic-keystore.p12`
- Добавляем настройки 

 
        #https properties
        server.ssl.key-store=classpath:payment-statistic-keystore.p12
        server.ssl.key-store-password=password
        server.ssl.key-store-type=pkcs12
        server.ssl.key-alias=payment-statistic
        server.ssl.key-password=password
    
- Настраиваем WebSecurityConfig на https:

    
        @Override
            protected void configure(HttpSecurity http){
            ...    
        
            /*
            * Enable https
            */
            http
                    .requiresChannel()
                    .anyRequest()
                    .requiresSecure();
- Настраиваем перенаправление на https:
    
        
        @Configuration
        public class HttpsRedirectConfig {
        
            @Value("${server.port}")
            private Integer applicationPort;
        
            @Bean
            public ServletWebServerFactory servletContainer() {
                TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
                    @Override
                    protected void postProcessContext(Context context) {
                        SecurityConstraint securityConstraint = new SecurityConstraint();
                        securityConstraint.setUserConstraint("CONFIDENTIAL");
                        SecurityCollection collection = new SecurityCollection();
                        collection.addPattern("/*");
                        securityConstraint.addCollection(collection);
                        context.addConstraint(securityConstraint);
                    }
                };
                tomcat.addAdditionalTomcatConnectors(getHttpConnector());
                return tomcat;
            }
        
            private Connector getHttpConnector() {
                Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
                connector.setScheme("http");
                connector.setPort(8080);
                connector.setSecure(false);
                connector.setRedirectPort(applicationPort);
                return connector;
            }
        }  
        
        
- Создаем сертификат пользователя:
    `keytool -export -keystore D:\payment-statistic-keystore.p12 -alias payment-statistic -file D:\myCertificate.crt`      
    
    
    
## Загрузка перечней из почты в Temp DB

Подключаем зависимости:


            <!-- https://mvnrepository.com/artifact/javax.mail/mail -->
            <dependency>
                <groupId>javax.mail</groupId>
                <artifactId>mail</artifactId>
                <version>1.4.7</version>
            </dependency>    
            
Создаем сервис:      
    MailService и его имплементацию
    MailServiceImpl 
                           
                           
!! Конвертация InputStream в MultipartFile

## Настраиваем security для методов
Меняем WebSecurityConfig:
	`@EnableGlobalMethodSecurity(
        prePostEnabled = true, 
        securedEnabled = true, 
        jsr250Enabled = true)`
        
        
## Деплой на сервер [инструкция](/help/SERVER_DEPLOY_HELP.md)

## Настройка миграции БД
- Именение настроек application-dev.properties


    spring.jpa.show-sql=true
    spring.jpa.generate-ddl=true
    spring.jpa.hibernate.ddl-auto=create   
- Меняем базу данных, чтобы старая сохранилась
    `spring.datasource.url=jdbc:h2:file:./src/main/resources/database/test_db`    
- Запускаем приложение с девелоперскими пропертями.
 - VM arguments:
    `-Dspring.profiles.active=dev`    
 - Либо прописываем режим в конфигурации запуска intellij -> active profiles
 
 - Выделяем запросы Hibernate по созданию базы данных:
 
 
     Hibernate: create sequence hibernate_sequence start with 1 increment by 1
     Hibernate: create table app_user (id bigint not null, active boolean not null, email varchar(255), password varchar(255), username varchar(255), primary key (id))
     Hibernate: create table payment_details (id bigint not null, additional_payment bigint not null, date date, document_number varchar(255), income_type integer, payment bigint not null, payment_code varchar(255), payment_description varchar(255), station_code integer not null, station_name varchar(255), tax_payment bigint not null, total_payment bigint not null, type varchar(255), number integer, payer_code integer, primary key (id))
     Hibernate: create table payment_list (number integer not null, payer_code integer not null, created_date timestamp, last_modified_date timestamp, backup_file_path varchar(255), closing_balance bigint not null, contract_number varchar(255), date date, opening_balance bigint not null, payer_name varchar(255), payment_taxes bigint not null, payment_vs_taxes bigint not null, payments bigint not null, tax_code integer not null, test_passed boolean not null, user_id bigint, last_modified_by bigint, primary key (number, payer_code))
     Hibernate: create table user_role (user_id bigint not null, roles varchar(255))
     Hibernate: alter table payment_details add constraint FKrunu207hvbresx9w7aso0etn foreign key (number, payer_code) references payment_list
     Hibernate: alter table payment_list add constraint FKgjxh7pxd1uek9utf16i68s7la foreign key (user_id) references app_user
     Hibernate: alter table payment_list add constraint FK8jly0hr6c1eqbx6mbqjyjtee4 foreign key (last_modified_by) references app_user
     Hibernate: alter table user_role add constraint FKg7fr1r7o0fkk41nfhnjdyqn7b foreign key (user_id) references app_user   

- Подключаем flyway к pom-нику [ссылочка](https://flywaydb.org/documentation/maven/):
        
    
    <!-- https://mvnrepository.com/artifact/org.flywaydb/flyway-core -->
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
        <version>6.5.5</version>
    </dependency>
- Настраиваем миграции:
    - Создаем директорию миграции `resouces/db.migration` 
    - Создаем файлы миграции: `V{numb_version}__{explanation}` 
        - пример `V1__init_db.sql`             