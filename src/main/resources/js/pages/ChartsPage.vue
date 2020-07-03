<template>
    <div class="container mx-1">
        <daily-charts></daily-charts>
        <year-charts></year-charts>

    </div>
</template>

<script>
    import DailyCharts from '../components/DailyCharts.vue'
    import YearCharts from '../components/YearCharts.vue'


    export default {
        name: 'ChartPage',
        components: {
            DailyCharts,
            YearCharts
        },

    }
</script>

<style scoped>

</style>