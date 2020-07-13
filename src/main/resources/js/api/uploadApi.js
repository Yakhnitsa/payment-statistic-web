import axios from 'axios'
const CSRF_TOKEN = $("meta[name='_csrf']").attr("content")

export default{

    uploadListsOnServer: files => {
        const formData = new FormData();

        for(var index = 0; index < files.length; index++) {
            formData.append("files", files[index]);
        }

        formData.append("_csrf", CSRF_TOKEN);
        axios.post('/api/upload-multiple',
            formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }

            }
        )
    },

}