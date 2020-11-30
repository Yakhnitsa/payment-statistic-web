<template>
    <div class="position-relative">
        <div class="scrollable-table">
            <table class="table table-striped table-hover table-sm" id="certificatesTable">
                <thead>
                <tr>
                    <th class="text-center">Отправитель</th>
                    <th class="text-center">
                        <table-header-with-filter :filter.sync="sendStationFilter"
                                                  header="ст Отправления" inputType="search"
                                                  inputPlaceholder="Код, название">
                        </table-header-with-filter>
                    </th>
                    <th class="text-center">
                        <table-header-with-filter :filter.sync="dateFilter"
                                                  header="Дата" inputType="date" inputPlaceholder="">
                        </table-header-with-filter>
                    </th>
                    <th class="text-center">
                        <table-header-with-filter :filter.sync="docNumberFilter"
                                                  header="№ Документа" inputType="search"
                                                  inputPlaceholder="накладная">
                        </table-header-with-filter>
                    </th>
                    <th class="text-center">
                        <table-header-with-filter :filter.sync="vagNumberFilter"
                                                  header="№ Вагона" inputType="search"
                                                  inputPlaceholder="Вагон">
                        </table-header-with-filter>
                    </th>
                    <th class="text-center overfrow-visible">
                        <div class="dropdown">
                            Ку
                            <span class="text-secondary" id="certFilterDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-filter fa-sm"></i>
                            </span>
                            <div class="dropdown-menu" aria-labelledby="certFilterDropdown">
                                <a class="dropdown-item" >
                                    <input type="checkbox" v-model="certFilter.active">
                                    По сертификатам
                                </a>
                                <a class="dropdown-item" :class="{'disabled': !certFilter.active}">
                                    <input type="radio" v-model="certFilter.value" :value=true>
                                    <input type="radio" v-model="certFilter.value" :value=false>
                                    {{certFilter.value ? 'C сертификатом' : 'Без сертификата'}}
                                </a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" >
                                    <input type="checkbox" v-model="selectedFilter.active">
                                    По выбранным
                                </a>
                                <a class="dropdown-item" :class="{'disabled': !selectedFilter.active}">
                                    <input type="radio" v-model="selectedFilter.value" :value=true>
                                    <input type="radio" v-model="selectedFilter.value" :value=false>
                                    {{selectedFilter.value ? 'Выбранные' : 'Не выбранные'}}
                                </a>


                                <!--<a class="dropdown-item" :class="{'disabled' : !certSelectedFilter.active }" >-->
                                    <!--<input type="checkbox"-->
                                            <!--v-model="certSelectedFilter.value.selected">-->
                                    <!--{{certSelectedFilter.value.selected ? 'Отмеченные' :'Не отмеченные'}}</a>-->
                                <!--<a class="dropdown-item" :class="{'disabled' : !certSelectedFilter.active }" >-->
                                    <!--<input :disable="!certSelectedFilter.active"-->
                                            <!--type="checkbox"-->
                                            <!--v-model="certSelectedFilter.value.hasCert">-->
                                    <!--{{certSelectedFilter.value.hasCert ? 'С сертификатами' :'Без сертификатов'}}-->
                                <!--</a>-->
                            </div>
                        </div>
                    </th>
                    <th class="text-center">
                        <table-header-with-filter :filter.sync="receiveStationFilter"
                                                  header="ст Назначения" inputType="search"
                                                  inputPlaceholder="Код, название">
                        </table-header-with-filter>
                    </th>
                    <th class="text-center">Получатель</th>
                    <th class="text-center">Груз</th>
                    <th class="text-center">Тара</th>
                    <th class="text-center">Нетто</th>
                    <th class="text-center">Дополнительно</th>
                </tr>
                </thead>
                <tbody>
                    <template v-for="doc in documentsMainFilter(railroadDocuments)">
                        <tr :class="{'has-cert': hasCert(vagon)}" v-for="vagon in vagonFilter(doc.vagonList)">
                            <td class="text-capitalize">{{doc.cargoSender | formatClient}}</td>
                            <td class="text-capitalize">{{doc.sendStation | formatStation}}</td>
                            <td>{{doc.docDate | formatDate}}</td>
                            <td>{{doc.docNumber}}</td>
                            <td>{{vagon.number}}</td>
                            <td class="cert-checkbox">
                                <input
                                        type="checkbox"
                                        :value="vagon"
                                        v-model="selected">
                                <i v-show="hasCert(vagon)" class="far fa-check-square"></i>
                            </td>
                            <td class="text-capitalize">{{doc.receiveStation | formatStation}}</td>
                            <td class="text-capitalize">{{doc.cargoReceiver | formatClient}}</td>
                            <td>({{doc.cargoCode}}) {{doc.cargoName}}</td>
                            <td>{{vagon.tareWeight}}</td>
                            <td>{{vagon.netWeight}}</td>
                            <td>
                            </td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>

        <div class="btn-group float-buttons" role="group">


            <button class="btn btn-secondary"
                    data-toggle="tooltip" data-placement="left" title="Обновить данные на сервере"
                    @click="sendDataToServer">
                <span class="badge badge-light">{{changes.length}}</span>
                Обновить</button>
            <button class="btn btn-outline-secondary" @click="copyTableToClipboard"
                    data-toggle="tooltip" data-placement="left" title="Копировать в буфер обмена">
                <i class="fas fa-copy"></i>
            </button>
        </div>



    </div>
</template>

<script>

    import { createNamespacedHelpers } from 'vuex';
    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('certStore');
    import MesssageManager from '../../../../shared/services/messageManager'
    import TableHeaderWithFilter from "../../../../shared/components/TableHeaderWithFilter.vue";

    export default {
        name: "QualityCertTable",
        components: {TableHeaderWithFilter},
        data(){
            return {
                selected:[],
                sendStationFilter: {
                    active: false,
                    value: ''
                },
                receiveStationFilter:{
                    active: false,
                    value: ''
                },
                dateFilter: {
                    active: false,
                    value: ''
                },
                docNumberFilter:{
                    active: false,
                    value: ''
                },
                vagNumberFilter: {
                    active: false,
                    value: ''
                },
                certFilter:{
                    active: false,
                    value: false
                },
                selectedFilter:{
                    active: false,
                    value: false
                }

            }
        },
        // props:['railroadDocuments'],
        computed:{
            ...mapGetters({
                railroadDocuments: 'documents',
                changes: 'changesList'
            })
        },

        methods:{
            ...mapMutations(['setChangesListMutation']),
            ...mapActions(['uploadChangesToServerAction']),

            sendDataToServer(){
                this.selected = [];

                this.uploadChangesToServerAction();
            },

            isSelected(vagon){
                return this.selected.findIndex(item => item.id === vagon.id) !== -1;
            },

            hasCert(vagon){
                return vagon.vagonInfo ? vagon.vagonInfo.hasCert : false;
            },

            copyTableToClipboard(){
                this.removeSelection();

                let urlField = document.getElementById('certificatesTable');
                let range = document.createRange();
                range.selectNode(urlField);
                window.getSelection().addRange(range);
                document.execCommand('copy');
                MesssageManager.showInfoMessage("Данные скопированы в буфер обмена");

                this.removeSelection();
            },

            removeSelection(){
                if (window.getSelection) {
                    if (window.getSelection().empty) {  // Chrome
                        window.getSelection().empty();
                    } else if (window.getSelection().removeAllRanges) {  // Firefox
                        window.getSelection().removeAllRanges();
                    }
                } else if (document.selection) {  // IE?
                    document.selection.empty();
                }
            },

            mapArrayToChanges(selected){
                return selected.map(vagon =>{

                    const changes = {
                        hasCert: vagon.vagonInfo ? !vagon.vagonInfo.hasCert : true
                    };
                    return { vagonId: vagon.id, changes};
                })
            },


        //    Array filtering methods

            vagonFilter(vagons){
                vagons = this.vagonNumberFilterFunc(vagons);
                vagons = this.selectedFilterFunc(vagons);
                return this.hasCertFilterFunc(vagons);
            },

            vagonNumberFilterFunc(vagons){
                const val = this.vagNumberFilter.value;
                if(val === '' || !this.vagNumberFilter.active) return vagons;
                return vagons.filter(vagon => {
                    return vagon.number
                        .toString()
                        .indexOf(val) > -1;
                });
            },

            hasCertFilterFunc(vagons){
                const selected = this.certFilter.value;
                if(!this.certFilter.active) return vagons;
                return vagons.filter(vagon => {
                    return this.hasCert(vagon) === selected;
                });
            },
            selectedFilterFunc(vagons){
                if(!this.selectedFilter.active) return vagons;
                return vagons.filter(vagon => {
                    return this.isSelected(vagon) === this.selectedFilter.value;
                });
            },

            documentsMainFilter(documents){
                documents = this.docDateFilterFunc(documents);
                documents = this.docNumberFilterFunc(documents);
                documents = this.sendStationFilterFunc(documents);
                return this.receiveStationFilterFunc(documents);
            },

            docDateFilterFunc(documents){
                const date = this.dateFilter.value;
                if(date === '' || !this.dateFilter.active) return documents;
                return documents.filter(doc =>{
                        return this.$options.filters.formatDate(doc.docDate)
                            === this.$options.filters.formatDate(date);
                    }

                )
            },

            sendStationFilterFunc(documents){
                const val = this.sendStationFilter.value.toLowerCase();
                if(val===''|| !this.sendStationFilter.active) return documents;
                return documents.filter(doc => {
                    return doc.sendStation.code.toString().indexOf(val) > -1 ||
                        doc.sendStation.rusName.toLowerCase().indexOf(val) > -1 ||
                        doc.sendStation.ukrName.toLowerCase().indexOf(val) > -1;
                })
            },

            receiveStationFilterFunc(documents){
                const val = this.receiveStationFilter.value.toLowerCase();
                if(val===''|| !this.receiveStationFilter.active) return documents;
                return documents.filter(doc => {
                    return doc.receiveStation.code.toString().indexOf(val) > -1 ||
                        doc.receiveStation.rusName.toLowerCase().indexOf(val) > -1 ||
                        doc.receiveStation.ukrName.toLowerCase().indexOf(val) > -1;
                })
            },

            docNumberFilterFunc(documents){
                const docNumb = this.docNumberFilter.value;
                if(docNumb ==='' || !this.docNumberFilter.active) return documents;
                return documents
                    .filter(doc => doc.docNumber.toString().indexOf(docNumb) > -1
                    )
            },

        },
        watch:{
            selected(){
                const changesList = this.mapArrayToChanges(this.selected);
                this.setChangesListMutation(changesList);
            }
        },

        filters:{
            formatStation(station){
                return '(' + station.code + ') ' + station.rusName.toLowerCase();
            },
            formatClient(client){
                return '(' + client.railroadCode + ') ' + client.name;
            }
        }
    }
</script>

<style scoped>

    @import "../../../../css/railroad-documents/tables-style.css";

    .form-control {
        display: inline-block;
        width: 8.5em;
        height: 2em;
        padding: inherit;
        font-size: inherit;
    }
    .filter-block{
        color: #636363;
    }
    .has-cert{
        font-style: italic;
        color: gray;
        background-color: #cfe1e1 !important;
    }

    .float-buttons {
        opacity: .8;
        position: absolute;
        bottom: 30px;
        right: 30px;
    }
    .float-buttons:hover{
        opacity: 1;
    }

    .cert-checkbox input{
        width: 1em;
        height: 1em;
    }
    .link-button{
        padding: .1rem .35rem;
        font-size: .7rem;
        line-height: 1.5;
        border-radius: 50%!important;
    }
    .overfrow-visible{
        overflow: visible!important;
    }

    .dropdown-item {
        padding: .15rem .5rem;
    }

    .dropdown-divider {
        margin: .2rem 0;
    }
    .custom-control-input {
        position: inherit;
        margin-left: 1em;
    }


</style>