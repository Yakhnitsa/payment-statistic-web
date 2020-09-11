import uploadApi from "../../api/uploadDocumentsApi";
import messageManager from "../../util/messageManager";

export default {
    namespaced: true,
    state: () => ({
        files:[],
        selectedFiles:[],
        uploadedDocuments:[],
        onUploadProgress: {progress:0}
    }),
    getters: {
        files: state => state.files,
        chosenFiles: state => state.chosenFiles,
    },
    mutations: {
        setFilesMutation(state,files){
            state.files = files
        },
    },
    actions: {
        async uploadListsOnServerAction({commit,state}) {
            try{
                const response = await uploadApi.uploadMultipleFiles(state.selectedFiles,onUploadProgress);
            }catch(error){
                if(error.response && error.response.status === 403){
                    messageManager.showSecurityException403()
                }
            }

        },
    },

}