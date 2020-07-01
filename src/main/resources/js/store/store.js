import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import axios from 'axios'

import statisticApi from '../api/statisticApi'

// Централизованное хранилище Vuex для данных приложения
export default new Vuex.Store({
    // Состояние объекта, массивы и прочее
    state: {
        dailyStatistic: {
            dates:[],
            details:[],
            payments: [],
            dateFrom:'',
            dateUntil:''
        },
        dailyChart:{
            dateFrom:'',
            dateUntil:'',
            labels:[],
            paymentStatistic:[],
            expensesStatistic:[],
            averageStatistic:[],
            typeChartData:[],
            stationChartData:[]
        },
        paymentListPage:{
            paymentLists: [],
            currentPage:0,
            totalPages: 0,
            dateFrom:'',
            dateUntil:''
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
        paymentLists: state => state.paymentLists,
        chosenFiles: state => state.uploadedData.files,
        tempUploadedLists: state => state.uploadedData.tempLists,
        selectedUploadedLists: state => state.uploadedData.selectedLists
    },
    // Методы для изменения объектов приолжения
    mutations: {
        addDailyStatisticMutation(state, data){
            state.dailyStatistic.dates = data.dates
            state.dailyStatistic.details = data.details;
            state.dailyStatistic.payments = data.payments;

        },
        addPaymentListsMutation(state,data){
            state.paymentLists = [...data.list];
            state.paymentListPage.currentPage = data.currentPage;
            state.paymentListPage.totalPages = data.totalPages;
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
        },

        setDailyStatisticPeriod(state,period){
            state.dailyStatistic.dateFrom = period.dateFrom
            state.dailyStatistic.dateUntil = period.dateUntil
        },

        setPaymentListPeriod(state,period){
            state.paymentListPage.dateFrom = period.dateFrom
            state.paymentListPage.dateUntil = period.dateUntil
        },

        addDailyChartMutation(state, data){
            state.dailyChart.labels = data.labels;
            state.dailyChart.paymentStatistic = data.paymentStatistic;
            state.dailyChart.expensesStatistic = data.expensesStatistic;
            state.dailyChart.averageStatistic = data.averageStatistic;
            state.dailyChart.typeChartData= data.typeChartData
            state.dailyChart.stationChartData = data.stationChartData
        },

        addDailyChartPeriodMutation(state,data){
            state.dailyChart.dateFrom = data.dateFrom
            state.dailyChart.dateUntil = data.dateUntil
        }

    },
    // Асинхронные запросы на изменение данных хранилища
    actions:{
        /**/


        getPaymentListsAction({commit,state},params){

            axios.get('/api/payments',{params})
                .then(response => {
                        commit('addPaymentListsMutation',response.data)
                        commit('setCurrentPageMutation',response.data.currentPage)
                        commit('setTotalPagesMutation',response.data.totalPages)
                    }

                )
        },

        getSinglePaymentListAction({commit,state},data){
            //TODO реализовать страницу перечня и данные по нем
            console.log(data);
        },

        deletePaymentListAction({commit,state},data){
            axios.delete('/api/remove-payment', { data: data})
                .then(function (response) {
                    commit('deletePaymentListMutation',response.data)
            })
                .catch((error) => console.log(error))
        },

        downloadPaymentListAction({commit,state},file){
            axios({
                url: '/api/download/file/' + file,
                method: 'GET',
                params:{file: file},
                responseType: 'blob',
            }).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', file);
                document.body.appendChild(link);
                link.click();
            });
        },

        downloadPaymentListsArchiveAction({},params){
            axios({
                url: '/api/download/archive',
                method: 'GET',
                params,
                responseType: 'blob',
            }).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                const achName = 'archive.zip';
                link.href = url;
                link.setAttribute('download', achName);
                document.body.appendChild(link);
                link.click();
            });
        },

        /*Методы для загрузки перечней на сервер*/
        loadTempListsFromServerAction({commit}){
            axios.get('/api/download-temp')
                .then(response =>{
                if(response.status == 200){
                    commit('setTepListsMutation',response.data)
                }

            }).catch((error) => console.log(error));
        },

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

        saveSelectedListsAction({commit,state},lists){
            axios.post('/api/save-temp-selected',
                lists, {
                }
            ).then(response =>{
                if(response.status == 200){
                    commit('setTepListsMutation',response.data)
                }
            }).catch((error) => console.log(error));

        },

        deleteSelectedListsAction({commit,state},lists){
            axios.post('/api/delete-temp-selected',lists)
                .then(response =>{
                    if(response.status ==200){
                        commit('setTepListsMutation',response.data)
                    }
            }).catch(error => console.log(error))
        },

        // Методы обработки статистических данных
        getDailyStatisticAction({commit,state},params){
            statisticApi.getDailyStatistic(params).then(response => {
                commit('addDailyStatisticMutation',response.data)
            })
                .catch((error) => console.log(error))
        },

        getDailyChartAction({commit,state},params) {
            statisticApi.getDailyChart(params).then(response =>{
                commit('addDailyChartMutation',response.data)
                commit('addDailyChartPeriodMutation',params)
            }).catch((error) => console.log(error))
        }



    }

})
