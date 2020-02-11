<template>
    <div>
        <h1>Перечни УТЛ-2</h1>
        <p>{{message}}</p>
        <daily-statistic></daily-statistic>
        <payment-list></payment-list>
    </div>
</template>

<script>
    import DailyStatistic from '../components/DailyStatistic.vue'
    import PaymentList from '../components/PaymentsList.vue'
    export default{
        components:{
            DailyStatistic,
            PaymentList
        },
        data: function() {
            return{
                message: 'Yahooo!!!, эта хрень еще и работает!!!'
            }
        },
    }

</script>

<style>

</style>