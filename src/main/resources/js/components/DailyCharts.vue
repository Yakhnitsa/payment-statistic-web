<template>
    <div class="chart">
        <h2 class="h2 mx-1">Графики показателей по дням</h2>
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
        <div class="container my-1">
            <div class="row">
                <linear-chart :height="300" :width="600"
                              :chart-data="linearChartData"
                              :options="linearChartOptions"
                              class="col"
                ></linear-chart>
            </div>
            <div class="row mx-5">
                <div class="form-check-inline">
                    <input class="form-check-input" v-model.number="averageIndex" type="radio" id="threeDaysAverageRadio" value="3" checked>
                    <label class="form-check-label" for="threeDaysAverageRadio">
                        Среднее за 3 дня
                    </label>
                </div>
                <div class="form-check-inline">
                    <input class="form-check-input" v-model.number="averageIndex" type="radio" id="sevenDaysAverageRadio" value="7">
                    <label class="form-check-label" for="sevenDaysAverageRadio">
                        Среднее за 7 дней
                    </label>
                </div>
            </div>

        </div>

        <collective-pie-chart
                :dates="dates"
                :expensesByType="expensesByType"
                :expensesByStation="expensesByStation">
        </collective-pie-chart>

    </div>
    
</template>

<script>
    import LinearChart from '../components/charts/LinearChart.vue'
    import PieChart from '../components/charts/PieChart'
    import CollectivePieChart from '../components/charts/CollectivePieChart.vue'

    import { mapState } from 'vuex';

    import numeral from 'numeral'

    export default {
        name: 'DailyChart',
        components: {
            LinearChart,
            PieChart,
            CollectivePieChart

        },
        data(){
            return{
                dateFrom:'',
                dateUntil:'',
                colours:[
                    '#E47514',
                    '#ac4fe4',
                    '#1abf60',
                    '#ff37cc',
                    '#28fff7',
                    '#976948',
                    '#149764',
                    '#974a11',
                    '#151f97',
                    '#6ed617',
                    '#0b9725',
                    '#b313dc',
                    '#1987e8',
                    '#759725',
                    '#13ffe2',
                    '#15dd5a',
                    '#E47514',
                    '#ac4fe4',
                    '#1abf60',
                    '#ff37cc',
                    '#28fff7',
                    '#976948',
                    '#149764',
                    '#974a11',
                    '#151f97',
                    '#6ed617',
                    '#0b9725',
                    '#b313dc',
                    '#1987e8',
                    '#759725',
                    '#13ffe2',
                    '#15dd5a'
                ],
                averageIndex: 3,

            }
        },
        computed:{
            ...mapState({
                dailyChartData: state => state.dailyChart.chartData,
                // labels: state => state.dailyChart.labels,// ...
                // expenses: state => state.dailyChart.expensesStatistic.map(element => element/100),// ...
                // payments: state => state.dailyChart.paymentStatistic.map(element => element/100),// ...
                // average: state => state.dailyChart.averageStatistic.map(element => element/100),// ...

            }),


            dates(){
                return this.dailyChartData.map(e => e.date);
            },
            labels(){
                return this.dailyChartData.map(e => e.date);
            },
            expenses(){
                return this.dailyChartData.map(e => e.expenses / 100);
            },
            payments(){
                return this.dailyChartData.map(e => e.payments/ 100);
            },
            expensesByType(){
                return this.dailyChartData.map(e => e.expensesByType);
            },
            expensesByStation(){
                return this.dailyChartData.map(e => e.expensesByStation);
            },
            average(){
                const averageArray = []
                for(let i = 0; i < this.expenses.length; i++ ){
                    let startIndex = i - Math.trunc(this.averageIndex/2);
                    let endIndex = startIndex + this.averageIndex;
                    startIndex = startIndex < 0 ? 0 : startIndex;
                    let micArray = this.expenses.slice(startIndex,endIndex)
                    let average = micArray.reduce((a, b) => a + b, 0)/ micArray.length
                    averageArray.push(average)
                }
                return averageArray;
            },

            payerCode(){
              return this.$store.state.payerCode;
            },

            linearChartData(){
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

            linearChartOptions(){
                return{
                    responsive: false,
                    maintainAspectRatio: true,
                    scales: {
                        yAxes: [{
                            ticks: {
                                // Include a dollar sign in the ticks
                                callback: function(value, index, values) {
                                    return numeral(value).format('0a')
                                }
                            }
                        }],
                        xAxes: [{
                            offset: true,
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
            },



            // typesChartData(){
            //     return{
            //         labels: this.dailyChart.typeChartData.map(element => element.type),
            //         datasets: [
            //             {
            //                 backgroundColor: this.colours,
            //                 data: this.dailyChart.typeChartData
            //                     .map(element => element.value / 100)
            //             }
            //         ]
            //     }
            // },
            //
            // typesChartOptions(){
            //     return{
            //         responsive: true,
            //         maintainAspectRatio: false,
            //         pieceLabel: {
            //             mode: 'percentage',
            //             precision: 1
            //         },
            //         title: {
            //             display: true,
            //             fontsize: 14,
            //             text: 'По виду списаний'
            //         },
            //         legend: {
            //             display: false,
            //             position: 'bottom',
            //
            //         },
            //         tooltips:{
            //             callbacks: {
            //                 label: function(tooltipItem, data) {
            //                     let label = data.labels[tooltipItem.index] || ''
            //                     let value = data.datasets[0].data[tooltipItem.index]
            //                     value = numeral(value).format('0,0')
            //                     return label + ': ' + value
            //                 }
            //             }
            //
            //         },
            //         animation: {
            //             animateScale: true,
            //             animateRotate: true
            //         },
            //     }
            // },
            //
            // stationsChartData(){
            //     return{
            //         dataentry: null,
            //         datalabel: null,
            //         labels: this.dailyChart.stationChartData.map(element => element.type),
            //         datasets: [
            //             {
            //                 backgroundColor: this.colours,
            //                 data: this.dailyChart.stationChartData.map(element => element.value / 100),
            //             }
            //         ]
            //     }
            // },
            //
            // stationsChartOptions(){
            //     return{
            //         responsive: true,
            //         maintainAspectRatio: false,
            //         pieceLabel: {
            //             mode: 'percentage',
            //             precision: 1
            //         },
            //         title: {
            //             display: true,
            //             fontsize: 12,
            //             text: 'По станциям'
            //         },
            //         legend: {
            //             display: false,
            //             position: 'bottom',
            //
            //         },
            //         tooltips:{
            //             callbacks: {
            //                 label: function(tooltipItem, data) {
            //                     let label = data.labels[tooltipItem.index] || ''
            //                     let value = data.datasets[0].data[tooltipItem.index]
            //                     value = numeral(value).format('₴0,0').replace(',','`')
            //                     return label + ': ' + value
            //                 }
            //             }
            //
            //         },
            //     }
            // },

        },

        methods:{
            updateChart(){
                var params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    averageIndex: 3,
                    payerCode: this.payerCode
                }
                this.$store.dispatch('getDailyChartAction', params)
            },

            setDefaultPeriod(){
                this.dateFrom = this.$store.state.dailyChart.dateFrom
                this.dateUntil = this.$store.state.dailyChart.dateUntil

                if((this.dateFrom == '') && (this.dateUntil == '')){
                    let today = new Date();
                    let weekAgo = new Date(today.setDate(today.getDate()-15));
                    this.dateFrom = weekAgo.toISOString().substring(0, 10);
                    this.dateUntil = new Date().toISOString().slice(0,10);
                    let period = {
                        dateFrom: this.dateFrom,
                        dateUntil: this.dateUntil
                    }
                    this.$store.commit('addDailyChartPeriodMutation',period)

                }
            }
        },

        mounted(){
            this.setDefaultPeriod()
            this.updateChart()
        }
    }
</script>

<style scoped>

    .chart {
        background: #70fff7;
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