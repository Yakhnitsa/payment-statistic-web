<template>
    <div class="container">
        <h3> PaymentList component</h3>
        <form>
            <div class="form-row">
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button type="button" class="btn btn-primary" v-on:click="getData">Получить данные</button>
                </div>
            </div>
        </form>

        <table id="payments-table" class="table table-sm">
            <thead class="thead-light">
                <tr class="text-center">
                    <th scope="col">№ перечня</th>
                    <th scope="col">Дата</th>
                    <th scope="col">Входящий остаток</th>
                    <th scope="col">Исходящий остаток</th>
                    <th scope="col">Всего проведено</th>
                    <th scope="col">Действия</th>
                </tr>
            </thead>
            <tbody>
                <tr class="text-right text-nowrap" v-for="payment in payments">
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
                        <button type="button" class="btn btn-secondary btn-sm" @click="downloadPayment(payment)">
                            <i class="fas fa-file-download"></i>
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "PaymentsTable",
        data: function(){
            return{
                payments:[],
                dateFrom:'',
                dateUntil:''
            }
        },
        filters:{
            formatPayment(num) {
                num = num/100;
                return (
                    num
                        .toFixed(2) // always two decimal digits
                        .replace('.', ',') // replace decimal point character with ,
                        .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1 ') // use ' ' as a separator
                )
            },
            formatDate(dateLong){
                return new Date(dateLong).toLocaleDateString()
            }
        },

        created: function(){
            this.updateList();
        },
        methods:{
            updateList(){
                this.payments = [];
                axios.get('/api/payments')
                    .then(response => {
                            response.data.forEach( payment => this.payments.push(payment))
                        }

                    )
            },
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
            },
            downloadPayment(list){
                //TODO Реализовать функцию скачивания перечня по номеру
                console.log(list)
            },
            getData(){
            //    TODO Реализовать функцию загрузки перечней за определенный период
            }
        }
    }
</script>

<style scoped>

</style>