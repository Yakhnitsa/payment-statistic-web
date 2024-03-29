<template>
    <div class="scrollable-table">
        <table class="table table-striped table-hover table-sm">
            <thead>
            <tr>
                <th class="sticky-checkbox">
                    <input type="checkbox" v-model="allSelected">
                </th>
                <th class="sticky-first-column text-center">

                    <table-header-with-filter :filter.sync="docNumberFilter"
                        header="№ документа" inputType="search" inputPlaceholder="№ документа">
                    </table-header-with-filter>
                </th>
                <th class="sticky-second-column text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="docDate" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="dateFilter"
                         header="Дата" inputType="date" inputPlaceholder="">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="sendStation" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="sendStationFilter"
                          header="ст Отправления" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="receiveStation" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="receiveStationFilter"
                        header="ст Назначения" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="cargoSender" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="cargoSenderFilter"
                        header="Отправитель" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="cargoReceiver" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="cargoReceiverFilter"
                        header="Получатель" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="tarifPayer" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="tarifPayerFilter"
                         header="Плательщик" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">
                    <sort-icon class="mr-1" @change-sorting="changeSorting"
                               sort-field="cargoCode" :sorting="sorting"/>
                    <table-header-with-filter :filter.sync="cargoNameFilter"
                        header="Груз" inputType="search" inputPlaceholder="код, название">
                    </table-header-with-filter>
                </th>
                <th class="text-center">К-ть вагонів</th>
                <th class="text-center">Маса вантажу</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="document in sortedDocs">
                <td class="sticky-checkbox">
                    <input type="checkbox"
                           :value="document" v-model="selectedDocuments">
                </td>
                <td class="sticky-first-column text-center">

                    <button class="show-on-hover btn btn-outline-secondary btn-sm link-button"
                          @click="downloadPdf(document)"
                         :class="{disabled : !docContainsPdf(document)}"
                          :disabled="!docContainsPdf(document)">
                        <i  class="fa fa-file-pdf"></i>
                    </button>
                    {{document.docNumber}}</td>
                <td class="sticky-second-column text-center">
                    {{document.docDate | formatDate}}
                    <span class="show-on-hover">{{document.docDate | formatTime }}</span>
                </td>
                <td class="text-capitalize">{{document.sendStation | formatStation}}</td>
                <td class="text-capitalize">{{document.receiveStation | formatStation}}</td>
                <td class="text-capitalize">{{document.cargoSender | formatClient}}</td>
                <td class="text-capitalize">{{document.cargoReceiver | formatClient}}</td>
                <td class="text-capitalize">{{document.tarifPayer | formatClient}}</td>
                <td class="text-capitalize">({{document.cargoCode}}) {{document.cargoName}}</td>
                <td class="text-right">{{document.vagonCount}}</td>
                <td class="text-right">{{document.fullWeight | formatWeight}}</td>
            </tr>
            </tbody>
        </table>

    </div>
</template>

<script>

    import {mapActions, mapMutations} from 'vuex';
    import TableHeaderWithFilter from "../../../../shared/components/TableHeaderWithFilter.vue";
    import SortIcon from "../../../../shared/components/SortIcon.vue";
    export default {
        name: "RailroadDocumentsTable",
        components: {SortIcon, TableHeaderWithFilter},
        props:['railroadDocuments'],
        data(){
            return{
                selectedDocuments:[],

                docNumberFilter:{
                    active: false,
                    value: ''
                },
                dateFilter: {
                    active: false,
                    value: ''
                },
                sendStationFilter: {
                    active: false,
                    value: ''
                },
                receiveStationFilter: {
                    active: false,
                    value: ''
                },
                cargoSenderFilter:{
                    active: false,
                    value: ''
                },
                cargoReceiverFilter:{
                    active: false,
                    value: ''
                },
                tarifPayerFilter:{
                    active: false,
                    value: ''
                },
                cargoNameFilter:{
                    active: false,
                    value: ''
                },
                sorting:{
                    asc: true,
                    field:''
                }
            }
        },
        computed:{
            filteredDocuments(){
                let documents = this.docNumberFilterFunc(this.railroadDocuments);
                documents = this.docDateFilterFunc(documents);
                documents = this.sendStationFilterFunc(documents);
                documents = this.receiveStationFilterFunc(documents);
                documents = this.cargoSenderFilterFunc(documents);
                documents = this.cargoReceiverFilterFunc(documents);
                documents = this.tarifPayerFilterFunc(documents);
                documents = this.cargoNameFilterFunc(documents);
                return documents;
            },

            sortedDocs(){
                if(this.sorting.field ==='') return this.filteredDocuments;
                return [...this.filteredDocuments].sort((a,b) =>{
                    let aField = a[this.sorting.field];
                    aField = aField.hasOwnProperty('code') ? aField.code : aField;
                    let bField = b[this.sorting.field];
                    bField = bField.hasOwnProperty('code') ? bField.code : bField;
                    let result = 0;
                    if(aField > bField) result = 1;
                    else if(aField < bField) result = -1;
                    return this.sorting.asc ? result : -result;
                });
            },
            allSelected:{
                get(){
                    return this.selectedDocuments.length > 0 ?
                        this.selectedDocuments.length === this.filteredDocuments.length : false;
                },
                set(selected){
                    this.clearSelectedDocuments();

                    if(selected){
                        this.filteredDocuments.forEach(doc => {
                            this.selectedDocuments.push(doc)
                        })
                    }
                }
            }
        },
        methods:{
            ...mapActions({
                downloadPdf: 'railDocsStore/downloadPdfFileAction',
                downloadXml: 'railDocsStore/downloadXmlFileAction'
            }),
            ...mapMutations({
                storeSelectedDocuments:'railDocsStore/setSelectedDocumentsMutation',
                storeFilteredDocuments:'railDocsStore/setFilteredDocumentsMutation',
            }),

            clearSelectedDocuments(){
                this.selectedDocuments = [];
            },

            docContainsPdf(railroadDocument){
                return railroadDocument.pdfBackupFilePath == null? false : railroadDocument.pdfBackupFilePath !== '';
            },
            changeSorting(field){
                if(this.sorting.field === field && this.sorting.asc){
                    this.sorting.asc = false;
                }
                else if(this.sorting.field === field && !this.sorting.asc){
                    this.sorting.field = '';
                    this.sorting.asc = true;
                }
                else{
                    this.sorting.asc = true;
                    this.sorting.field = field;
                }
            },
            /*
             * Filter methods
             */
            docNumberFilterFunc(documents){
                const docNumb = this.docNumberFilter.value;
                if(docNumb ==='' || !this.docNumberFilter.active)
                    return documents;

                return documents.filter(doc => doc.docNumber.toString().indexOf(docNumb) > -1)
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

            cargoSenderFilterFunc(documents){
                const val = this.cargoSenderFilter.value.toLowerCase();
                if(val===''|| !this.cargoSenderFilter.active) return documents;
                return documents.filter(doc => {
                    return doc.cargoSender.code.toString().indexOf(val) > -1 ||
                        doc.cargoSender.name.toLowerCase().indexOf(val) > -1
                })
            },

            cargoReceiverFilterFunc(documents){
                const val = this.cargoReceiverFilter.value.toLowerCase();
                if(val===''|| !this.cargoReceiverFilter.active) return documents;
                return documents.filter(doc => {
                    return doc.cargoReceiver.code.toString().indexOf(val) > -1 ||
                        doc.cargoReceiver.name.toLowerCase().indexOf(val) > -1
                })
            },

            tarifPayerFilterFunc(documents){
                const val = this.tarifPayerFilter.value.toLowerCase();
                if(val===''|| !this.tarifPayerFilter.active) return documents;
                return documents.filter(doc => {
                    return doc.tarifPayer.code.toString().indexOf(val) > -1 ||
                        doc.tarifPayer.name.toLowerCase().indexOf(val) > -1
                })
            },

            cargoNameFilterFunc(documents){
                const val = this.cargoNameFilter.value.toLowerCase();
                if(val===''|| !this.cargoNameFilter.active) return documents;
                return documents.filter(doc => {
                    let index = doc.cargoName.toLowerCase().indexOf(val);
                    return index > -1 ||
                        doc.cargoCode.indexOf(val) > -1
                })
            },

        },
        watch:{
            selectedDocuments(){
                this.storeSelectedDocuments(this.selectedDocuments)
            },
            filteredDocuments(){
                this.storeFilteredDocuments(this.filteredDocuments)
            }
        },
        filters:{
            formatStation(station){
                return '(' + station.code + ') ' + station.rusName.toLowerCase();
            },
            formatClient(client){
                return '(' + client.code + ') ' + client.name;
            }
        }
    }
</script>

<style>
    @import "../../../../css/railroad-documents/tables-style.css";

    .sticky-checkbox {
        left: 0;
        min-width: 2em;
        max-width: 2em;
    }
    .sticky-first-column {
        min-width: 9em;
        max-width: 9em;
        left: 2em;

    }
    .sticky-second-column {
        min-width: 9em;
        max-width: 11em;
        left: 11em;
    }
    .sticky-first-column, .sticky-second-column, .sticky-checkbox{
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        white-space: nowrap;
        background-color: var(--header-bg-color);
    }
    th.sticky-first-column, th.sticky-second-column, th.sticky-checkbox{
        z-index: 2;
    }




</style>