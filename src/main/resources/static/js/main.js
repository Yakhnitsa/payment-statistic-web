import Vue from 'vue'
import App from '../pages/App.vue'
import VueResouce from 'vue-resource'

Vue.use(VueResouce)

new Vue({
        el:'#app',
        render: a => a(App)
})