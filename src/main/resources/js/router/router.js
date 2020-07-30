import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

import DailyStatistic from '../pages/DailyStatistic.vue'
import Payments from '../pages/PaymentsList.vue'
import PaymentDetailsPage from '../pages/PaymentDetailsPage.vue'
import Test from '../pages/Test.vue'
import ChartsPage from '../pages/ChartsPage.vue'

export default new VueRouter({
    mode: 'history',
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
            component: PaymentDetailsPage
        },
        {
            path: '/test',
            component: Test
        }
    ]
})