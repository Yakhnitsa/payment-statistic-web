import uploadApi from "../../api/uploadApi";
import messageManager from "../../util/messageManager";
import paymentListApi from "../../api/paymentListApi";

export default {
    namespaced: true,
    state: () => ({
        files:[],
        selectedLists:[],
        tempLists:[],
        lastUpdate:'',
        mailUpdateAwait: false
    }),
    getters: {
        chosenFiles: state => state.files,
        tempUploadedLists: state => state.tempLists,
        selectedUploadedLists: state => state.selectedLists,
        mailUpdateAwait: state => state.mailUpdateAwait,
    },
    mutations: {
        setFilesMutation(state,files){
            state.files = files;
        },
        addChosenFilesMutation(state,data){
            state.files = [...data]
        },

        setMailUpdateAwaitMutation(state,waiting){
            state.mailUpdateAwait = waiting
        },

        setTepListsMutation(state,data){
            state.tempLists = [...data]
        },

    },
    actions: {
        /*Методы для загрузки перечней на сервер*/
        loadTempListsFromServerAction({commit}){
            paymentListApi.downloadTempList()
                .then(response =>{
                    if(response.status === 200){
                        commit('setTepListsMutation',response.data)
                    }

                }).catch((error) => console.log(error));
        },

        async uploadListsOnServerAction({commit,state}) {
            try{
                const response = await uploadApi.uploadListsOnServer(state.files);
                const data = await response.data;
                commit('addChosenFilesMutation',[]);
                commit('setTepListsMutation',data)
            }catch(error){
                if(error.response && error.response.status === 403){
                    messageManager.showSecurityException403()
                }
            }

        },

        async scanFromMailAction({commit,state},params) {
            commit('setMailUpdateAwaitMutation',true);
            try{
                const response = await uploadApi.scanFromMailToTempDB(params);
                if(response.status === 200){
                    const data = await response.data;
                    commit('setTepListsMutation',data)
                }
            }catch(error){
                if(error.response && error.response.status === 403){
                    messageManager.showSecurityException403()
                }
            }finally {
                commit('setMailUpdateAwaitMutation',false)
            }


        },

        async saveSelectedListsAction({commit,state},lists){
            try{
                const response = await uploadApi.saveSelected(lists);
                if(response.status === 200){
                    const data = await response.data;
                    commit('setTepListsMutation',data)
                }
            }catch(error){
                if(error.response && error.response.status === 403) {
                    messageManager.showSecurityException403()
                }
            }
        },

        async deleteSelectedListsAction({commit,state},lists){
            try{
                const response = await uploadApi.deleteSelected(lists);
                if(response.status === 200){
                    const data = await response.data;
                    commit('setTepListsMutation',data)
                }
            }catch(error){
                if(error.response && error.response.status === 403) {
                    messageManager.showSecurityException403()
                }
            }
        },
    },

}