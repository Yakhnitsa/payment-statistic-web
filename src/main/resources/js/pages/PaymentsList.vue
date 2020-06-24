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
                    <li v-for="page in totalPages" class="page-item"><a class="page-link" @click="loadPage(page-1)" >{{page}}</a></li>
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
                dateFrom:'2020-05-01',
                dateUntil:'2020-05-10'
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
            this.dateFrom = this.$store.state.paymentListPage.dateFrom
            this.dateUntil = this.$store.state.paymentListPage.dateUntil
            this.updateList();
        },
        methods:{
            ...mapActions(['getPaymentListsAction','deletePaymentListAction','downloadPaymentListAction']),
            updateList(){
                this.loadPage(0)
            },
            loadPage(page){
                const params = {
                    page,
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                }
                this.$store.commit('setPaymentListPeriod',params)
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
            test(list){
                // this.$store.commit('deletePaymentListMutation',list)
            },
            downloadArchive(){

                const period = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil
                }
                const length = this.periodLength(period)
                if(Number.isNaN(length) || length > 60 || length < 1){
                    let message = 'Неверно выбранный период, период архива должен быль больше 0 и меньше 60 дней. ' +
                        'Запрашиваемый период: ' + length + ' дней.'
                    this.showWarningMessage(message)
                    return
                }
                this.$store.dispatch('downloadPaymentListsArchiveAction', period)
            },
            //Проверка размера периода, не больше 60 дней в архиве
            periodLength(period){
                const from  = new Date(period.dateFrom)
                const until  = new Date(period.dateUntil)
                return (until -  from)/(60 * 60 * 24 * 1000)
            },
            showWarningMessage(message){
                console.log(message)
            //    TODO Реализовать всплывающее окно о неправильном периоде
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
            formatDate(date){
                return new Date(date).toLocaleDateString()
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