import downloadApi from "../../api/rail-docs-api/railDocsDownloadApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({

    }),
    getters: {

    },
    mutations: {

    },
    actions: {
        async downloadXmlFileAction({commit,state,getters},railDoc) {
            downloadApi.downloadSingleFile('xml',railDoc).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let fileName = getFilenameFromDoc(railDoc,'.xml');
                // const disposition = response.headers['content-disposition'];
                // if (disposition && disposition.indexOf('attachment') !== -1) {
                //     const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                //     let matches = filenameRegex.exec(disposition);
                //     if (matches != null && matches[1]) fileName = matches[1].replace(/['"]/g, '');
                // }

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
                // const disposition = response.headers['content-disposition'];
                // if (disposition && disposition.indexOf('attachment') !== -1) {
                //     const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                //     let matches = filenameRegex.exec(disposition);
                //     if (matches != null && matches[1]) fileName = matches[1].replace(/['"]/g, '');
                // }

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