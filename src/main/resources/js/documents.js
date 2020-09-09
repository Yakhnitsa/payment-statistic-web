import Vue from 'vue'
import MainPage from './pages/RailDocumentsRootPage.vue'

import router from 'router/rail-documents-router'

new Vue({
    el:'#documents-page',
    render: a => a(MainPage),
    router
});