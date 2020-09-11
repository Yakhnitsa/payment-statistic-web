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
            const index = state.uploadedDocuments.findIndex(doc => doc.number = document.number);
            if(index > 0){
                state.uploadedDocuments.splice(index,1,document);
            }
        },
        setOnUploadProgressMutation(state, progress){
            console.log(progress);
            state.onUploadProgress = progress;
        }
    },
    actions: {
        async uploadFilesOnServerAction({commit,state}) {
            console.log('uploadsBegin...');
            let currentFile;
            for(let f = 0; f < state.files.length; f++){
                console.log(f);
                try{
                    currentFile = state.files[f];
                    const response = await uploadApi.uploadSingleFile(currentFile);
                    commit.addUploadedDocumentMutation(response.data);
                    console.log(Math.round(f*100));
                    commit.setOnUploadProgressMutation(Math.round(f*100/state.files.length));

                }catch(error){
                    if(error.response){
                        messageManager.showOnLoadException(currentFile.name);
                    }
                }
            }
            console.log('uploads end...');

        },
    },

}