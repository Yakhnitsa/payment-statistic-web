import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    downloadSingleFile: (type,document) => axios({
        url: '/api/raildocs-download/' + type,
        method: 'GET',
        params:{dateStamp: document.dateStamp, docNumber: document.docNumber},
        responseType: 'blob',
    }),
    // downloadXml: document => axios({
    //     url: '/api/raildocs-download/xml' + file,
    //     method: 'GET',
    //     params:{dateStamp: document.dateStamp, docDate: document.docDate},
    //     responseType: 'blob',
    // }),
    // downloadArchive: params => axios({
    //     url: '/api/download/archive',
    //     method: 'GET',
    //     params,
    //     responseType: 'blob',
    // }),

}