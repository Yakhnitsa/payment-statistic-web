<template>
    <div class="position-relative">
        <div class="scrollable-table">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th class="text-center">Відправник</th>
                    <th class="text-center">
                        <span v-if="!sendStationFilter.active">
                            ст. Відправлення
                        </span>
                        <span v-else>
                             <input type="search" class="form-control"
                                    placeholder="ст Відправл..."
                                    v-model="sendStationFilter.value">
                        </span>
                        <span class="text-secondary" @click="sendStationFilter.active = !sendStationFilter.active">
                            <i :class="['fas', sendStationFilter.active ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
                        </span>
                    </th>
                    <th class="text-center">
                        <span v-if="!dateFilter.active">
                            Дата
                        </span>
                        <span v-else>
                             <input type="date" class="form-control"
                                    placeholder="Дата"
                                    v-model="dateFilter.value">
                        </span>
                        <span class="text-secondary" @click="dateFilter.active = !dateFilter.active">
                            <i :class="['fas', dateFilter.active ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
                        </span>
                    </th>


                    <th class="text-center">
                        <span v-if="!docNumberFilter.active">
                            № Накладної
                        </span>
                        <span v-else>
                             <input type="search" class="form-control"
                                    placeholder="№ Накладної"
                                    v-model="docNumberFilter.value">
                        </span>
                        <span class="text-secondary" @click="docNumberFilter.active = !docNumberFilter.active">
                            <i :class="['fas', docNumberFilter.active ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
                        </span>
                    </th>
                    <th class="text-center">
                        <span v-if="!vagNumberFilter.active">
                            № Вагону
                        </span>
                        <span v-else>
                             <input type="search" class="form-control"
                                     placeholder="№ Вагону"
                                     v-model="vagNumberFilter.value">
                        </span>
                        <span class="text-secondary" @click="vagNumberFilter.active = !vagNumberFilter.active">
                            <i :class="['fas', vagNumberFilter.active ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
                        </span>
                    </th>
                    <th class="text-center">[ ]</th>
                    <th class="text-center">ст Призначення</th>
                    <th class="text-center">Отримувач</th>
                    <th class="text-center">Вантаж</th>
                    <th class="text-center">Тара</th>
                    <th class="text-center">Нетто</th>
                    <th class="text-center">Додаткова інформація</th>
                </tr>
                </thead>
                <tbody>
                    <template v-for="doc in documentsMainFilter(railroadDocuments)">
                        <tr :class="{'changed': isChanged(vagon.id)}" v-for="vagon in vagonFilter(doc)">
                            <td class="text-capitalize">{{doc.cargoSender | formatClient}}</td>
                            <td class="text-capitalize">{{doc.sendStation | formatStation}}</td>
                            <td>{{doc.docDate | formatDate}}</td>
                            <td>{{doc.docNumber}}</td>
                            <td>{{vagon.number}}</td>
                            <td class="cert-checkbox">
                                <input
                                        type="checkbox"
                                        :checked="hasCert(vagon)"
                                        @change="setCertSelected(vagon)"
                                >
                            </td>
                            <td class="text-capitalize">{{doc.receiveStation | formatStation}}</td>
                            <td class="text-capitalize">{{doc.cargoReceiver | formatClient}}</td>
                            <td>({{doc.cargoCode}}) {{doc.cargoName}}</td>
                            <td>{{vagon.tareWeight}}</td>
                            <td>{{vagon.netWeight}}</td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>

        <button class="btn btn-secondary submit-button" @click="sendDataToServer">{{changes.length}} Обновить</button>

    </div>
</template>

<script>

    import { createNamespacedHelpers } from 'vuex';
    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('certStore');
    export default {
        name: "QualityCertTable",
        data(){
            return {
                sendStationFilter: {
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
                certSelectedFilter:{
                    active: false,
                    value: ''
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
            ...mapMutations(['addChangesMutation']),
            ...mapActions(['uploadChangesToServerAction']),
            addChanges(event){
                console.log(event);
            },
            setCertSelected(vagon){
                const hasCert = vagon.vagonInfo ? !vagon.vagonInfo.hasCert : true;
                const vagonId = vagon.id;
                const changes = {
                    hasCert: hasCert
                };
                this.addChangesMutation({vagonId, changes});
            },
            sendDataToServer(){
                this.uploadChangesToServerAction();
            },

            isChanged(vagonId){
                return this.changes.findIndex(item => item.vagonId === vagonId) !== -1;
            },

            hasCert(vagon){
                return vagon.vagonInfo ? vagon.vagonInfo.hasCert : false;
            },


        //    Array filtering methods
            vagonFilter(document){
                const result = document.vagonList;
                const val = this.vagNumberFilter.value;
                if(val === '' || !this.vagNumberFilter.active) return result;
                return result.filter(vagon => {
                    return vagon.number
                        .toString()
                        .indexOf(val) > -1;
                });
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
            //TODO Проверить или это работает!!!
            certSelectedFilterFunc(vagons){
                const selected = this.certSelectedFilter.value;
                if(!this.certSelectedFilter.active) return vagons;
                return vagons.filter(vagon => {
                    return vagon.vagonInfo === null ? !selected : vagon.vagonInfo.hasCert === selected;
                });
            },


            documentsMainFilter(documents){
                documents = this.docDateFilterFunc(documents);
                documents = this.docNumberFilterFunc(documents);
                return this.sendStationFilterFunc(documents);
            },
            docDateFilterFunc(documents){
                const date = this.dateFilter.value;
                if(date === '' || !this.dateFilter.active) return documents;
                return documents.filter(doc =>{
                        return doc.docDate === date;
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
            docNumberFilterFunc(documents){
                const docNumb = this.docNumberFilter.value;
                if(docNumb ==='' || !this.docNumberFilter.active) return documents;
                return documents
                    .filter(doc => doc.docNumber.toString().indexOf(docNumb) > -1
                    )
            },


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

    /*:root{*/
        /*--header-bg-color: #d5e7e7;*/
    /*}*/

    .scrollable-table {
        max-width: 100%;
        max-height: 30em;
        overflow: scroll;
        position: relative;
    }

    table {
        font-size: .9em;
    }

    thead th {
        position: -webkit-sticky;
        position: sticky;
        top: 0;
        background-color: var(--header-bg-color);
        z-index: 1;
    }

    .sticky-first-column {
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        left: 0;
        min-width: 8em;
        max-width: 8em;
        white-space: nowrap;
        background-color: var(--header-bg-color);
    }
    th.sticky-first-column{
        z-index: 2;
    }

    .sticky-second-column {
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        left: 8em;
        max-width: 8em;
        white-space: nowrap;
        background-color: var(--header-bg-color);
    }
    th.sticky-second-column{
        z-index: 2;
    }

    td, th{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 15em;
    }

    td:hover{
        white-space: normal;
        font-weight: 500;
    }

    .submit-button{
        opacity: .7;
        position: absolute;
        bottom: 30px;
        right: 30px;
    }
    .submit-button:hover{
        opacity: 1;
    }
    .cert-checkbox input{
        width: 1em;
        height: 1em;
    }
    .changed{
        font-style: italic;
        color: gray;
        background-color: #cfe1e1 !important;
    }

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


</style>