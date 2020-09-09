import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import RootPage from '../pages/rail-docs-pages/RailDocumensList.vue'
import Certificates from '../pages/rail-docs-pages/QualityCertsList.vue'

const rootPath = '';

export default new VueRouter({
    mode: 'history',
    base: '/railroad-documents',
    routes:[
        {
            path: '/',
            component: RootPage
        },
        {
            path: '/certificates',
            component: Certificates
        }
        // {
        //     path: '/daily-statistic',
        //     component: DailyStatistic
        // },
        // {
        //     path: '/payments',
        //     component: Payments
        // },
        // {
        //     path: '/payment-details',
        //     component: PaymentDetailsPage,
        //     name: 'payment-details',
        //     props: true
        // },
        // {
        //     path: '/test',
        //     component: Test,
        //     name:'test',
        //     props: true
        // }
    ]
})