<template>
    <div class="chart">
        <h2 class="h2 mx-1">Статистика по дням</h2>
        <form>
            <div class="form-row mx-1">
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button type="button" class="btn btn-primary" v-on:click="updateChart">Получить данные</button>
                </div>
            </div>
        </form>
        <linear-chart :height="300" :width="600"
                      :chart-data="chartData"
                      :options="chartOptions"></linear-chart>
    </div>
    
</template>

<script>
    import LinearChart from '../components/charts/LinearChart.vue'

    import { mapState } from 'vuex';

    import numeral from 'numeral'

    export default {
        name: 'DailyChart',
        components: {
            LinearChart
        },
        data(){
            return{
                dateFrom:'',
                dateUntil:''
            }
        },
        computed:{
            ...mapState({
                labels: state => state.dailyChart.labels,// ...
                expenses: state => state.dailyChart.expensesStatistic.map(element => element/100),// ...
                payments: state => state.dailyChart.paymentStatistic.map(element => element/100),// ...
                average: state => state.dailyChart.averageStatistic.map(element => element/100),// ...
            }),

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
                    maintainAspectRatio: true,
                    scales: {
                        yAxes: [{
                            ticks: {
                                // Include a dollar sign in the ticks
                                callback: function(value, index, values) {
                                    return numeral(value).format('₴0,0')
                                }
                            }
                        }]
                    },
                    // Настройка отображения значений столбцов
                    tooltips: {
                        callbacks: {
                            label(tooltipItem, data) {
                                // Get the dataset label.
                                const label = data.datasets[tooltipItem.datasetIndex].label;

                                // Format the y-axis value.
                                const value = numeral(tooltipItem.yLabel).format('₴0,0')

                                return `${label}: ${value}`;
                            }
                        }
                    }
                }
            }

        },

        methods:{
            updateChart(){
                var params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
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