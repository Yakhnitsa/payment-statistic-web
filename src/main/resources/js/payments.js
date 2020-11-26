import Vue from 'vue'
import App from './pages/payments-pages/PaymentsRootPage.vue'
import VueResouce from 'vue-resource'
import router from 'router/payments-router'
import store from 'store/payments-store'
import 'shared/filters/filters'
import 'shared/services/vue-toast'

import 'babel-polyfill'

Vue.use(VueResouce);

new Vue({
        el:'#app',
        router,
        store,
        render: a => a(App)
});