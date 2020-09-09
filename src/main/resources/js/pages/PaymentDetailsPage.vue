<template>
    <div class="container mx-1">
        <div class="container my-2">
            <div class="my-2">
                <button class="btn btn-secondary" type="button" data-toggle="collapse" data-target="#requestForm"
                        aria-expanded="true" aria-controls="requestForm">
                    Отобразить форму
                </button>

                <button class="btn btn-outline-secondary" @click="submitForm()">Обновить данные</button>


            </div>

            <div>
                <form>

                    <div class="collapse show my-2" id="requestForm">
                        <div class="form-row">
                            <div class="form-group col-lg-2">
                                <label for="inputPayerCode">Код плательщика</label>
                                <select v-model="payerCode" class="form-control form-control-sm" id="inputPayerCode">
                                    <option v-for="code in paymentCodes">{{code}}</option>
                                </select>
                            </div>
                            <div class="form-group col-lg-4">
                                <label for="inputPaymentType">Название платежа</label>
                                <input type="text" v-model="paymentType"
                                       class="form-control" id="inputPaymentType"
                                       list="paymentTypes"
                                       placeholder="Отправка, ведомости, прочее...">
                                <datalist id="paymentTypes">
                                    <option v-for="type in foundTypes" :value="type"></option>
                                </datalist>
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
                                <label for="inputStation">Станция ({{stationCode}})</label>
                                <station-input :input-class="'form-control'"
                                               :stationCode="stationCode"
                                               :station.sync="station"
                                               :stations="stations"
                                ></station-input>
                                <!--<input type="text" v-model="stationCode" class="form-control" id="inputStation"-->
                                       <!--placeholder="(код) Станция">-->
                            </div>
                            <div class="form-group col-md-3">
                                <label for="inputDocNumber">Документ</label>
                                <input type="number" v-model="docNumber" class="form-control" id="inputDocNumber"
                                       placeholder="№ документа">
                            </div>
                            <div class="form-group col-md-3">
                                <label for="inputPaymentSum">Сумма платежа</label>
                                <input type="number" v-model="paymentSum" class="form-control" id="inputPaymentSum"
                                       placeholder="Сумма">
                            </div>
                        </div>
                    </div>

                </form>
            </div>

        </div>

        <div class="float-right mr-4">
            <div class="row">
                <!--<span>Страница</span>-->
                <nav aria-label="Page nav ">
                    <ul class="pagination pagination-sm">
                        <li class="page-item"
                            :class="{disabled: currentPage <= 1}">
                            <a @click="currentPage--" class="page-link"><i class="fas fa-caret-left"></i></a>
                        </li>
                        <li>
                            <select
                                    v-model="currentPage"
                                    class="form-control form-control-sm" id="inputPageSelect">
                                <option
                                        :value="page"
                                        v-for="page in pages">{{page}}</option>
                            </select>
                        </li>
                        <li class="page-item"
                            :class="{disabled: currentPage >= pages}"
                        >
                            <a @click="currentPage++"class="page-link"><i class="fas fa-caret-right"></i></a>
                        </li>
                    </ul>
                </nav>

            </div>


        </div>

        <payment-details-table></payment-details-table>
    </div>


</template>

<script>
    import PaymentDetailsRequestForm from "../components/PaymentDetailsRequestForm.vue";
    import PaymentDetailsTable from "../components/PaymentDetailsTable.vue";
    import paymentDetailsApi from "../api/paymentDetailsApi"
    import StationInput from "../components/StationInput.vue";

    export default {
        name: "PaymentDetailsPage",
        props:['redirectParams'],
        components: {StationInput, PaymentDetailsTable, PaymentDetailsRequestForm},
        data() {
            return {
                station:{code:'',rusName:'',ukrName:''},
                payerCode: '',
                paymentType: '',
                dateFrom: '',
                dateUntil: '',
                currentPage: 1,
                docNumber: '',
                paymentSum: '',
                paymentTypes: []
            }
        },
        computed:{
            paymentCodes(){
                return paymentCodes;
            },
            pages(){
                return this.$store.state.paymentDetailsPage.totalPages;
            },
            stationCode:{
                get(){
                    return this.station ? this.station.code : undefined;
                },
                set(value){
                    this.station.code = value;
                }

            },
            stations(){
                return this.$store.getters.stations
            },
            foundTypes(){
                const regexp = RegExp(this.paymentType, "i");
                const filteredArray = this.paymentTypes.filter(type => regexp.test(type));
                if(filteredArray.length > 6) return []
                else if(filteredArray.length === 1){
                    this.paymentType = filteredArray[0]
                    return []
                }
                return filteredArray

            }
        },
        methods:{
            submitForm() {
                const params = {
                    payerCode: this.payerCode,
                    paymentType: this.paymentType,
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    pageNumber: this.currentPage -1,
                    stationCode: this.stationCode,
                    docNumber: this.docNumber,
                    paymentSum: this.paymentSum,
                };
                this.$store.dispatch('getPaymentDetailsAction',params);
            },
        },
        watch:{
            currentPage(){
                this.submitForm()
            }
        },
        mounted(){
            if(this.redirectParams){
                for (const [key, value] of Object.entries(this.redirectParams)) {
                    this[key] = value;
                }
                this.submitForm();
            }
            paymentDetailsApi.getPaymentTypes().then(response =>{
                if(response.status === 200){
                    this.paymentTypes = response.data
                }
            })

        }

    }
</script>

<style scoped>

</style>