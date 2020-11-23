import railDocumentsApi from "../../api/rail-docs-api/railroadDocumentsApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({
        documents: [],
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        requestParams: {}
    }),
    getters: {
        documents: state => state.documents,
        currentPage: state => state.currentPage,
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

        setRequestParamsMutation(state, params) {
            state.requestParams = params;
        }

    },
    actions: {
        async fetchRailroadDocumentsAction({commit, state}) {
            try {
                state.requestParams.currentPage = state.currentPage;
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