import Vue from 'vue'
import App from './pages/App.vue'
import VueResouce from 'vue-resource'
import router from 'router/router'
import store from 'store/store'
import 'util/filters'

import 'babel-polyfill'

Vue.use(VueResouce)
import VueSimpleContextMenu from 'vue-simple-context-menu'
Vue.component('vue-simple-context-menu', VueSimpleContextMenu)

new Vue({
        el:'#app',
        router,
        store,
        render: a => a(App)
});