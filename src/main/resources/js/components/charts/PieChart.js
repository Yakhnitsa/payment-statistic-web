import { Pie,mixins } from 'vue-chartjs'
const { reactiveProp } = mixins

export default {
    extends: Pie,
    name: "PieChart",
    mixins: [reactiveProp],
    props:['options'],

    mounted () {
        this.renderChart(this.chartData,this.options)
    }
}