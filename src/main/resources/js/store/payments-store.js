import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import statisticApi from '../api/statisticApi'
import paymentListApi from "../api/paymentListApi";
import paymentDetailsApi from "../api/paymentDetailsApi";
import messageManager from '../util/messageManager';
import CommonModule from "./common-module";
import UploadModule from "./payments-modules/upload-module";
import ChartsModule from "./payments-modules/charts-module";


export default new Vuex.Store({
    modules: {
        uploadStore: UploadModule,
        commonStore: CommonModule,
        chartsStore: ChartsModule
    },
    state: {
        payerCode:'',
        dailyStatistic: {
            dates:[],
            details:[],
            payments: [],
            expensesByStation:[],
            dateFrom:'',
            dateUntil:''
        },
        // dailyChart:{
        //     chartData:[],
        //     dateFrom:'',
        //     dateUntil:'',
        // },
        // yearChart:{
        //     dateFrom:'',
        //     dateUntil:'',
        //     chartData:[],
        // },
        paymentListPage:{
            paymentLists: [],
            currentPage:0,
            totalPages: 0,
            dateFrom:'',
            dateUntil:''
        },
        paymentDetailsPage:{
            paymentDetails:[],
            paymentTypes:[],
            currentPage:0,
            totalPages:0,
            totalElements: 0,
            //Параметры запроса
            dateFrom:'',
            dateUntil:'',
            stationCode:'',
            docNumber:'',
            paymentType:'',
            paymentSum:''
        },
    },
    // Изменяемые свойства объекта, computed properties, ets...
    getters:{
        paymentLists: state => state.paymentListPage.paymentLists,
        userRoles: () => userRoles,
        hasEditorPermission: () => userRoles.includes('ROLE_EDITOR'),
        inDeveloperMode:() => isDevMode,
        stations:() => stations,
        paymentCodes:() => paymentCodes,
    },
    // Методы для изменения объектов приолжения
    mutations: {

        setPayerCodeMutation(state,code){
          state.payerCode = code
        },

        addPaymentListsMutation(state,data){
            state.paymentListPage.paymentLists = [...data.list];
            state.paymentListPage.currentPage = data.currentPage;
            state.paymentListPage.totalPages = data.totalPages;
        },

        setPaymentListPeriod(state,period){
            state.paymentListPage.dateFrom = period.dateFrom
            state.paymentListPage.dateUntil = period.dateUntil
        },

        deletePaymentListMutation(state,list){
            const index = state.paymentListPage.paymentLists.findIndex(element =>
                (element.number === list.number) && (element.payerCode === list.payerCode)
            )
            if(index >-1){
                state.paymentListPage.paymentLists.splice(index, 1);
            }
        },

        addDailyStatisticMutation(state, data){
            state.dailyStatistic.dates = data.dates
            state.dailyStatistic.details = data.details;
            state.dailyStatistic.payments = data.payments;
            state.dailyStatistic.expensesByStation = data.expensesByStation;

        },

        setDailyStatisticPeriod(state,period){
            state.dailyStatistic.dateFrom = period.dateFrom
            state.dailyStatistic.dateUntil = period.dateUntil
        },

        // addDailyChartMutation(state, data){
        //     state.dailyChart.chartData = data
        // },
        //
        // addDailyChartPeriodMutation(state,data){
        //     state.dailyChart.dateFrom = data.dateFrom
        //     state.dailyChart.dateUntil = data.dateUntil
        // },
        //
        // addYearChartMutation(state, data){
        //     state.yearChart.chartData = data;
        // },
        //
        // addYearChartPeriodMutation(state,data){
        //     state.yearChart.dateFrom = data.dateFrom
        //     state.yearChart.dateUntil = data.dateUntil
        // },

        setPaymentTypesMutation(state,data){
            state.paymentDetailsPage.paymentTypes = data;
        },

        addPaymentDetailsMutation(state,data){
            state.paymentDetailsPage.paymentDetails = data.content;
            state.paymentDetailsPage.totalPages = data.totalPages;
            state.paymentDetailsPage.totalElements = data.totalElements;
        }

    },

    actions:{

        getPaymentListsAction({commit,state},params){
            paymentListApi.getPaymentLists(params)
                .then(response => {
                        commit('addPaymentListsMutation',response.data)
                    }
                )
        },

        getSinglePaymentListAction({commit,state},data){
            //TODO реализовать страницу перечня и данные по нем
            console.log(data);
        },

        deletePaymentListAction({commit,state},list){
            paymentListApi.deletePaymentList(list)
                .then(function (response) {
                    commit('deletePaymentListMutation',response.data)
            })
                .catch((error) => console.log(error))
        },

        downloadPaymentListAction({commit,state},file){
            paymentListApi.downloadPaymentList(file).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', file);
                document.body.appendChild(link);
                link.click();
            });
        },

        downloadPaymentListsArchiveAction({},params){
            paymentListApi.downloadArchive(params).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let achName = 'archive.zip'
                const disposition = response.headers['content-disposition']
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    let matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) achName = matches[1].replace(/['"]/g, '');
                }

                link.href = url;
                link.setAttribute('download', achName);
                document.body.appendChild(link);
                link.click();
            });
        },

        // /*Методы для загрузки перечней на сервер*/
        // loadTempListsFromServerAction({commit}){
        //     paymentListApi.downloadTempList()
        //         .then(response =>{
        //         if(response.status === 200){
        //             commit('setTepListsMutation',response.data)
        //         }
        //
        //     }).catch((error) => console.log(error));
        // },
        //
        // async uploadListsOnServerAction({commit,state}) {
        //     try{
        //         const response = await uploadApi.uploadListsOnServer(state.uploadedData.files);
        //         const data = await response.data;
        //         commit('addChosenFilesMutation',[])
        //         commit('setTepListsMutation',data)
        //     }catch(error){
        //         if(error.response && error.response.status == 403){
        //             messageManager.showSecurityException403()
        //         }
        //     }
        //
        // },
        //
        // async scanFromMailAction({commit,state},params) {
        //     commit('setMailUpdateAwaitMutation',true);
        //     try{
        //         const response = await uploadApi.scanFromMailToTempDB(params);
        //         if(response.status === 200){
        //             const data = await response.data;
        //             commit('setTepListsMutation',data)
        //         }
        //     }catch(error){
        //         if(error.response && error.response.status == 403){
        //             messageManager.showSecurityException403()
        //         }
        //     }finally {
        //         commit('setMailUpdateAwaitMutation',false)
        //     }
        //
        //
        // },
        //
        // async saveSelectedListsAction({commit,state},lists){
        //     try{
        //         const response = await uploadApi.saveSelected(lists)
        //         if(response.status === 200){
        //             const data = await response.data
        //             commit('setTepListsMutation',data)
        //         }
        //     }catch(error){
        //         if(error.response && error.response.status == 403) {
        //             messageManager.showSecurityException403()
        //         }
        //     }
        // },
        //
        // async deleteSelectedListsAction({commit,state},lists){
        //     try{
        //         const response = await uploadApi.deleteSelected(lists);
        //         if(response.status === 200){
        //             const data = await response.data;
        //             commit('setTepListsMutation',data)
        //         }
        //     }catch(error){
        //         if(error.response && error.response.status == 403) {
        //             messageManager.showSecurityException403()
        //         }
        //     }
        // },

        // Методы обработки статистических данных
        getDailyStatisticAction({commit,state},params){
            statisticApi.getDailyStatistic(params).then(response => {
                commit('addDailyStatisticMutation',response.data)
            }).catch((error) => console.log(error))
        },

        getDailyChartAction({commit,state},params) {
            statisticApi.getDailyChart(params).then(response =>{
                commit('addDailyChartMutation',response.data)
                commit('addDailyChartPeriodMutation',params)
            }).catch((error) => console.log(error))
        },

        getYearChartAction({commit,state},params){
            statisticApi.getYearChart(params).then(response =>{
                commit('addYearChartMutation',response.data)
                commit('addYearChartPeriodMutation',params)
            }).catch((error) => console.log(error));
        },

        //Payment details actions

        getPaymentDetailsAction({commit,state},params){
            paymentDetailsApi.getPayments(params)
                .then(response => {
                    if(response.status === 200){
                        commit('addPaymentDetailsMutation',response.data);
                    }

                })
        },

        getPaymentTypesAction({commit,state},params){
            paymentDetailsApi.getPaymentTypes().then(response =>{
                commit('setPaymentTypesMutation',response.data);
            }).catch((error) => console.log(error));
        }

    }

})

