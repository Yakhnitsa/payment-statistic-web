import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    getRailroadDocuments: params => axios.get('/api/documents/railroad-documents',{params}),

    downloadArchive: params => axios({
        url: '/api/documents/download-archive',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

    downloadPdfBackupFile: params => axios({
        url: '/api/documents/download-pdf',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

    downloadXmlBackupFile: params => axios({
        url: '/api/documents/download-xml',
        method: 'GET',
        params,
        responseType: 'blob',
    }),

}