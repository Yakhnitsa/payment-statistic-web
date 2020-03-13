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
                    <td scope="col">
                        <button type="button" class="btn btn-secondary btn-sm" @click="showPayment(payment)">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button type="button" class="btn btn-secondary btn-sm" @click="deletePayment(payment)">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
        <!--<div v-for="payment in payments">-->
            <!--<p>{{payment.number}}</p>-->
        <!--</div>-->
    </div>
</template>

<script>
    import axios from 'axios'
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
            showPayment(payment){
                var formData = new FormData();

                formData.append("listNumber",payment.number);
                formData.append("payerCode",payment.payerCode)

                const params = {
                    listNumber: payment.number,
                    payerCode: payment.payerCode,
                };

                axios.get('/api/single-payment',
                    {params}, {
                        // headers: {
                        //     'Content-Type': 'multipart/form-data'
                        // }

                    }
                ).then(function (response) {
                    console.log(response)
                    console.log('SUCCESS!')
                })
                    .catch((error) => console.log(error))
            },
            deletePayment(list){
                var URL = '/api/delete-payment/' + list.payerCode + '_' + list.number
                axios.delete(URL)
                    .then(response =>{
                        console.log(response)
                        this.$emit('update-list')
                    })
                    .catch(error => console.log(error))
            }
        }
    }
</script>

<style scoped>

</style>