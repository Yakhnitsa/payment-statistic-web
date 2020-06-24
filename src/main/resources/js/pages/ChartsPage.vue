<template>
    <div id="test">
        <linear-chart
                class="daily-chart"
                :data="chartData"
                :options="chartOptions"></linear-chart>
    </div>
</template>

<script>
    import LinearChart from '../components/LinearChart.vue'

    export default {
        name: 'app',
        components: {
            LinearChart
        },
        data(){
            return{
                chartData:{
                    labels: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
                    datasets: [
                        {
                            label: 'Коммиты на GitHub',
                            backgroundColor: '#f87979',
                            data: [40, 12, 40, 29, 15, 42, 35, 80, 40, 20, 12, 11]
                        },
                        {
                            label: 'Затраты на разработку',
                            backgroundColor: '#a92990',
                            data: [10, 15, 11, 29, 15, 41, 22, 44, 18, 20, 25, 14]
                        }
                    ],
                },
                chartOptions:{responsive: true, maintainAspectRatio: false},
            }
        }
    }
</script>

<style scoped>
    .daily-chart{
        max-width: 500px;
        max-height: 300px;
    }
</style>