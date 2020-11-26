import railDocumentsApi from "../../api/rail-docs-api/railroadDocumentsApi";
import messageManager from "../../shared/services/messageManager";
import downloadApi from "../../api/rail-docs-api/railDocsDownloadApi";
import paymentListApi from "../../api/paymentListApi";

export default {
    namespaced: true,
    state: () => ({
        documents: [],
        selectedDocuments:[],
        filteredDocuments:[],
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        requestParams: {}
    }),
    getters: {
        documents: state => state.documents,
        selectedDocuments: state => state.selectedDocuments,
        filteredDocuments: state => state.filteredDocuments,
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
            state.currentPage = page;
        },
        setTotalPagesMutation(state, pages) {
            state.totalPages = pages;
        },
        setTotalElementsMutation(state, elements) {
            state.totalElements = elements;
        },

        setRequestParamsMutation(state, params) {
            state.requestParams = params;
        },

        setSelectedDocumentsMutation(state, params){
            state.selectedDocuments = params;
        },

        setFilteredDocumentsMutation(state,params){
            state.filteredDocuments = params
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

        async downloadSelectedDocumentsArchiveAction({state,dispatch}){
            const docIds = mapDocsToDocsId(state.selectedDocuments);
            dispatch('downloadDocsArchiveAction',docIds);
        },
        async downloadFilteredDocumentsArchiveAction({state,dispatch}){
            const docIds = mapDocsToDocsId(state.filteredDocuments);
            dispatch('downloadDocsArchiveAction',docIds);
        },

        async downloadDocsArchiveAction({state}, documents){
            downloadApi.downloadArchive(documents).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let achName = 'archive.zip';
                const disposition = response.headers['content-disposition'];
                // if (disposition && disposition.indexOf('attachment') !== -1) {
                //     const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                //     let matches = filenameRegex.exec(disposition);
                //     if (matches != null && matches[1]) achName = matches[1].replace(/['"]/g, '');
                // }

                link.href = url;
                link.setAttribute('download', achName);
                document.body.appendChild(link);
                link.click();
            });
        },

        async downloadXmlFileAction({commit,state,getters},railDoc) {
            downloadApi.downloadSingleFile('xml',railDoc).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let fileName = getFilenameFromDoc(railDoc,'.xml');
                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
            });
        },

        async downloadPdfFileAction({commit,state,getters},railDoc) {
            downloadApi.downloadSingleFile('pdf',railDoc).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let fileName = getFilenameFromDoc(railDoc,'.pdf');

                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
            });
        },
    },

}

function getFilenameFromDoc(railDoc,fileExtension) {
    let date = new Date(railDoc.dateStamp);
    const day = date.getDay() < 10 ? '0' + date.getDay() : date.getDay();
    const month  = date.getMonth() < 10 ? '0' + date.getMonth() : date.getMonth();

    date = day + '_' + month + '_' + date.getFullYear();
    const docNumber = railDoc.docNumber;
    const cargoSender = railDoc.cargoSender.railroadCode;
    const cargoReceiver = railDoc.cargoReceiver.railroadCode;
    const sendStation = railDoc.sendStation.rusName;
    const vagCount = railDoc.vagonCount;
    return date + '_(' + cargoSender + ')_' + sendStation +
        vagCount +'ваг_' +  '_ЖД_' + docNumber + '_(' + cargoReceiver + ')'+ fileExtension;

}

function mapDocsToDocsId(railDocuments){
    return railDocuments.map( document =>   {
        return {
            docNumber : document.docNumber,
            dateStamp: document.dateStamp
        }
    })
}


