import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import axios from 'axios'

// Централизованное хранилище Vuex для данных приложения
export default new Vuex.Store({
    // Состояние объекта, массивы и прочее
    state: {
        dailyStatistic: {
            details:[],
            payments: []
        },
        paymentLists: [],
        uploadedData:{
            files:[],
            selectedLists:[],
            tempLists:[]
        }
    },
    // Изменяемые свойства объекта, computed properties, ets...
    getters:{
        chosenFiles: state => state.uploadedData.files,
        tempUploadedLists: state => state.uploadedData.tempLists,
        selectedUploadedLists: state => state.uploadedData.selectedLists
        // sortedMessages: state => (state.messages || []).sort((a,b) => -(a.id - b.id))
    },
    // Методы для изменения объектов приолжения
    mutations: {
        addDailyStatisticMutation(state, data){
            state.dailyStatistic.details = data.details;
            state.dailyStatistic.payments = data.payments;

        },
        addPaymentListsMutation(state,data){
            state.paymentLists = [...data];
        },
        deletePaymentListMutation(state,list){
            const index = state.paymentLists.findIndex(element =>
                (element.number === list.number) && (element.payerCode == list.payerCode)
            )
            if(index >-1){
                state.paymentLists.splice(index, 1);
            }
        },

        addChosenFilesMutation(state,data){
            state.uploadedData.files = [...data]
        },
        setTepListsMutation(state,data){
            state.uploadedData.tempLists = [...data]
        }

    },
    // Асинхронные запросы на изменение данных хранилища
    actions:{
        /**/
        getDailyStatisticAction({commit,state},params){
            axios.get('/api/daily-statistic',
                {params}, {}
            ).then(response => {
                commit('addDailyStatisticMutation',response.data)
            })
                .catch((error) => console.log(error))
        },
        addPaymentListsAction({commit,state},params){
            axios.get('/api/payments',{params})
                .then(response => {
                        commit('addPaymentListsMutation',response.data)
                    }

                )
        },
        deletePaymentListAction({commit,state},data){
            axios.delete('/api/remove-payment', { data: data})
                .then(function (response) {
                    commit('deletePaymentListMutation',response.data)
            })
                .catch((error) => console.log(error))
        },
        downloadPaymentListAction({commit,state},params){

        },

        /*Методы для загрузки перечней на сервер*/
        uploadListsOnServerAction({commit,state}) {
            var formData = new FormData();

            for(var index = 0; index < state.uploadedData.files.length; index++) {
                formData.append("files", state.uploadedData.files[index]);
            }

            axios.post('/api/upload-multiple',
                formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }

                }
            ).then(response =>{
                if(response.status == 200){
                    commit('addChosenFilesMutation',[])
                    commit('setTepListsMutation',response.data)

                }
                else if(response.status == 204){
                    console.log("no content!");
                }
            }).catch((error) => console.log(error));
        },
        //    TODO Реализовать методы загрузки данных на сервер...
        saveSelectedListsAction({commit,state},lists){},
        deleteSelectedListsAction({commit,state},lists){}



    }
})
