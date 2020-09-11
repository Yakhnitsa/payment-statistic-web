import Vue from 'vue'
import router from 'router/rail-documents-router'
import store from 'store/rail-documents-store'

import MainPage from './pages/RailDocumentsRootPage.vue'

import 'babel-polyfill'

new Vue({
    el:'#documents-page',
    render: a => a(MainPage),
    router,
    store
});