<template>
    <div class="container">
        <loading-window></loading-window>
        <form class="sticky-top my-2">
            <div class="form-row ">
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

        <div class="container">
            <table id="payments-table" class="table table-fixed able-sm table-striped">
                <thead class="thead-light">
                    <tr class="text-center">
                        <th scope="col" class="col-2">№ перечня</th>
                        <th scope="col" class="col-2">Дата</th>
                        <th scope="col" class="col-2">Входящий остаток</th>
                        <th scope="col" class="col-2">Исходящий остаток</th>
                        <th scope="col" class="col-2">Всего проведено</th>
                        <th scope="col" class="col-2">Действия</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="text-right text-nowrap" v-for="payment in sortedPayments">
                        <td scope="col" class="col-2">{{payment.number}}</td>
                        <td scope="col" class="col-2">{{payment.date | formatDate}}</td>
                        <td scope="col" class="col-2">{{payment.openingBalance | formatPayment }}</td>
                        <td scope="col" class="col-2">{{payment.closingBalance | formatPayment}}</td>
                        <td scope="col" class="col-2">{{payment.paymentVsTaxes | formatPayment}}</td>
                        <td scope="col" class="col-2">
                            <button type="button" class="btn btn-secondary btn-sm" @click="showPayment(payment)">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button type="button" class="btn btn-secondary btn-sm" @click="deletePayment(payment)">
                                <i class="fas fa-trash-alt"></i>
                            </button>
                            <button type="button" class="btn btn-secondary btn-sm" @click="test(payment)">
                                <i class="fas fa-file-download"></i>
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    import {mapActions,mapState} from 'vuex'
    import LoadingWindow from "../components/LoadingWindow.vue";
    export default {
        name: "PaymentsTable",
        components: {LoadingWindow},
        data: function(){
            return{
                dateFrom:'',
                dateUntil:''
            }
        },
        computed: {
            payments: function(){
                return this.$store.getters.paymentLists
            },
            sortedPayments: function(){
                // return this.payments.sort(function (a,b){
                //         if (a.number > b.number) {
                //             return 1;
                //         }
                //         if (a.number < b.number) {
                //             return -1;
                //         }
                //         return 0;
                //     }
                // )
                return this.payments.sort((a,b)=> a.number - b.number)
            }
        },

        created: function(){
            this.updateList();
        },
        methods:{
            ...mapActions(['addPaymentListsAction','deletePaymentListAction']),
            updateList(){
                this.addPaymentListsAction()
            },
            showPayment(payment){

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
                console.log(list)
                this.deletePaymentListAction(list)

            },
            downloadPayment(list){
                //TODO Реализовать функцию скачивания перечня по номеру
                console.log(list)
            },
            getData(){
            //    TODO Реализовать функцию загрузки перечней за определенный период
                this.addPaymentListsAction()
            },
            test(list){
                this.$store.commit('deletePaymentListMutation',list)
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
    }
</script>

<style scoped>
    .sticky-top {
        top: 60px;
    }

    .table-fixed thead {
        width: 100%;
    }
    .table-fixed tbody {
        height: 400px;
        overflow-y: auto;
        width: 100%;
    }
    .table-fixed thead, .table-fixed tbody, .table-fixed tr, .table-fixed td, .table-fixed th {
        display: block;
    }
    .table-fixed tbody td, .table-fixed thead > tr> th {
        float: left;
        border-bottom-width: 0;
    }
</style>