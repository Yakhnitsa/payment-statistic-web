import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter);

import DailyStatistic from '../pages/payments-pages/daily-statistic-page/DailyStatistic.vue'
import Payments from '../pages/payments-pages/payment-list-page/PaymentsList.vue'
import PaymentDetailsPage from '../pages/payments-pages/payment-details-page/PaymentDetailsPage.vue'
import Test from '../pages/Test.vue'
import ChartsPage from '../pages/payments-pages/charts-page/ChartsPage.vue'

export default new VueRouter({
    mode: 'history',
    base: '/payment-statistic',
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