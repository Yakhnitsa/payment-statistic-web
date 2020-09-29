import statisticApi from "../../api/statisticApi";

export default {
    namespaced: true,
    state: () => ({
        dailyChart: {
            chartData: [],
            dateFrom: '',
            dateUntil: '',
        },
        yearChart: {
            dateFrom: '',
            dateUntil: '',
            chartData: [],
        },
    }),
    getters: {},
    mutations: {
        addDailyChartMutation(state, data) {
            state.dailyChart.chartData = data
        },

        addDailyChartPeriodMutation(state, data) {
            state.dailyChart.dateFrom = data.dateFrom;
            state.dailyChart.dateUntil = data.dateUntil
        },

        addYearChartMutation(state, data) {
            state.yearChart.chartData = data;
        },

        addYearChartPeriodMutation(state, data) {
            state.yearChart.dateFrom = data.dateFrom;
            state.yearChart.dateUntil = data.dateUntil
        },

    },
    actions: {
        getDailyChartAction({commit, state}, params) {
            statisticApi.getDailyChart(params).then(response => {
                commit('addDailyChartMutation', response.data);
                commit('addDailyChartPeriodMutation', params)
            }).catch((error) => console.log(error))
        },

        getYearChartAction({commit, state}, params) {
            statisticApi.getYearChart(params).then(response => {
                commit('addYearChartMutation', response.data);
                commit('addYearChartPeriodMutation', params)
            }).catch((error) => console.log(error));
        },
    },

}