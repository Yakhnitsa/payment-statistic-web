import railDocumentsApi from "../../api/rail-docs-api/railroadDocumentsApi";
import messageManager from "../../util/messageManager";

export default {
    namespaced: true,
    state: () => ({
        documents:[],
        currentPage:0,
        totalPages:0,
        totalElements:0,
        requestParams:{

        }
    }),
    getters: {
        documents: state => state.documents,
        currentPage: state => state.currentPage,
        totalPages: state => state.totalPages,
        totalResults: state => state.totalResults,
    },
    mutations: {
        setDocumentsMutation(state, documents){
            state.documents = documents;
        },
        setCurrentPageMutation(state, page){
            state.currentPage = page;
        },
        setTotalPagesMutation(state, pages){
            state.totalPages = pages;
        },
        setTotalElementsMutation(state, elements){
            state.totalElements = elements;
        },

    },
    actions: {
        async fetchRailroadDocumentsAction({commit,state,getters},params) {
            try{
                const response = await railDocumentsApi.getRailroadDocuments(params);
                const data =  await response.data;
                commit('setDocumentsMutation',data.content);
                // commit('setCurrentPageMutation',data.content);
                commit('setTotalPagesMutation',data.totalPages);
                commit('setTotalElementsMutation',data.totalElements)
            }catch(error){
                if(error.response){
                    messageManager.showOnLoadException(error);
                }
            }

        },
    },

}