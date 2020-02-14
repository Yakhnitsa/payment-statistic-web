<template>
    <div>
        <p>{{loadedPayments}}</p>
    </div>

</template>

<script>
    export default {
        name: "LoadingWindow",
        props:['loadedPayments']
    }
</script>

<style scoped>

</style>