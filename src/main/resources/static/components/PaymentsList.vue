<template>
    <div class="container">
        <h3> PaymentList component</h3>
        <div v-for="payment in payments">
            <p>{{payment.number}}</p>
        </div>
    </div>
</template>

<script>
    export default {
        name: "PaymentsList",
        props: ['payments'],
        data: function(){
            return{

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
                        paymentArray.splice(index + 3, 0, ',');
                        index += 4;
                    }
                    return '$' + paymentArray.reverse().join('');
                } else {
                    return '$' + (payment / 100).toFixed(2);
                }
            }
        }
    }
</script>

<style scoped>

</style>