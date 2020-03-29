<template>
    <div>
        <!--TODO Настроить Header с путями к разным данным приложения
            - Ежедневная статистика
            - Список перечней
            - Окно загрузки перечней
            - Прочья хрень
        -->

        <router-link to="/daily-statistic">Статистика по дням</router-link>
        <router-link to="/payments">Список перечней</router-link>
        <div>
            <router-view></router-view>
        </div>


        <!--<button @click="test">TEST</button>-->
        <!--<loading-window v-on:update-list="updateList"></loading-window>-->
        <!--<daily-statistic></daily-statistic>-->
        <!--<payment-list :payments="payments" v-on:update-list="updateList"></payment-list>-->
    </div>
</template>

<script>
    import axios from 'axios'
    import DailyStatistic from './DailyStatistic.vue'
    import PaymentList from './PaymentsList.vue'
    import LoadingWindow from '../components/LoadingWindow.vue'

    export default{
        components:{
            DailyStatistic,
            PaymentList,
            LoadingWindow
        },
        data: function() {
            return{
                payments:[],
                loadedPayments:[],
            }
        },
        created: function(){
            this.updateList();
        },
        methods:{
            test(event){
                // var formData = new FormData();
                // var json = [{ "firstName" : "Hammond", "lastName" : "Fergison" },
                //     { "firstName" : "Elmer", "lastName" : "Fudd" }];
                // formData.append("pojo",json)
                // axios.post('/api/test',json)
                //     .then(result => console.log(result))
                console.log(event)
            },
            updateList(){
                this.payments = [];
                axios.get('/api/payments')
                    .then(response => {
                            response.data.forEach( payment => this.payments.push(payment))
                        }

                    )
            },

        }
    }

</script>

<style>

</style>