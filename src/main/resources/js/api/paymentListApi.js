import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content")

export default{
    getPaymentLists: params => axios.get('/api/payments',{params}),
    deletePaymentList: data => axios.delete('/api/remove-payment', {
        headers: {
            'X-CSRF-Token': CSRF_TOKEN
        },data
    }),
    downloadPaymentList: file => axios({
        url: '/api/download/file/' + file,
        method: 'GET',
        params:{file: file},
        responseType: 'blob',
    }),
    downloadArchive: params => axios({
        url: '/api/download/archive',
        method: 'GET',
        params,
        responseType: 'blob',
    }),
    downloadTempList: () => axios.get('/api/download-temp'),

    getSinglePayment: params => axios.get('/api/single-payment',{ params})

}