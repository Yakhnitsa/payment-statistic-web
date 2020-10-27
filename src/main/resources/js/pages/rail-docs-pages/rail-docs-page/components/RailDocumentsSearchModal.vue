<template>
    <transition name="modal-fade">
        <div class="modal-backdrop">
            <div class="modal bd-example-modal-lg" tabindex="-1" role="dialog">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Поиск ЖД накладных</h5>
                            <button type="button" class="close" @click="closeModal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form @submit="checkAndSubmit">
                                <div class="form-row">
                                    <div class="col-md-6">
                                        <label>ст Отправления ({{stationFrom.code}})</label>
                                        <station-input :station.sync="stationFrom"
                                                       :stations="stations"
                                                       :input-class="'form-control'"
                                        ></station-input>
                                    </div>
                                    <div class="col-md-6">
                                        <label>ст Назначения ({{stationTo.code}})</label>
                                        <station-input :station.sync="stationTo"
                                                       :stations="stations"
                                                       :input-class="'form-control'"
                                        ></station-input>
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="docNumberInput" class="col-sm-3 col-form-label text-nowrap">№
                                        накладной</label>
                                    <div class="col-sm-3">
                                        <input v-model="docNumber" type="number" class="form-control"
                                               id="docNumberInput" placeholder="№ ЖД накладной">
                                    </div>
                                    <label for="vagonNumberInput" class="col-sm-3 col-form-label text-nowrap">№
                                        вагона</label>
                                    <div class="col-sm-3">
                                        <input v-model="vagonNumber" type="number" class="form-control"
                                               id="vagonNumberInput" placeholder="№ вагона 8 знач">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="cargoSenderInput" class="col-sm-3 col-form-label text-nowrap">Отправитель</label>
                                    <div class="col-sm-3">
                                        <input v-model="cargoSenderCode" type="number" class="form-control"
                                               id="cargoSenderInput" placeholder="код 4 знач.">
                                    </div>
                                    <label for="cargoReceiverInput" class="col-sm-3 col-form-label text-nowrap">Получатель</label>
                                    <div class="col-sm-3">
                                        <input v-model="cargoReceiverCode" type="number" class="form-control"
                                               id="cargoReceiverInput" placeholder="код 4 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="tarifPayerInput" class="col-sm-4 col-form-label text-nowrap">Код
                                        плательщика</label>
                                    <div class="col-sm-4">
                                        <input v-model="tarifPayerCode" type="number" class="form-control"
                                               id="tarifPayerInput" placeholder="7 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="cargoCodeInput" class="col-sm-4 col-form-label text-nowrap">Код
                                        груза</label>
                                    <div class="col-sm-4">
                                        <input v-model="cargoCode" type="number" class="form-control"
                                               id="cargoCodeInput" placeholder="6 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="row">
                                    <div class="form-group col-sm-4 my-auto">
                                        Дата документа
                                        <!--<input type="date" v-model="dateFrom" class="form-control" id="inputDateFrom">-->
                                    </div>
                                    <div class="form-group col-sm-4 my-auto">
                                        <input type="date" v-model="dateFrom" class="form-control" id="inputDateFrom">
                                    </div>
                                    <div class="form-group col-sm-4 my-auto">
                                        <input type="date" v-model="dateUntil" class="form-control" id="inputDateUntil">
                                    </div>
                                </div>


                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" @click="updateData">Применить</button>
                            <button type="button" class="btn btn-primary" @click="clearAllData">Очистить форму</button>
                            <button type="button" class="btn btn-secondary" @click="closeModal">Закрыть</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </transition>
    <!-- Button trigger modal -->


</template>

<script>
    import StationInput from "../../../../shared/components/StationInput.vue";

    import {mapGetters, mapMutations, mapActions } from 'vuex';

    export default {
        name: "RailDocumentsSearchModal",
        components: {StationInput},
        props: ['show-modal'],
        data() {
            return {
                stationFrom: {code: '', rusName: '', ukrName: ''},
                stationTo: {code: '', rusName: '', ukrName: ''},
                cargoSenderCode: '',
                cargoReceiverCode: '',
                tarifPayerCode: '',
                dateFrom: '',
                dateUntil: '',
                docNumber: '',
                vagonNumber: '',
                cargoCode: ''
            }
        },
        computed: {
            ...mapGetters({
                storedParams: 'railDocsStore/storedRequestParams',
                stations: 'commonStore/stations',

            }),
        },
        methods: {
            ...mapMutations({
                clearRequestParams: 'railDocsStore/clearRequestParams',
                storeRequestParams:'railDocsStore/setRequestParamsMutation'
            }),
            ...mapActions({
                fetchDataFromServer: 'railDocsStore/fetchRailroadDocumentsAction'
            }),
            closeModal() {
                this.$emit('close-modal');
            },
            updateData() {
                const requestParams = {
                    currentPage: 0,
                    stationFromCode: this.stationFrom.code,
                    stationToCode: this.stationTo.code,
                    cargoSenderCode: this.cargoSenderCode,
                    cargoReceiverCode: this.cargoReceiverCode,
                    tarifPayerCode: this.tarifPayerCode,
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    docNumber: this.docNumber,
                    vagonNumber: this.vagonNumber,
                    cargoCode: this.cargoCode,
                };
                this.storeRequestParams(requestParams);
                this.fetchDataFromServer();

            },
            clearAllData() {
                this.clearRequestParams();
                this.getParamsFromStore();
            },
            checkAndSubmit() {
                console.log('check and submit data')
            },

            getParamsFromStore() {
                this.stationFrom.code = this.storedParams.stationFromCode;
                this.stationTo.code = this.storedParams.stationToCode;
                this.cargoSenderCode = this.storedParams.cargoSenderCode;
                this.cargoReceiverCode = this.storedParams.cargoReceiverCode;
                this.tarifPayerCode = this.storedParams.tarifPayerCode;
                this.dateFrom = this.storedParams.dateFrom;
                this.dateUntil = this.storedParams.dateUntil;
                this.docNumber = this.storedParams.docNumber;
                this.vagonNumber = this.storedParams.vagonNumber;
                this.cargoCode = this.storedParams.cargoCode;
            }
        },
        mounted() {
            this.getParamsFromStore();
        },

    }
</script>

<style scoped>

    .modal-backdrop {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background-color: rgba(0, 0, 0, 0.3);
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .modal {
        display: flex;
        flex-direction: column;
    }

    .modal-fade-enter,
    .modal-fade-leave-active {
        opacity: 0;
    }

    .modal-fade-enter-active,
    .modal-fade-leave-active {
        transition: opacity .3s ease
    }

    .separator {
        margin-top: .5rem;
        margin-bottom: .5rem;
    }
</style>