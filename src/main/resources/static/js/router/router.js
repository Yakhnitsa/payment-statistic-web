import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)
import DailyStatistic from 'pages/DailyStatistic.vue'
import Payments from 'pages/PaymentsList.vue'

 const Home = { template: '<div>Home page</div>' }

export default new VueRouter({
    mode: 'history',
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

})