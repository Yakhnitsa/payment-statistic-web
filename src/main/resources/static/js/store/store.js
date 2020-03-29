import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

import axios from 'axios'

// Централизованное хранилище Vuex для данных приложения
export default new Vuex.Store({
    // Состояние объекта, массивы и прочее
    state: {
        dailyStatistic: { details:[], payments: []}
    },
    // Изменяемые свойства объекта, computed properties, ets...
    getters:{
        // sortedMessages: state => (state.messages || []).sort((a,b) => -(a.id - b.id))
    },
    // Методы для изменения объектов приолжения
    mutations: {
        addDailyStatisticMutation(state, data){
            state.dailyStatistic.details = [...data.details];
            state.dailyStatistic.payments = [...data.payments];

        },
    },
    // Асинхронные запросы на изменение данных хранилища
    actions:{
        // async addMessageAction({commit,state},message){
        //     // const result = await messagesApi.add(message)
        //     // const data = await result.json()
        //     // const index = state.messages.findIndex(item => item.id === data.id);
        //     // if(index > -1){
        //     //     commit('updateMessageMutation',data)
        //     // }else{
        //     //     commit('addMessageMutation',data)
        //     // }
        // },
        getDailyStatisticAction({commit,state},params){
            // // console.log(params);
            // const params = {
            //     dateFrom: date1,
            //     dateUntil: date2,
            // };
            console.log(params);

            axios.get('/api/daily-statistic',
                {params}, {}
            ).then(response => {
                console.log(response)
                // this.detailsList = response.data.details;
                // this.paymentLists = response.data.payments;
            })
                .catch((error) => console.log(error))
        },
        // async updateMessageAction({commit},message){
        //     // const result = await messagesApi.update(message)
        //     // const data = await result.json()
        //     // commit('updateMessageMutation',data)
        // },
        // async removeMessageAction({commit},message){
        //     // const result = await messagesApi.remove(message.id)
        //     // if(result.ok){
        //     //     commit('removeMessageMutation',message)
        //     // }
        // },
    }
})
