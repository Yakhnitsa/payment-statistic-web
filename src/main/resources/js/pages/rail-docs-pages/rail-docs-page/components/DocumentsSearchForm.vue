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
                            <form @submit="updateData">
                                <div class="form-row">
                                    <div class="col-md-6">
                                        <label>ст Отправления ({{stationFrom.code}})</label>
                                        <station-input :station.sync="stationFrom"
                                                       :stationCode="searchParams.stationFromCode"
                                                       :stations="stations"
                                                       :input-class="'form-control'"
                                        ></station-input>
                                    </div>
                                    <div class="col-md-6">
                                        <label>ст Назначения ({{stationTo.code}})</label>
                                        <station-input :station.sync="stationTo"
                                                       :stationCode="searchParams.stationToCode"
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
                                        <input v-model="searchParams.docNumber" type="number" class="form-control"
                                               id="docNumberInput" placeholder="№ ЖД накладной">
                                    </div>
                                    <label for="vagonNumberInput" class="col-sm-3 col-form-label text-nowrap">№
                                        вагона</label>
                                    <div class="col-sm-3">
                                        <input v-model="searchParams.vagonNumber" type="number" class="form-control"
                                               id="vagonNumberInput" placeholder="№ вагона 8 знач">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="cargoSenderInput" class="col-sm-3 col-form-label text-nowrap">Отправитель</label>
                                    <div class="col-sm-3">
                                        <input v-model="searchParams.cargoSenderCode" type="number" class="form-control"
                                               id="cargoSenderInput" placeholder="код 4 знач.">
                                    </div>
                                    <label for="cargoReceiverInput" class="col-sm-3 col-form-label text-nowrap">Получатель</label>
                                    <div class="col-sm-3">
                                        <input v-model="searchParams.cargoReceiverCode" type="number" class="form-control"
                                               id="cargoReceiverInput" placeholder="код 4 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="tarifPayerInput" class="col-sm-4 col-form-label text-nowrap">Код
                                        плательщика</label>
                                    <div class="col-sm-4">
                                        <input v-model="searchParams.tarifPayerCode" type="number" class="form-control"
                                               id="tarifPayerInput" placeholder="7 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="form-row">
                                    <label for="cargoCodeInput" class="col-sm-4 col-form-label text-nowrap">Код
                                        груза</label>
                                    <div class="col-sm-4">
                                        <input v-model="searchParams.cargoCode" type="number" class="form-control"
                                               id="cargoCodeInput" placeholder="6 знач.">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="row">
                                    <div class="form-group col-sm-4 my-auto">
                                        Дата документа
                                        <!--<input type="date" v-model="dateFrom" class="form-control" id="inputDateFrom">-->
                                    </div>
                                    <div class="form-group col-sm-4 my-auto mt-1">
                                        <input type="date" v-model="searchParams.dateFrom" class="form-control" id="inputDateFrom">
                                    </div>
                                    <div class="form-group col-sm-4 my-auto mt-1">
                                        <input type="date" v-model="searchParams.dateUntil" class="form-control" id="inputDateUntil">
                                    </div>
                                </div>
                                <hr class="separator">
                                <div class="row">
                                    <label for="itemsCountInput" class="col-md-8 col-lg-4 col-form-label text-nowrap">
                                        Элементов на странице
                                    </label>
                                    <div class="col-md-4 col-lg-2">
                                        <select v-model="searchParams.itemsPerPage" class="form-control" id="itemsCountInput">
                                            <option>20</option>
                                            <option>50</option>
                                            <option>100</option>
                                            <option>200</option>
                                            <option>500</option>
                                            <option>1000</option>
                                        </select>

                                    </div>
                                    <label class="col-md-6 col-lg-2 col-form-label text-nowrap">Сортировка</label>
                                    <div class="col-md-6 col-lg-4 mt-1">
                                        <div class="btn-group btn-group-toggle">
                                            <label class="btn btn-outline-secondary"
                                                   :class="{'active': searchParams.sortDirection === 'desc'}" >
                                                <input type="radio" value="desc"

                                                       v-model="searchParams.sortDirection">
                                                Обратная
                                            </label>
                                            <label class="btn btn-outline-secondary"
                                                   :class="{'active': searchParams.sortDirection === 'asc'}">
                                                <input type="radio" value="asc"

                                                       v-model="searchParams.sortDirection">
                                                Прямая
                                            </label>
                                        </div>
                                    </div>

                                </div>


                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" @click="updateData">Применить</button>
                            <button type="button" class="btn btn-primary" @click="clearSearchParams">Очистить форму</button>
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
        name: "DocumentsSearchForm",
        components: {StationInput},
        props: ['show-modal'],
        data() {
            return {
                searchParams:{
                    stationFromCode:'',
                    stationToCode:'',
                    cargoSenderCode: '',
                    cargoReceiverCode: '',
                    tarifPayerCode: '',
                    dateFrom: '',
                    dateUntil: '',
                    docNumber: '',
                    vagonNumber: '',
                    cargoCode: '',
                    itemsPerPage:'50',
                    sortDirection:'desc',
                    sortBy:'docDate'
                },
                stationFrom: {code: '', rusName: '', ukrName: ''},
                stationTo: {code: '', rusName: '', ukrName: ''},
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
                setCurrentPage: 'railDocsStore/setCurrentPageMutation',
                storeRequestParams:'railDocsStore/setRequestParamsMutation'
            }),
            ...mapActions({
                fetchDataFromServer: 'railDocsStore/fetchRailroadDocumentsAction'
            }),
            closeModal() {
                this.$emit('close-modal');
            },
            updateData() {
                this.setCurrentPage(0);
                this.storeRequestParams(this.searchParams);
                this.fetchDataFromServer();

            },
            clearSearchParams() {
                for(const[key,value] of Object.entries(this.searchParams)){
                    this.searchParams[key] = '';
                }
                this.getParamsFromStore();
            },


            getParamsFromStore() {
                for (const [key, value] of Object.entries(this.storedParams)) {
                    this.searchParams[key] = value;
                }
            }
        },
        watch:{
            stationFrom(){
                this.searchParams.stationFromCode = this.stationFrom.code;
            },
            stationTo(){
                this.searchParams.stationToCode = this.stationTo.code;
            }
        },
        created() {
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
        overflow-y: auto;
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