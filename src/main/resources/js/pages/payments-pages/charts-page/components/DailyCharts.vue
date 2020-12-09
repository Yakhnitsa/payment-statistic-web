<template>
    <div class="chart">
        <h2 class="h2 mx-1">Графики показателей по дням</h2>
        <form class="container-sm update-form">
            <div class="form-row mx-1">
                <div class="form-group col-4">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-4">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-4">
                    <button type="button" class="btn btn-primary" v-on:click="updateChart">
                        <span class="button-text">Обновить данные</span>

                    </button>
                </div>
            </div>
        </form>
        <div class="container-fluid my-1">
            <div class="row">
                <linear-chart :height="300" :width="600"
                              :chart-data="linearChartData"
                              :options="linearChartOptions"
                              class="col"
                ></linear-chart>
            </div>
            <div class="row mx-5">
                <div class="form-check-inline">
                    <input class="form-check-input" v-model.docNumber="averageIndex" type="radio" id="threeDaysAverageRadio" value="3" checked>
                    <label class="form-check-label" for="threeDaysAverageRadio">
                        Среднее за 3 дня
                    </label>
                </div>
                <div class="form-check-inline">
                    <input class="form-check-input" v-model.docNumber="averageIndex" type="radio" id="sevenDaysAverageRadio" value="7">
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
    import LinearChart from '../charts/LinearChart.vue'
    import PieChart from '../charts/PieChart'
    import CollectivePieChart from '../charts/CollectivePieChart.vue'

    import { createNamespacedHelpers } from 'vuex';
    const { mapState, mapActions, mapGetters , mapMutations } = createNamespacedHelpers('chartsStore');

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
                // dateFrom:'',
                // dateUntil:'',
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
            }),
            ...mapGetters({
                dateFromStored: 'dailyChartDateFrom',
                dateUntilStored: 'dailyChartDateUntil'
            }),
            dateFrom:{
                get(){
                    return this.dateFromStored;
                },
                set(value){
                    this.setDateFrom(value);
                }
            },
            dateUntil:{
                get(){
                    return this.dateUntilStored;
                },
                set(value){
                    this.setDateUntil(value);
                }
            },

            dates(){
                return this.dailyChartData.map(e => this.$options.filters.formatDate(e.date));
            },
            labels(){
                return this.dailyChartData.map(e => this.$options.filters.formatDate(e.date));
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
                const averageArray = [];
                for(let i = 0; i < this.expenses.length; i++ ){
                    let startIndex = i - Math.trunc(this.averageIndex/2);
                    let endIndex = startIndex + this.averageIndex;
                    startIndex = startIndex < 0 ? 0 : startIndex;
                    let micArray = this.expenses.slice(startIndex,endIndex);
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
                                },
                                beginAtZero: true
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
        },

        methods:{
            ...mapMutations({
                setDateFrom: 'setDailyChartDateFromMutation',
                setDateUntil:'setDailyChartDateUntilMutation'}),
            updateChart(){
                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    payerCode: this.payerCode
                };
                this.$store.dispatch('chartsStore/getDailyChartAction', params)
            },

            setDefaultPeriod(){
                if((this.dateFrom === '') && (this.dateUntil === '')){
                    let today = new Date();
                    this.setDateUntil(today.toISOString().slice(0,10));

                    let weekAgo = new Date(today.setDate(today.getDate()-15));
                    this.setDateFrom(weekAgo.toISOString().substring(0, 10));

                }
            }
        },
        watch:{
            payerCode(){
                this.updateChart();
            }
        },

        mounted(){
            this.setDefaultPeriod();
            this.updateChart()
        }
    }
</script>

<style scoped>


</style>