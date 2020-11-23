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
                let fileName = 'file.xml';
                const disposition = response.headers['content-disposition'];
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    let matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) fileName = matches[1].replace(/['"]/g, '');
                }

                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
            });
        },
        async downloadPdfFileAction({commit,state,getters},document) {
            downloadApi.downloadSingleFile('pdf',document).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let fileName = 'file.pdf';
                const disposition = response.headers['content-disposition'];
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    let matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) fileName = matches[1].replace(/['"]/g, '');
                }

                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
            });
        },

    },

}