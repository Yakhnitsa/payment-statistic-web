import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content")

export default{

    async uploadListsOnServer(files){
        const formData = new FormData();

        for(let index = 0; index < files.length; index++) {
            formData.append("files", files[index]);
        }

        // formData.append("_csrf", CSRF_TOKEN);
        return await axios.post('/api/upload-multiple',
            formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'X-CSRF-Token': CSRF_TOKEN
                }

            })
    },

    async scanFromMailToTempDB(lastUpdate){
        const formData = new FormData();
        formData.append('lastUpdate',lastUpdate);
        return await axios.post('/api/scan-from-mail',
            formData, {
                headers: {
                    'X-CSRF-Token': CSRF_TOKEN
                }
            });
    },

    async saveSelected(lists) {
        return await axios({
            method:'post',
            url: '/api/save-temp-selected',
            data: lists,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    },

    async deleteSelected(lists){
        return await axios({
            method:'post',
            url: '/api/delete-temp-selected',
            data: lists,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    }

}