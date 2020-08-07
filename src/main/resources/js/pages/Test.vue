<template>
    <div>
        <div>Test component</div>
        <div>{{dateFrom}}</div>
        <div>{{dateUntil}}</div>
        <div>{{paymentType}}</div>
        <button class="btn btn-outline-secondary" @click="submitForm()">Обновить данные</button>
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
    import PaymentDetailsTable from "../components/PaymentDetailsTable.vue";
    export default {
        props:['dateFrom','dateUntil','paymentType'],
    }
</script>

<style scoped>

</style>