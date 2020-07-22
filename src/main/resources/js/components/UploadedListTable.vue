<template>
    <div  class="scrolling-y">
        <table class="table table-striped table-hover">
            <thead class="sticky-header">
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
                <td>
                    <i v-show="!payment.testPassed" class="fa fa-exclamation-circle" style="color:red" aria-hidden="true"></i>
                    {{payment.number}}</td>
                <td>{{payment.date | formatDate}}</td>
                <td class="text-right">{{payment.openingBalance | formatPayment}}</td>
                <td class="text-right">{{payment.closingBalance | formatPayment}}</td>
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


    .sticky-header {
        position: sticky;
        top: 0;
        z-index: 10;
    }
    .scrolling-y {
        max-height:400px;
        overflow-y: scroll;
    }
    .text-right {
        text-align: right;
        padding-right: 2em;
    }

    .text-left {
        text-align: left;
        margin-left: 1em;
    }
    .text-center {
        text-align: center;
    }


</style>