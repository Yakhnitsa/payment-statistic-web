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