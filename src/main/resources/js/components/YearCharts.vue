<template>
    <div class="chart">
        <h2 class="h4 mx-1">Годовая статистика платежей и затрат</h2>
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
        <div class="row">
            <linear-chart :height="300" :width="800"
                          :chart-data="linearChartData"
                          :options="linearChartOptions"
                          class="col my-1"
            ></linear-chart>


        </div>
        <!--<div class="row">-->
            <!--<pie-chart :height="300"-->
                       <!--:chart-data="typesChartData"-->
                       <!--:options="typesChartOptions"-->
                       <!--class="col my-1"-->

            <!--&gt;</pie-chart>-->
            <!--<pie-chart :height="300"-->
                       <!--:chart-data="stationsChartData"-->
                       <!--:options="stationsChartOptions"-->
                       <!--class="col my-1"-->

            <!--&gt;</pie-chart>-->
        <!--</div>-->

        <!--<linear-chart :height="300" :width="600"-->
                      <!--:chart-data="linearChartData"-->
                      <!--:options="linearChartOptions"></linear-chart>-->
    </div>
    
</template>

<script>
    import LinearChart from '../components/charts/LinearChart.vue'
    import PieChart from '../components/charts/PieChart'

    import { mapState } from 'vuex';

    import numeral from 'numeral'

    export default {
        name: 'YearChart',
        components: {
            LinearChart,
            PieChart
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
                    '#15dd5a'
                ]

            }
        },
        computed:{
            ...mapState({
                yearChartData: state => state.yearChart.chartData,
                // labels: state => state.dailyChart.labels,// ...
                // expenses: state => state.dailyChart.expensesStatistic.map(element => element/100),// ...
                // payments: state => state.dailyChart.paymentStatistic.map(element => element/100),// ...
                // average: state => state.dailyChart.averageStatistic.map(element => element/100),// ...

            }),

            linearChartLabels(){
                return this.yearChartData.map(element => element.date);
            },
            linearChartExpenses(){
                return this.yearChartData.map(element => element.expenses/100);
            },
            linearChartPayments(){
                return this.yearChartData.map(element => element.payments/100);
            },

            linearChartData(){
                return{
                    labels: this.linearChartLabels,
                    datasets: [
                        {
                            type: 'bar',
                            label: 'Списания',
                            backgroundColor: 'rgba(255, 0, 0, .7)',
                            data: this.linearChartExpenses,
                        },
                        {
                            type: 'bar',
                            label: 'Платежи',
                            backgroundColor: 'rgba(102,0,153,.7)',
                            data: this.linearChartPayments
                        },
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
                                    return numeral(value).format('(0a)')
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

            payerCode(){
                return this.$store.state.payerCode;
            },

        },

        methods:{
            updateChart(){
                var params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    averageIndex: 3,
                    payerCode: this.payerCode
                }
                this.$store.dispatch('getYearChartAction', params)
            },

            setDefaultPeriod(){
                this.dateFrom = this.$store.state.yearChart.dateFrom
                this.dateUntil = this.$store.state.yearChart.dateUntil

                if((this.dateFrom === '') && (this.dateUntil === '')){
                    let yearAgo = new Date();
                    yearAgo.setFullYear(yearAgo.getFullYear()-1)
                    this.dateFrom = yearAgo.toISOString().substring(0, 10);
                    this.dateUntil = new Date().toISOString().slice(0,10);
                    let period = {
                        dateFrom: this.dateFrom,
                        dateUntil: this.dateUntil
                    };
                    this.$store.commit('addYearChartPeriodMutation',period)

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