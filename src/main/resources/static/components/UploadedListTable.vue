<template>
    <div>
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>
                    <label class="form-checkbox">
                        <input type="checkbox" v-model="selectAll" @click="select">
                        <i class="form-icon"></i>
                    </label>
                </th>
                <th>payer code</th>
                <th>payer numb</th>
                <th>date</th>
                <th>open balance</th>
                <th>close balance</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="payment in payments">
                <td>
                    <label class="form-checkbox">
                        <input type="checkbox" :value="payment" v-model="selectedPayments">
                        <i class="form-icon"></i>
                    </label>
                </td>
                <td>{{payment.payerCode}}</td>
                <td>{{payment.number}}</td>
                <td>{{payment.date | formatDate}}</td>
                <td>{{payment.openingBalance | formatPayment}}</td>
                <td>{{payment.closingBalance | formatPayment}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    export default {
        name: "UploadedListTable",
        props:['payments',],
        data: function(){
            return{
                selectAll: false,
                selectedPayments:[],
            }
        },
        methods: {
            select() {
                this.selectedPayments = [];
                if (!this.selectAll) {
                    for (let i in this.payments) {
                        this.selectedPayments.push(this.payments[i]);
                    }
                }
            }
        },
        watch:{
            selectedPayments: function () {
                this.$emit('change-selected',this.selectedPayments)
            }
        },
        filters:{
            formatPayment(payment){
                if (!parseInt(payment)) {
                    return '';
                }
                if (payment > 99999) {
                    var paymentString = (payment / 100).toFixed(2);
                    var paymentArray = paymentString.split('').reverse();
                    var index = 3;
                    while (paymentArray.length > index + 3) {
                        paymentArray.splice(index + 3, 0, ' ');
                        index += 4;
                    }
                    return paymentArray.reverse().join('');
                } else {
                    return (payment / 100).toFixed(2);
                }
            },
            formatDate(dateLong){
                return new Date(dateLong).toLocaleDateString()
            }
        },
        created: function(){
        },
        mounted: function(){
        }
    }
</script>

<style scoped>

</style>