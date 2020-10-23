<template>
    <transition name="modal-fade">
        <div class="modal-backdrop">
            <div class="modal bd-example-modal-lg" tabindex="-1" role="dialog">
                <div class="modal-dialog modal-lg" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Фильтр ЖД накладных</h5>
                            <button type="button" class="close" @click="closeModal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form>
                                <div class="form-row">
                                    <label for="cargoSenderInput" class="col-sm-3 col-form-label text-nowrap">Отправитель</label>
                                    <div class="col-sm-3">
                                        <input type="number" class="form-control" id="cargoSenderInput" placeholder="код 4 знач.">
                                    </div>
                                    <label for="cargoReceiverInput" class="col-sm-3 col-form-label text-nowrap">Получатель</label>
                                    <div class="col-sm-3">
                                        <input type="number" class="form-control" id="cargoReceiverInput" placeholder="код 4 знач.">
                                        <div class="invalid-feedback">
                                            Please choose a username.
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="col-md-4">
                                        <label for="validationDefault01">ст Отправления ({{stationFrom.code}})</label>
                                        <station-input :station.sync="stationFrom"
                                                       :stations="stations"
                                                       :input-class="'form-control'"
                                        ></station-input>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="validationDefault02">ст Назначения ({{stationTo.code}})</label>
                                        <station-input :station.sync="stationTo"
                                                       :stations="stations"
                                                       :input-class="'form-control'"
                                        ></station-input>
                                    </div>
                                    <div class="col-md-4 mb-3">
                                        <label for="validationDefaultUsername">Username</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text" id="inputGroupPrepend2">@</span>
                                            </div>
                                            <input type="text" class="form-control" id="validationDefaultUsername" placeholder="Username" aria-describedby="inputGroupPrepend2" required>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="col-md-6 mb-3">
                                        <label for="validationDefault03">City</label>
                                        <input type="text" class="form-control" id="validationDefault03" placeholder="City" required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="validationDefault04">State</label>
                                        <input type="text" class="form-control" id="validationDefault04" placeholder="State" required>
                                    </div>
                                    <div class="col-md-3 mb-3">
                                        <label for="validationDefault05">Zip</label>
                                        <input type="text" class="form-control" id="validationDefault05" placeholder="Zip" required>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary">Применить</button>
                            <button type="button" class="btn btn-primary">Очистить форму</button>
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

    import {mapGetters} from 'vuex';
    export default {
        name: "RailDocumentsSearchModal",
        components: {StationInput},
        props:['show-modal'],
        data(){
            return{
                stationFrom:{code:'',rusName:'',ukrName:''},
                stationTo:{code:'',rusName:'',ukrName:''},
                cargoSenderCode:'',
                cargoReceiverCode:'',
                tarifPayerCode:'',
                dateFrom:'',
                dateUntil:'',
                docNumber:''
            }
        },
        computed:{
            ...mapGetters({
                stations: 'commonStore/stations'
            }),
            // stations(){
            //     console.log(this.$store);
            //     return this.$store.getters.commonStore.stations;
            // }

        },
        methods:{
            closeModal(){
                this.$emit('close-modal');
            },
            updateData(){

            },
            clearAllData(){

            }
        },
        mounted(){

        }
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
</style>