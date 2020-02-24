<template>
    <div>


        <h1>Перечни УТЛ-2</h1>

        <p>{{message}}</p>
        <loading-window></loading-window>
        <!--<daily-statistic></daily-statistic>-->
        <payment-list :payments="payments"></payment-list>
    </div>
</template>

<script>
    import axios from 'axios'
    import DailyStatistic from '../components/DailyStatistic.vue'
    import PaymentList from '../components/PaymentsList.vue'
    import LoadingWindow from '../components/LoadingWindow.vue'

    export default{
        components:{
            DailyStatistic,
            PaymentList,
            LoadingWindow
        },
        data: function() {
            return{
                message: 'Yahooo!!!, эта хрень еще и работает!!!',
                payments:[],
                loadedPayments:[],
            }
        },
        created: function(){
            axios.get('/api/payments')
                .then(response => {
                    console.log(response);
                    response.data.forEach( payment => this.payments.push(payment))
                }

            )
        }
    }

</script>

<style>

</style>