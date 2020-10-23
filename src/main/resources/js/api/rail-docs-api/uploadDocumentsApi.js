import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    async uploadSingleFile(file,progress){
        const formData = new FormData();

        formData.append('file',file);

        return await axios.post('/api/documents/upload-single-raildoc',
            formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'X-CSRF-Token': CSRF_TOKEN
                },
                onUploadProgress: function(progressEvent){
                    // progress =  Math.round( progressEvent.loaded * 100 / progressEvent.total );
                    // console.log(progress);
                }
            })
    },
    async fetchTempUploadedDocs(){
        return await axios.get('/api/documents/get-temp-uploaded');
    },

    async saveDocumentsToMainDb(documents){
        return await axios({
            method:'post',
            url: '/api/documents/save-docs-to-main-db',
            data: documents,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    },
    async deleteDocumentsFromTempDb(documents){
        return await axios({
            method:'delete',
            url: '/api/documents/delete-docs-from-temp-db',
            data: documents,
            headers:{
                'X-CSRF-Token': CSRF_TOKEN
            }
        })
    }

}