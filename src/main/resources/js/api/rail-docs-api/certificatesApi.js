import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    getCertificatesRegister: params => axios.get('/api/documents/certificates',{params}),

    async uploadChangesToServer(params, payload){
        return await axios({
            method:'post',
            url: '/api/documents/certificates',
            params: params,
            data: payload,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    },

}