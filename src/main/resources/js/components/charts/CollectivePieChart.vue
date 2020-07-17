<template>
    <div class="container">
        <ul class="pagination">
            <li class="page-item"
                :class="{disabled: this.index < 1}">
                <a class="page-link" @click="decrementIndex()"><i class="fas fa-caret-left"></i></a>
            </li>
            <li class="page-item h5">{{currentPeriod}}</li>
            <!--<li class="page-item"><a class="page-link">2</a></li>-->
            <!--<li class="page-item"><a class="page-link">3</a></li>-->
            <li class="page-item"
                :class="{disabled: index >= dates.length - 1}">
                <a class="page-link" @click="incrementIndex()"><i class="fas fa-caret-right"></i></a>
            </li>
        </ul>
        <div class="row">
            <pie-chart :height="300"
                       :chart-data="typesChartData"
                       :options="typesChartOptions"
                       class="col my-1"
            ></pie-chart>
            <pie-chart :height="300"
                       :chart-data="stationsChartData"
                       :options="stationsChartOptions"
                       class="col my-1"

            ></pie-chart>
        </div>
    </div>

</template>

<script>
    import PieChart from './PieChart'
    import numeral from 'numeral'

    export default {
        name: "CollectivePieChart",
        props:['dates','expensesByStation','expensesByType'],
        components:{PieChart},
        data(){
            return{
                index:0,
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
            }
        },
        computed:{
            currentPeriod(){
              return this.dates[this.index];
            },
            typesChartData(){
                return{
                    labels : this.getLabels(this.expensesByType[this.index]),
                    datasets: [
                        {
                            backgroundColor: this.colours,
                            data: this.getValues(this.expensesByType[this.index])
                        }
                    ]
                }
            },
            typesChartOptions(){
                return{
                    responsive: true,
                    maintainAspectRatio: false,
                    pieceLabel: {
                        mode: 'percentage',
                        precision: 1
                    },
                    title: {
                        display: true,
                        fontsize: 14,
                        text: 'По виду списаний'
                    },
                    legend: {
                        display: false,
                        position: 'bottom',

                    },
                    tooltips:{
                        callbacks: {
                            label: function(tooltipItem, data) {
                                let label = data.labels[tooltipItem.index] || ''
                                let value = data.datasets[0].data[tooltipItem.index]
                                value = numeral(value).format('0,0')
                                return label + ': ' + value
                            }
                        }

                    },
                    animation: {
                        animateScale: true,
                        animateRotate: true
                    },
                }
            },

            stationsChartData(){
                return{
                    labels : this.getLabels(this.expensesByStation[this.index]),
                    datasets: [
                        {
                            backgroundColor: this.colours,
                            data: this.getValues(this.expensesByStation[this.index])
                        }
                    ]
                }
            },
            stationsChartOptions(){
                return{
                    responsive: true,
                    maintainAspectRatio: false,
                    pieceLabel: {
                        mode: 'percentage',
                        precision: 1
                    },
                    title: {
                        display: true,
                        fontsize: 12,
                        text: 'По станциям'
                    },
                    legend: {
                        display: false,
                        position: 'bottom',
                    },
                    tooltips:{
                        callbacks: {
                            label: function(tooltipItem, data) {
                                let label = data.labels[tooltipItem.index] || ''
                                let value = data.datasets[0].data[tooltipItem.index]
                                value = numeral(value).format('₴0,0')
                                return label + ': ' + value
                            }
                        }

                    },
                }
            },

        },
        methods:{
            incrementIndex(){
                if(this.index < this.dates.length) this.index++;
            },
            decrementIndex(){
                if(this.index > 0) this.index--;
            },
            getLabels(dataArray){
                if(dataArray.length === 0) return ['no data']
                return dataArray.map(element => element.type)
            },
            getValues(dataArray){
                if(dataArray.length === 0) return [0]
                return dataArray.map(element => element.value / 100)
            }
        },
        mounted(){

        }
    }
</script>

<style scoped>

</style>