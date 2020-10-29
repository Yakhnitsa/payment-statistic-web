import railDocumentsApi from "../../api/rail-docs-api/railroadDocumentsApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({
        documents: [],
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        requestParams: {
            currentPage: 0,
            stationFromCode: '',
            stationToCode: '',
            cargoSenderCode: '',
            cargoReceiverCode: '',
            tarifPayerCode: '',
            dateFrom: '',
            dateUntil: '',
            docNumber: '',
            vagonNumber: '',
            cargoCode: '',
        }
    }),
    getters: {
        documents: state => state.documents,
        currentPage: state => state.requestParams.currentPage,
        totalPages: state => state.totalPages,
        totalElements: state => state.totalElements,
        storedRequestParams: state => state.requestParams,
    },
    mutations: {
        setDocumentsMutation(state, documents) {
            state.documents = documents;
        },
        setCurrentPageMutation(state, page) {
            state.requestParams.currentPage = page;
        },
        setTotalPagesMutation(state, pages) {
            state.totalPages = pages;
        },
        setTotalElementsMutation(state, elements) {
            state.totalElements = elements;
        },
        clearRequestParams(state) {
            state.requestParams.currentPage = 0;
            state.requestParams.stationFromCode = '';
            state.requestParams.stationToCode = '';
            state.requestParams.cargoSenderCode = '';
            state.requestParams.cargoReceiverCode = '';
            state.requestParams.tarifPayerCode = '';
            state.requestParams.dateFrom = '';
            state.requestParams.dateUntil = '';
            state.requestParams.docNumber = '';
            state.requestParams.vagonNumber = '';
            state.requestParams.cargoCode = '';
        },

        setRequestParamsMutation(state, params) {
            console.log(params);
            state.requestParams.currentPage = params.currentPage;
            state.requestParams.stationFromCode = params.stationFromCode;
            state.requestParams.stationToCode = params.stationToCode;
            state.requestParams.cargoSenderCode = params.cargoSenderCode;
            state.requestParams.cargoReceiverCode = params.cargoReceiverCode;
            state.requestParams.tarifPayerCode = params.tarifPayerCode;
            state.requestParams.dateFrom = params.dateFrom;
            state.requestParams.dateUntil = params.dateUntil;
            state.requestParams.docNumber = params.docNumber;
            state.requestParams.vagonNumber = params.vagonNumber;
            state.requestParams.cargoCode = params.cargoCode
        }

    },
    actions: {
        async fetchRailroadDocumentsAction({commit, state}) {
            console.log(state.requestParams);
            try {
                const response = await railDocumentsApi.getRailroadDocuments(state.requestParams);
                const data = await response.data;
                commit('setDocumentsMutation', data.content);
                commit('setTotalPagesMutation', data.totalPages);
                commit('setTotalElementsMutation', data.totalElements)
            } catch (error) {
                if (error.response) {
                    messageManager.showOnLoadException(error);
                }
            }
        },
    },

}