import railDocumentsApi from "../../api/rail-docs-api/railroadDocumentsApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({
        documents:[],
        currentPage:0,
        totalPages:0,
        totalElements:0,
        requestParams:{
            stationFromCode:'',
            stationToCode:'',
            cargoSenderCode:'',
            cargoReceiverCode:'',
            tarifPayerCode:'',
            dateFrom:'',
            dateUntil:'',
            docNumber:'',
            vagonNumber:'',
            cargoCode:'',
        }
    }),
    getters: {
        documents: state => state.documents,
        currentPage: state => state.currentPage,
        totalPages: state => state.totalPages,
        totalElements: state => state.totalElements,
        storedRequestParams: state => state.requestParams,
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
        clearRequestParams(state){

        },

        setRequestParamsMutation(state, params){

        }

    },
    actions: {
        async fetchRailroadDocumentsAction({commit,state,getters}) {
            try{
                const response = await railDocumentsApi.getRailroadDocuments(state.requestParams);
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