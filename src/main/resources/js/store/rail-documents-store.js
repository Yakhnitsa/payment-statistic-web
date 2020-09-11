import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import UploadModule from './rail-docs-modules/upload-module'

export default new Vuex.Store({
    modules: {
        upload: UploadModule
    }
})