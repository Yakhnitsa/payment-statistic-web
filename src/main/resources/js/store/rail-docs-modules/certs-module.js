import certificatesApi from "../../api/rail-docs-api/certificatesApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({
        documents: [],
        changes:[],
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        requestParams: {
            currentPage: 0,
            stationFromCode: '',
            stationToCode: '',
            cargoSenderCode: '',
            cargoReceiverCode: '',
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
        changesList: state => state.changes
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
        addChangesMutation(state, { vagonId, changes}){
            const index = state.changes
                .findIndex(item =>  item.vagonId === vagonId);
            index >= 0 ? state.changes.splice(index,1) :
                state.changes.push({vagonId, changes});

        },
        updateChangesInDocumentsMutation(state,changes){
            changes.forEach(item =>{
                state.documents.forEach(document =>{
                    updateVagonInfoInDocument(document,item)
                });
            });
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
            try {
                const response = await certificatesApi.getCertificatesRegister(state.requestParams);
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

        async uploadChangesToServerAction({commit,state}) {
            try {
                const response = await certificatesApi.uploadChangesToServer(state.changes);
                const data = await response.data;
                state.changes = [];
                commit('updateChangesInDocumentsMutation',data);
            } catch (error) {
                if (error.response) {
                    messageManager.showOnLoadException(error);
                }
            }
        }
    },



}
function updateVagonInfoInDocument(railDoc,changes){
    let index = railDoc.vagonList.findIndex(vagon => vagon.id === changes.vagon.id);
    if(index !== -1){
        railDoc.vagonList[index].vagonInfo = changes;
        console.log(railDoc.vagonList[index]);
    }

}