<template>
    <div class="container">
        <h3> PaymentList component</h3>
        <table id="payments-table" class="table table-sm">
            <thead class="thead-light">
                <tr>
                    <th scope="col">№ перечня</th>
                    <th scope="col">Дата</th>
                    <th scope="col">Входящий остаток</th>
                    <th scope="col">Исходящий остаток</th>
                    <th scope="col">Всего проведено</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="payment in payments">
                    <td scope="col">{{payment.number}}</td>
                    <td scope="col">{{payment.date | formatDate}}</td>
                    <td scope="col">{{payment.openingBalance | formatPayment }}</td>
                    <td scope="col">{{payment.closingBalance | formatPayment}}</td>
                    <td scope="col">{{payment.paymentVsTaxes | formatPayment}}</td>
                </tr>
            </tbody>
        </table>
        <!--<div v-for="payment in payments">-->
            <!--<p>{{payment.number}}</p>-->
        <!--</div>-->
    </div>
</template>

<script>
    export default {
        name: "PaymentsTable",
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
                        paymentArray.splice(index + 3, 0, ' ');
                        index += 4;
                    }
                    return paymentArray.reverse().join('');
                } else {
                    return (payment / 100).toFixed(2);
                }
            },
            formatDate(dateLong){
                // var date = new Date(dateLong);
                return new Date(dateLong).toLocaleDateString()
            }
        },
        created: function(){
            // console.log("created");
            // $(document).ready(function() {
            //     console.log("data table initialized")
            //     $('#payments-table').DataTable();
            // } );
        },
        methods:{

        }
    }
</script>

<style scoped>

</style>