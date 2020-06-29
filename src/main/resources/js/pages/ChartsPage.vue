<template>
    <div class="container mx-1">
        <daily-charts></daily-charts>
    </div>
</template>

<script>
    import DailyCharts from '../components/DailyCharts.vue'


    export default {
        name: 'ChartPage',
        components: {
            DailyCharts
        },

    }
</script>

<style scoped>

</style>