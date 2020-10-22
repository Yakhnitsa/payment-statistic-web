<template>
    <div class="container mx-1">
        <div class="ml-3">
            <loading-window></loading-window>
        </div>

        <div class="ml-3 my-2">
            <div class="form-row ">
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button class="btn btn-primary" v-on:click="updateList()">Обновить</button>
                </div>
                <div class="form-group col-md-3">
                    <button class="btn btn-secondary" @click="downloadArchive()">Архив за период
                        <i class="fas fa-file-archive"></i>
                    </button>
                </div>
            </div>
        </div>

        <div class="container">

            <table id="payments-table" class="table table-fixed table-sm table-striped">
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
                <tr class="text-right text-nowrap" v-for="payment in checkedPayments">
                    <td scope="col" class="col-2">{{payment.number}}</td>
                    <td scope="col" class="col-2">{{payment.date | formatDate}}</td>
                    <td scope="col" class="col-2"
                        :class="{'text-danger': !payment.checkedOpeningBalance}">
                        {{payment.openingBalance | formatPayment }}
                    </td>
                    <td scope="col" class="col-2"
                        :class="{'text-danger': !payment.checkedClosingBalace}">
                        {{payment.closingBalance | formatPayment}}</td>
                    <td scope="col" class="col-2">{{payment.paymentVsTaxes | formatPayment}}</td>
                    <td scope="col" class="col-2">
                        <button type="button" class="btn btn-secondary btn-sm" @click="showPayment(payment)">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button type="button"
                                :disabled="!hasEditorPermission"
                                data-toggle="tooltip" data-placement="bottom"
                                :title="hasEditorPermission ? 'Удалить запись' : 'Недостаточно прав для совершения действия'"
                                class="btn btn-secondary btn-sm" @click="deletePayment(payment)">
                            <i class="fas fa-trash-alt"></i>
                        </button>
                        <button type="button" class="btn btn-secondary btn-sm" @click="downloadPayment(payment)">
                            <i class="fas fa-file-download"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
            <pageable
                    @changePage="loadPage"
                    :total-pages="totalPages"
                    :current-page="currentPage"></pageable>
            <!--<nav aria-label="Page navigation example">-->
                <!--<ul class="pagination">-->
                    <!--<li class="page-item"-->
                        <!--:class="{disabled: currentPage < 1}">-->
                        <!--<a class="page-link" @click="loadPage(currentPage-1)"><i class="fas fa-caret-left"></i></a>-->
                    <!--</li>-->
                    <!--<li v-for="page in totalPages"-->
                        <!--class="page-item"-->
                        <!--:class="{active: page === currentPage + 1}"-->
                    <!--&gt;<a class="page-link" @click="loadPage(page-1)" >{{page}}</a></li>-->
                    <!--&lt;!&ndash;<li class="page-item"><a class="page-link">2</a></li>&ndash;&gt;-->
                    <!--&lt;!&ndash;<li class="page-item"><a class="page-link">3</a></li>&ndash;&gt;-->
                    <!--<li class="page-item"-->
                        <!--:class="{disabled: currentPage >= totalPages -1}">-->
                        <!--<a class="page-link" @click="loadPage(currentPage+1)"><i class="fas fa-caret-right"></i></a>-->
                    <!--</li>-->
                <!--</ul>-->
            <!--</nav>-->
        </div>
    </div>
</template>

<script>
    import {mapActions,mapState,mapGetters} from 'vuex'
    import LoadingWindow from "./components/LoadingWindow.vue";
    import Pageable from "../../../shared/components/Pageable.vue"
    export default {
        name: "PaymentListPage",
        components: {LoadingWindow, Pageable},
        data: function(){
            return{
                dateFrom:'',
                dateUntil:''
            }
        },
        computed: {
            ...mapGetters(['hasEditorPermission'],

            ),
            payments: function(){
                return this.$store.getters.paymentLists
            },
            sortedPayments: function(){
                return this.payments.sort((a,b)=> a.date - b.date)
            },
            // проверка перечней по логике конец предыдущего = начало текущего периода
            checkedPayments(){
                const resultList = [...this.sortedPayments]
                let i;
                for (i = 0; i < resultList.length; i++) {
                    let currentPl = resultList[i];
                    //    checkClosingBalance
                    if(i > 0){
                        let prevPl = resultList[i-1];
                        currentPl.checkedClosingBalace = currentPl.closingBalance === prevPl.openingBalance;
                    }else{
                        currentPl.checkedClosingBalace = true;
                    }
                    //    checkOpentingBalanct
                    if(i < resultList.length -2){
                        let nextPl = resultList[i+1];
                        currentPl.checkedOpeningBalance = currentPl.openingBalance === nextPl.closingBalance
                    }
                    else{
                        currentPl.checkedOpeningBalance = true;
                    }

                }

                return resultList;

            },
            currentPage(){
                return this.$store.state.paymentListPage.currentPage
            },
            totalPages(){
                return this.$store.state.paymentListPage.totalPages
            },
            payerCode(){
                return this.$store.state.payerCode
            },


        },
        watch:{
            payerCode(newVal){
                alert("Код плательщика изменен на: " + newVal);
                this.loadPage(this.currentPage);
            }
        },

        created: function(){
            this.dateFrom = this.$store.state.paymentListPage.dateFrom;
            this.dateUntil = this.$store.state.paymentListPage.dateUntil;
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
                    payerCode: this.payerCode
                };
                this.$store.commit('setPaymentListPeriod',params);
                this.getPaymentListsAction(params)
            },
            showPayment(payment){
                //
                // const params = {
                //     listNumber: payment.number,
                //     payerCode: payment.payerCode,
                // };
                //
                // paymentListApi.getSinglePayment(params)
                //     .then(function (response) {
                //         //    TODO Реализовать отображение перечня в окне
                //         console.log(response)
                //         console.log('SUCCESS!')
                //     })
                //     .catch((error) => console.log(error))
            },
            deletePayment(list){
                this.deletePaymentListAction(list)

            },
            downloadPayment(list){
                this.downloadPaymentListAction(list.backupFilePath)
            },
            test(list){
                // this.$store.commit('deletePaymentListMutation',paymentList)
            },
            downloadArchive(){
                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    payerCode: this.payerCode

                }
                console.log(params);

                const length = this.periodLength(params)
                if(Number.isNaN(length) || length > 60 || length < 1){
                    let message = 'Неверно выбранный период, период архива должен быль больше 0 и меньше 60 дней. ' +
                        'Запрашиваемый период: ' + length + ' дней.'
                    this.showWarningMessage(message)
                    return
                }
                this.$store.dispatch('downloadPaymentListsArchiveAction', params)
            },
            //Проверка размера периода, не больше 60 дней в архиве
            periodLength(period){
                const from  = new Date(period.dateFrom)
                const until  = new Date(period.dateUntil)
                return (until -  from)/(60 * 60 * 24 * 1000)
            },
            showWarningMessage(message){
                alert(message)
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

    .text-danger{
        font-weight: bold;
        font-style: italic;
    }
</style>