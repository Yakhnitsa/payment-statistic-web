<template>
    <div class="container-fluid">
        <daily-charts></daily-charts>
        <year-charts></year-charts>
    </div>
</template>

<script>
    import DailyCharts from './components/DailyCharts.vue'
    import YearCharts from './components/YearCharts.vue'

    export default {
        name: 'ChartPage',
        components: {
            DailyCharts,
            YearCharts
        },

    }
</script>

<style>
    .update-form{
        max-width: 35em;
        margin-left: .5em;
    }

    .chart {
        background: #70fff7;
        border-radius: 5px;
        box-shadow: 0 2px 15px rgba(25, 25, 25, 0.27);
        margin:  25px 0;
        min-width: 620px;
    }

    .chart h2 {
        margin-top: 0;
        padding: 15px 0;
        color:  rgba(255, 0,0, 0.5);
        border-bottom: 1px solid #323d54;
    }

</style>