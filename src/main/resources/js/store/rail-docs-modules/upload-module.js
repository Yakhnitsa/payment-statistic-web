import uploadApi from "../../api/rail-docs-api/uploadDocumentsApi";
import messageManager from "../../shared/services/messageManager";

export default {
    namespaced: true,
    state: () => ({
        files:[],
        uploadedDocuments:[],
        selectedDocuments:[],
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
        addFilesMutation(state,files){
            files.forEach(file =>{
               let index = state.files.findIndex(exFile => exFile.name === file.name);
               if(index === -1){
                   state.files.push(file);
               }
            });
        },
        deleteSelectedFilesMutation(state){
            state.files = state.files.filter(file => !file.selected);
        },
        deleteAllFilesMutation(state){
            state.files = []
        },

        setSelectedDocumentsMutation(state, documents){
            state.selectedDocuments = documents;
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

        addUploadedDocumentMutation(state, document){
            if(document.hasOwnProperty('docNumber')){
                if(document.docNumber === -1){
                    document.isValid = false;
                    state.uploadedDocuments.push(document);
                } else{
                    document.isValid = true;
                    const index = state.uploadedDocuments
                        .findIndex(doc => doc.docNumber === document.docNumber);
                    if(index !== -1){
                        state.uploadedDocuments.splice(index,1,document);
                    }else{
                        state.uploadedDocuments.push(document);
                    }
                }
            }

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
        async fetchTempUploadedDocs({commit}){
            try{
                const response = await uploadApi.fetchTempUploadedDocs();
                const data = await response.data;
                data.forEach(doc =>{
                    commit('addUploadedDocumentMutation',doc)
                })
            }catch{
                if(error.response){
                    messageManager.showOnLoadException('Ошибка загрузки файлов из временного хранилища');
                }
            }
        },
        async saveSelectedDocumentsToMainDbAction({commit,state}){
            try{
                const response = await uploadApi.saveDocumentsToMainDb(state.selectedDocuments);
                const data = await response.data;
                state.uploadedDocuments = data['temp-docs'];
                messageManager.showSuccessfullyUploadedDocs(data['saved-docs']);

            }catch{
                if(error.response){
                    messageManager.showOnLoadException('Ошибка при сохранении документов в основную базу данных');
                }
            }

        },
        async deleteSelectedDocumentsFromTempDbAction({commit,state}){
            try{
                const response = await uploadApi.deleteDocumentsFromTempDb(state.selectedDocuments);
                state.uploadedDocuments = await response.data;

            }catch{
                if(error.response){
                    messageManager.showOnLoadException('Ошибка при удалении документов с временной БД');
                }
            }
        }
    },

}