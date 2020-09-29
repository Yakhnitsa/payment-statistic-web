import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

import DailyStatistic from '../pages/DailyStatistic.vue'
import Payments from '../pages/payments-pages/payment-list-page/PaymentsList.vue'
import PaymentDetailsPage from '../pages/PaymentDetailsPage.vue'
import Test from '../pages/Test.vue'
import ChartsPage from '../pages/payments-pages/charts-page/ChartsPage.vue'

export default new VueRouter({
    mode: 'history',
    // base: '/payments',
    routes:[
        {
            path: '/',
            component: ChartsPage
        },
        {
            path: '/daily-statistic',
            component: DailyStatistic
        },
        {
            path: '/payments',
            component: Payments
        },
        {
            path: '/payment-details',
            component: PaymentDetailsPage,
            name: 'payment-details',
            props: true
        },
        {
            path: '/test',
            component: Test,
            name:'test',
            props: true
        }
    ]
})