import uploadApi from "../../api/uploadApi";
import messageManager from "../../util/messageManager";

export default {
    state: () => ({
        files:[],
        selectedLists:[],
        tempLists:[],
        lastUpdate:'',
        mailUpdateAwait: false
    }),
    getters: {
        chosenFiles: state => state.uploadedData.files,
        tempUploadedLists: state => state.uploadedData.tempLists,
        selectedUploadedLists: state => state.uploadedData.selectedLists,
        mailUpdateAwait: state => state.uploadedData.mailUpdateAwait,
    },
    mutations: {
        setFilesMutation(state,files){
            state.files = files;
        },


    },
    actions: {

    },

}