import Vue from 'vue'
import MainPage from './pages/RailDocumentsMainPage.vue'


new Vue({
    el:'#documents-page',
    render: a => a(MainPage)
});