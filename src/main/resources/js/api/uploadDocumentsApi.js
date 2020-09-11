import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content");

export default{

    async uploadSingleFile(file,progress){
        const formData = new FormData();

        formData.append('file',file);

        return await axios.post('/api/upload-single-raildoc',
            formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'X-CSRF-Token': CSRF_TOKEN
                },
                onUploadProgress: function(progressEvent){
                    progress =  Math.round( progressEvent.loaded * 100 / progressEvent.total );
                }
            })
    },
    async uploadMultipleFiles(files,progress){
        const formData = new FormData();

        for(let index = 0; index < files.length; index++) {
            formData.append("files", files[index]);
        }

        return await axios.post('/api/upload-multiple-raildocs',
            formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'X-CSRF-Token': CSRF_TOKEN
                },
                onUploadProgress: function(progressEvent){
                    progress = Math.round( progressEvent.loaded * 100 / progressEvent.total );
                }
            })
    },


}