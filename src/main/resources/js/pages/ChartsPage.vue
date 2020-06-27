<template>
    <div class="mx-2 chart">
        <h2 class="h2 mx-1">Статистика по дням</h2>
        <button class="btn btn-secondary mx-1" @click="test()">test</button>
        <linear-chart :height="300" :width="800"
                :chart-data="chartData"
                :options="chartOptions"></linear-chart>
    </div>
</template>

<script>
    import LinearChart from '../components/LinearChart.vue'

    import { mapState } from 'vuex';

    export default {
        name: 'ChartPage',
        components: {
            LinearChart
        },
        computed:{
            ...mapState({
                labels: state => state.dailyChart.labels,// ...
                expenses: state => state.dailyChart.expensesStatistic.map(element => element/100),// ...
                payments: state => state.dailyChart.paymentStatistic.map(element => element/100),// ...
                average: state => state.dailyChart.averageStatistic.map(element => element/100),// ...
            }),
            // chartData(){
            //     return{
            //         labels: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            //         datasets: [
            //             {
            //                 type: 'bar',
            //                 label: 'Коммиты на GitHub',
            //                 backgroundColor: '#f87979',
            //                 data: [40, 12, 40, 29, 15, 42, 35, 80, 40, 20, 12, 11]
            //             },
            //             {
            //                 type: 'bar',
            //                 label: 'Затраты на разработку',
            //                 backgroundColor: '#a92990',
            //                 data: [10, 15, 11, 29, 15, 41, 22, 44, 18, 20, 25, 14]
            //             }
            //         ],
            //     }
            // },
            chartData(){
                return{
                    labels: this.labels,
                    datasets: [
                        {
                            type: 'bar',
                            label: 'Списания',
                            backgroundColor: 'rgba(255, 0, 0, .7)',
                            data: this.expenses,
                        },
                        {
                            type: 'bar',
                            label: 'Платежи',
                            backgroundColor: 'rgba(102,0,153,.7)',
                            data: this.payments
                        },
                        {
                            type: 'line',
                            label: 'Среднее списание',
                            borderColor: '#e17805',
                            borderWidth: 1,
                            pointBackgroundColor: 'white',
                            pointBorderColor: '#e17805',
                            backgroundColor: 'rgba(0,255,0,0.7)',
                            data: this.average
                        }
                    ],
                }
            },
            chartOptions(){
                return{
                    responsive: false,
                    maintainAspectRatio: true
                }
            }

        },

        methods:{
            test(){
                var params = {
                    dateFrom: '2020-04-20',
                    dateUntil: '2020-05-20',
                    averageIndex: 3
                }
                console.log(params);
                this.$store.dispatch('getDailyChartAction', params)
            },
        }
    }
</script>

<style scoped>
    .chart {
        background: #8dffe6;
        border-radius: 5px;
        box-shadow: 0px 2px 15px rgba(25, 25, 25, 0.27);
        margin:  25px 0;
    }

    .chart h2 {
        margin-top: 0;
        padding: 15px 0;
        color:  rgba(255, 0,0, 0.5);
        border-bottom: 1px solid #323d54;
    }

</style>