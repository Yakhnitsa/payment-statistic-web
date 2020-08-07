<template>
    <div>
        <div>Test component</div>
        <div>{{dateFrom}}</div>
        <div>{{dateUntil}}</div>
        <div>{{paymentType}}</div>
        <button class="btn btn-outline-secondary" @click="routeTo()">Перейти...</button>
        <router-link
                :to="{name: 'payment-details',
                    params: {
                        hasRequestParams: true,
                        payerCode: '8210260',
                        dateFrom: '2020-07-20',
                        dateUntil: '2020-08-03'
                    }
                }"
                class="nav-item nav-link">With name test</router-link>

        <router-link
                :to="{path: 'payment-details',
                    params: {
                        hasRequestParams: true,
                        payerCode: '8210260',
                        dateFrom: '2020-07-20',
                        dateUntil: '2020-08-03'
                    }
                }"
                class="nav-item nav-link">With path test</router-link>

    </div>


</template>

<script>
    export default {
        props:['dateFrom','dateUntil','paymentType'],
        methods:{
            routeTo(){
                const route = {
                    name: 'payment-details',
                    params: {
                        hasRequestParams: true,
                        payerCode: '5635841',
                        dateFrom: '2020-07-20',
                        dateUntil: '2020-08-03'
                    }
                }
                this.$router.push(route);
            }
        }
    }
</script>

<style scoped>

</style>