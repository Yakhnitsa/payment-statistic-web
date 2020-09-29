import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

import statisticApi from '../api/statisticApi'
import paymentListApi from "../api/paymentListApi";
import paymentDetailsApi from "../api/paymentDetailsApi";
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
        payerCode: '',
        dailyStatistic: {
            dates: [],
            details: [],
            payments: [],
            expensesByStation: [],
            dateFrom: '',
            dateUntil: ''
        },
        paymentListPage: {
            paymentLists: [],
            currentPage: 0,
            totalPages: 0,
            dateFrom: '',
            dateUntil: ''
        },
        paymentDetailsPage: {
            paymentDetails: [],
            paymentTypes: [],
            currentPage: 0,
            totalPages: 0,
            totalElements: 0,
            //Параметры запроса
            dateFrom: '',
            dateUntil: '',
            stationCode: '',
            docNumber: '',
            paymentType: '',
            paymentSum: ''
        },
    },
    // Изменяемые свойства объекта, computed properties, ets...
    getters: {
        paymentLists: state => state.paymentListPage.paymentLists,
        userRoles: () => userRoles,
        hasEditorPermission: () => userRoles.includes('ROLE_EDITOR'),
        inDeveloperMode: () => isDevMode,
        stations: () => stations,
        paymentCodes: () => paymentCodes,
    },
    // Методы для изменения объектов приолжения
    mutations: {

        setPayerCodeMutation(state, code) {
            state.payerCode = code
        },

        addPaymentListsMutation(state, data) {
            state.paymentListPage.paymentLists = [...data.list];
            state.paymentListPage.currentPage = data.currentPage;
            state.paymentListPage.totalPages = data.totalPages;
        },

        setPaymentListPeriod(state, period) {
            state.paymentListPage.dateFrom = period.dateFrom;
            state.paymentListPage.dateUntil = period.dateUntil
        },

        deletePaymentListMutation(state, list) {
            const index = state.paymentListPage.paymentLists.findIndex(element =>
                (element.number === list.number) && (element.payerCode === list.payerCode)
            );
            if (index > -1) {
                state.paymentListPage.paymentLists.splice(index, 1);
            }
        },

        addDailyStatisticMutation(state, data) {
            state.dailyStatistic.dates = data.dates;
            state.dailyStatistic.details = data.details;
            state.dailyStatistic.payments = data.payments;
            state.dailyStatistic.expensesByStation = data.expensesByStation;

        },

        setDailyStatisticPeriod(state, period) {
            state.dailyStatistic.dateFrom = period.dateFrom
            state.dailyStatistic.dateUntil = period.dateUntil
        },

        setPaymentTypesMutation(state, data) {
            state.paymentDetailsPage.paymentTypes = data;
        },

        addPaymentDetailsMutation(state, data) {
            state.paymentDetailsPage.paymentDetails = data.content;
            state.paymentDetailsPage.totalPages = data.totalPages;
            state.paymentDetailsPage.totalElements = data.totalElements;
        }

    },

    actions: {

        getPaymentListsAction({commit, state}, params) {
            paymentListApi.getPaymentLists(params)
                .then(response => {
                        commit('addPaymentListsMutation', response.data)
                    }
                )
        },

        getSinglePaymentListAction({commit, state}, data) {
            //TODO реализовать страницу перечня и данные по нем
            console.log(data);
        },

        deletePaymentListAction({commit, state}, list) {
            paymentListApi.deletePaymentList(list)
                .then(function (response) {
                    commit('deletePaymentListMutation', response.data)
                })
                .catch((error) => console.log(error))
        },

        downloadPaymentListAction({commit, state}, file) {
            paymentListApi.downloadPaymentList(file).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', file);
                document.body.appendChild(link);
                link.click();
            });
        },

        downloadPaymentListsArchiveAction({}, params) {
            paymentListApi.downloadArchive(params).then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                let achName = 'archive.zip';
                const disposition = response.headers['content-disposition'];
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

        // Методы обработки статистических данных
        getDailyStatisticAction({commit, state}, params) {
            statisticApi.getDailyStatistic(params).then(response => {
                commit('addDailyStatisticMutation', response.data)
            }).catch((error) => console.log(error))
        },

        //Payment details actions

        getPaymentDetailsAction({commit, state}, params) {
            paymentDetailsApi.getPayments(params)
                .then(response => {
                    if (response.status === 200) {
                        commit('addPaymentDetailsMutation', response.data);
                    }
                })
        },

        getPaymentTypesAction({commit, state}, params) {
            paymentDetailsApi.getPaymentTypes().then(response => {
                commit('setPaymentTypesMutation', response.data);
            }).catch((error) => console.log(error));
        }
    }

})

