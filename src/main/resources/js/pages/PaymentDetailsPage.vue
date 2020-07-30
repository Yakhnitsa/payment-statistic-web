<template>
    <div class="container">
        <payment-details-request-form></payment-details-request-form>
        <payment-details-table></payment-details-table>
    </div>


</template>

<script>
    import PaymentDetailsRequestForm from "../components/PaymentDetailsRequestForm.vue";
    import PaymentDetailsTable from "../components/PaymentDetailsTable.vue";
    export default {
        name: "PaymentDetailsPage",
        components: {PaymentDetailsTable, PaymentDetailsRequestForm}
    }
</script>

<style scoped>

</style>