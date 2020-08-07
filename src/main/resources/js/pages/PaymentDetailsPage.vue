<template>
    <div class="container">
        <payment-details-request-form :inputData.sync="someData"
                :has-request-params="hasRequestParams"
                :request-params="requestParams"
                @submit-form="updateData"></payment-details-request-form>
        <payment-details-table></payment-details-table>
    </div>


</template>

<script>
    import PaymentDetailsRequestForm from "../components/PaymentDetailsRequestForm.vue";
    import PaymentDetailsTable from "../components/PaymentDetailsTable.vue";
    export default {
        name: "PaymentDetailsPage",
        props:['hasRequestParams','payerCode', 'paymentType', 'dateFrom',
            'dateUntil', 'currentPage' , 'stationCode', 'docNumber', 'paymentSum'],
        components: {PaymentDetailsTable, PaymentDetailsRequestForm},
        data(){
            return{
                someData:''
            }
        },
        computed:{
            requestParams(){
                return {
                    payerCode: this.payerCode,
                    paymentType: this.paymentType,
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    currentPage: this.currentPage,
                    stationCode: this.stationCode,
                    docNumber: this.docNumber,
                    paymentSum: this.paymentSum,
                };
            }
        },
        methods:{
            updateData(params){
                console.log(params)
                this.$store.dispatch('getPaymentDetailsAction',params);
            }
        },
        mounted(){
            if(this.hasRequestParams){
                // const params = {
                //     payerCode: this.payerCode,
                //     paymentType: this.paymentType,
                //     dateFrom: this.dateFrom,
                //     dateUntil: this.dateUntil,
                //     currentPage: this.currentPage,
                //     stationCode: this.stationCode,
                //     docNumber: this.docNumber,
                //     paymentSum: this.paymentSum,
                // };
                this.updateData(this.requestParams);
            }
        }
    }
</script>

<style scoped>

</style>