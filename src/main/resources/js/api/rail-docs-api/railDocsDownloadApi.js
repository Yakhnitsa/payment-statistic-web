import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    downloadSingleFile: (type,document) => axios({
        url: '/api/raildocs-download/file/' + type,
        method: 'GET',
        params:{dateStamp: document.dateStamp, docNumber: document.docNumber, fileType: type},
        responseType: 'blob',
    }),

    downloadArchive: payload => axios({
        url: '/api/raildocs-download/archive',
        method: 'POST',
        data: payload,
        headers:{
            'X-CSRF-Token': CSRF_TOKEN
        },
        responseType: 'blob',
    }),

}