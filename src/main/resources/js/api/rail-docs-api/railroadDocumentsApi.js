import axios from 'axios'
axios.defaults.baseURL = '/api/documents/';
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    getRailroadDocuments: params => axios.get('/railroad-documents',{params}),

    downloadArchive: params => axios({
        url: '/download-archive',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

    downloadPdfBackupFile: params => axios({
        url: '/download-pdf',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

    downloadXmlBackupFile: params => axios({
        url: '/download-xml',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

}