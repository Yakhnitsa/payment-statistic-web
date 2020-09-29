import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);

import RootPage from '../pages/rail-docs-pages/RailDocumensPage.vue'
import Certificates from '../pages/rail-docs-pages/QualityCertsPage.vue'
import LoadingPage from '../pages/rail-docs-pages/loading-page/LoadingPage.vue'

const rootPath = '';

export default new VueRouter({
    mode: 'history',
    base: '/railroad-documents',
    routes:[
        {
            path: '/',
            component: LoadingPage
        },
        {
            path: '/certificates',
            component: Certificates
        },
        {
            path: '/upload',
            component: LoadingPage
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