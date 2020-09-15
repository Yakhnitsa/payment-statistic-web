import uploadApi from "../../api/uploadDocumentsApi";
import messageManager from "../../util/messageManager";

export default {
    namespaced: true,
    state: () => ({
        files:[],
        uploadedDocuments:[],
        onUploadProgress: 0
    }),
    getters: {
        files: state => state.files,
        uploadedDocuments: state => state.uploadedDocuments,
        selectedFiles: state => state.files.filter(file => file.selected),
        onUploadProgress: state => state.onUploadProgress,
    },
    mutations: {
        setFilesMutation(state,files){
            state.files = files;
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
        },
        setFileUploadedMutation(state,updatedFile){
            const index = state.files
                .findIndex(doc => doc.name === updatedFile.name);

            updatedFile.uploaded = true;
            state.files.splice(index,1,updatedFile);
        },
        setFileSelectedMutation(state,selectedFile){
            const index = state.files
                .findIndex(doc => doc.name === selectedFile.name);
            selectedFile.selected = !selectedFile.selected;
            state.files.splice(index,1,selectedFile);
        },
    },
    actions: {
        async uploadFilesOnServerAction({commit,state,getters}) {
            let currentFile;
            const selectedFiles = getters.selectedFiles;
            for(let f = 0; f < selectedFiles.length; f++){
                currentFile = selectedFiles[f];
                try{
                    const response = await uploadApi.uploadSingleFile(currentFile);
                    const data =  await response.data;
                    commit('addUploadedDocumentMutation',data);
                    commit('setOnUploadProgressMutation', Math.round(f*100/selectedFiles.length));
                    commit('setFileUploadedMutation',currentFile);
                    commit('setFileSelectedMutation',currentFile);
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