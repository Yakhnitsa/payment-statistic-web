import uploadApi from "../../api/uploadDocumentsApi";
import messageManager from "../../util/messageManager";

export default {
    namespaced: true,
    state: () => ({
        files:[],
        selectedFiles:[],
        uploadedDocuments:[],
        onUploadProgress: 0
    }),
    getters: {
        files: state => state.files,
        chosenFiles: state => state.chosenFiles,
    },
    mutations: {
        setFilesMutation(state,files){
            state.files = files
        },
        addUploadedDocumentMutation(state, document){
            const index = state.uploadedDocuments
                .findIndex(doc => doc.number === document.number);
            if(index !== -1){
                state.uploadedDocuments.splice(index,1,document);
            }else{
                state.uploadedDocuments.push(document);
            }
        },
        setOnUploadProgressMutation(state, progress){
            state.onUploadProgress = progress;
        }
    },
    actions: {
        async uploadFilesOnServerAction({commit,state}) {
            let currentFile;
            for(let f = 0; f < state.files.length; f++){
                currentFile = state.files[f];
                try{
                    const response = await uploadApi.uploadSingleFile(currentFile);
                    const data =  await response.data;
                    commit('addUploadedDocumentMutation',data);
                    commit('setOnUploadProgressMutation', Math.round(f*100/state.files.length));
                    currentFile.uploaded = true;
                }catch(error){
                    if(error.response){
                        messageManager.showOnLoadException(currentFile.name);
                    }
                }
            }
            commit('setOnUploadProgressMutation', 100);

        },
    },

}