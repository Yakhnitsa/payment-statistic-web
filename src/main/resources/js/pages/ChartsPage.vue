<template>
    <div id="test">
        <linear-chart :height="300" :width="500"
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
                            type: 'bar',
                            label: 'Коммиты на GitHub',
                            backgroundColor: '#f87979',
                            data: [40, 12, 40, 29, 15, 42, 35, 80, 40, 20, 12, 11]
                        },
                        {
                            type: 'bar',
                            label: 'Затраты на разработку',
                            backgroundColor: '#a92990',
                            data: [10, 15, 11, 29, 15, 41, 22, 44, 18, 20, 25, 14]
                        }
                    ],
                },
                chartOptions:{
                    responsive: false,
                    maintainAspectRatio: true},
            }
        }
    }
</script>

<style scoped>

</style>