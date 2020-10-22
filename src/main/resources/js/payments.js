import Vue from 'vue'
import App from './pages/PaymentsRootPage.vue'
import VueResouce from 'vue-resource'
import router from 'router/payments-router'
import store from 'store/payments-store'
import 'shared/filters/filters'

import 'babel-polyfill'

Vue.use(VueResouce);

new Vue({
        el:'#app',
        router,
        store,
        render: a => a(App)
});