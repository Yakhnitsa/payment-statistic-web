import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{
    async testPostRequest(params, payload){
        return await axios({
            method:'post',
            url: '/api/test',
            params: params,
            data: payload,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    },
}