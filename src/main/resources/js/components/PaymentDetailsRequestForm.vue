<template>
    <div class="container my-2">
        <div class="my-2">
            <button class="btn btn-secondary" type="button" data-toggle="collapse" data-target="#requestForm" aria-expanded="false"
                    aria-controls="requestForm">
                Отобразить форму
            </button>

            <button class="btn btn-outline-secondary" @click="submitForm()">Обновить данные</button>

            <div class="float-right mr-4">
                <nav aria-label="Page nav ">
                    <ul class="pagination pagination-sm">
                        <li class="page-item">
                            <a class="page-link"><i class="fas fa-caret-left"></i></a>
                        </li>
                        <li>
                            <select v-model="currentPage" class="form-control form-control-sm" id="inputPageSelect">
                                <option v-for="page in pages">{{page}}</option>
                            </select>
                        </li>
                        <li class="page-item">
                            <a class="page-link"><i class="fas fa-caret-right"></i></a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>

        <div>
            <form>

                <div class="collapse my-2" id="requestForm">
                    <div class="form-row">
                        <div class="form-group col-lg-2">
                            <label for="inputPayerCode">Код плательщика</label>
                            <select v-model="payerCode" class="form-control form-control-sm" id="inputPayerCode">
                                <option v-for="code in paymentCodes">{{code}}</option>
                            </select>
                        </div>
                        <div class="form-group col-lg-4">
                            <label for="inputPaymentType">Название платежа</label>
                            <input type="text" v-model="paymentType" class="form-control" id="inputPaymentType" placeholder="Отправка, ведомости, прочее...">
                        </div>
                        <div class="form-group col-lg-2">
                            <label for="inputDateFrom">Начало периода</label>
                            <input type="date" v-model="dateFrom" class="form-control" id="inputDateFrom">
                        </div>
                        <div class="form-group col-lg-2">
                            <label for="inputPassword4">Конец периода</label>
                            <input type="date" v-model="dateUntil" class="form-control" id="inputPassword4">
                        </div>
                    </div>
                    <div class="form-row">

                        <div class="form-group col-md-3">
                            <label for="inputStation">Станция</label>
                            <input type="text" v-model="station" class="form-control" id="inputStation" placeholder="(код) Станция">
                        </div>
                        <div class="form-group col-md-3">
                            <label for="inputDocNumber">Документ</label>
                            <input type="number" v-model="docNumber" class="form-control" id="inputDocNumber" placeholder="№ документа">
                        </div>
                        <div class="form-group col-md-3">
                            <label for="inputPaymentSum">Сумма платежа</label>
                            <input type="number" v-model="paymentSum" class="form-control" id="inputPaymentSum" placeholder="Сумма">
                        </div>
                    </div>
                </div>

            </form>
        </div>

    </div>

</template>

<script>
    export default {
        name: "PaymentDetailsRequestForm",

    //    TODO форма запроса списка перечней
        data(){
            return{
                payerCode:'',
                paymentType:'',
                dateFrom:'',
                dateUntil:'',
                currentPage:'',
                station:'',
                docNumber:'',
                paymentSum:''
            }
        },
        computed:{
            paymentCodes(){
                return paymentCodes;
            },
            pages(){
                return this.$store.state.paymentDetailsPage.totalPages
            }
        },
        methods:{
            submitForm(){

            }
        }
    }
</script>

<style scoped>

</style>