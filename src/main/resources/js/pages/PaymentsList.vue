<template>
    <div class="container">
        <loading-window></loading-window>
        <div class="sticky-top my-2">
            <div class="form-row ">
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button class="btn btn-primary" v-on:click="updateList()">Получить данные</button>
                </div>
                <div class="form-group col-md-2">
                    <button class="btn btn-secondary" @click="downloadArchive()">Архив за период
                        <i class="fas fa-file-archive"></i>
                    </button>
                </div>

            </div>
        </div>

        <div class="container">
            <nav aria-label="Page navigation example">
                <ul class="pagination">
                    <li class="page-item"><a class="page-link" ><i class="fas fa-caret-left"></i></a></li>
                    <li v-for="page in totalPages" class="page-item"><a class="page-link" @click="loadPage(page)" >{{page}}</a></li>
                    <!--<li class="page-item"><a class="page-link">2</a></li>-->
                    <!--<li class="page-item"><a class="page-link">3</a></li>-->
                    <li class="page-item"><a class="page-link"><i class="fas fa-caret-right"></i></a></li>
                </ul>
            </nav>
            <table id="payments-table" class="table table-fixed able-sm table-striped">
                <thead class="thead-light">
                    <tr class="text-center">
                        <th scope="col" class="col-2">№ перечня</th>
                        <th scope="col" class="col-2">Дата</th>
                        <th scope="col" class="col-2">Входящий остаток</th>
                        <th scope="col" class="col-2">Исходящий остаток</th>
                        <th scope="col" class="col-2">Сумма платежей</th>
                        <th scope="col" class="col-2">
                            Действия
                        </th>
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
                            <button type="button" class="btn btn-secondary btn-sm" @click="downloadPayment(payment)">
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
                return this.payments.sort((a,b)=> a.date - b.date)
            },
            currentPage(){
                return this.$store.state.paymentListPage.currentPage
            },
            totalPages(){
                return this.$store.state.paymentListPage.totalPages
            }

        },

        created: function(){
            this.updateList();
        },
        methods:{
            ...mapActions(['getPaymentListsAction','deletePaymentListAction','downloadPaymentListAction']),
            updateList(){
                this.loadPage(0)
            },
            loadPage(page){
                const params = {
                    page:0,
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                }
                this.getPaymentListsAction(params)
            },
            showPayment(payment){

                const params = {
                    listNumber: payment.number,
                    payerCode: payment.payerCode,
                };

                axios.get('/api/single-payment',{ params})
                    .then(function (response) {
                    //    TODO Реализовать отображение перечня в окне
                    console.log(response)
                    console.log('SUCCESS!')
                })
                    .catch((error) => console.log(error))
            },
            deletePayment(list){
                this.deletePayment(list)

            },
            downloadPayment(list){
                this.downloadPaymentListAction(list.backupFilePath)
            },
            // getData(){
            //     const period = {
            //         dateFrom: this.dateFrom,
            //         dateUntil: this.dateUntil,
            //     }
            //     //TODO Реализовать...
            //     this.getPaymentListsAction(,period);
            // },
            test(list){
                // this.$store.commit('deletePaymentListMutation',list)
            },
            downloadArchive(){
                //TODO Реализовать скачивание архива перечней за весь период
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
.btn {
    background-color: DodgerBlue;
    border: none;
    color: white;
    padding: 12px 16px;
    font-size: 16px;
    cursor: pointer;
}

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