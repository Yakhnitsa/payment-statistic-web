import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import UploadModule from './rail-docs-modules/upload-module'
import RailroadDocumentsModule from './rail-docs-modules/rail-docs-module'
import CommonModule from './common-module'
import CertificatesModule from './rail-docs-modules/certs-module'

export default new Vuex.Store({
    modules: {
        uploadStore: UploadModule,
        commonStore: CommonModule,
        railDocsStore: RailroadDocumentsModule,
        certStore: CertificatesModule,
    },
    getters:{

    }

})