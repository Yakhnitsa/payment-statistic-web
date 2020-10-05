<template>
    <table class="table table-striped table-hover">
        <thead class="sticky-header">
        <tr>
            <th>
                <label class="form-checkbox">
                    <input type="checkbox"
                           v-model="allSelected"
                    >
                    <i class="form-icon"></i>
                </label>
            </th>
            <th class="text-center">№ Накладной</th>
            <th class="text-center">Дата</th>
            <th class="text-center">Маршрут</th>
            <th class="text-center">Отправитель/ получатель</th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="document in uploadedDocuments">
            <td  scope="col" class="col-2">
                <label class="form-checkbox">
                    <input type="checkbox"
                           :disabled="!isDocCorrect(document)"
                           :value="document" v-model="selectedDocuments">
                    <i class="form-icon"></i>
                </label>
                <span>{{document.docNumber !== -1}}</span>
                <span v-if="document.pdfBackupFile" class="far fa-file-pdf text-success"></span>
            </td>
            <td scope="col"
                :class="{'incorrect' : !isDocCorrect(document)}"
                class="text-center">
                {{document.docNumber}}
            </td>
            <td  scope="col" class="text-center small">
                {{document.dateStamp | formatDate}}
            </td>
            <td scope="col" class="small">
                {{document.sendStation | formatStation}}
                <hr>
                {{document.receiveStation | formatStation}}
            </td>
            <td scope="col" class="small">
                {{document.cargoSender | formatClient}}
                <hr>
                {{document.cargoReceiver | formatClient}}
            </td>

        </tr>
        </tbody>
    </table>
</template>

<script>
    import {mapMutations, mapActions, mapGetters} from 'vuex';

    export default {
        name: "UploadedDocumentsTable",
        data(){
            return{
                selectedDocuments:[],
            }
        },
        computed:{
            ...mapGetters({
                    uploadedDocuments: 'uploadStore/uploadedDocuments',
            }),
            allSelected:{
                get(){
                    return this.selectedDocuments.length > 0 ?
                        this.selectedDocuments.length=== this.uploadedDocuments.length : false;
                },
                set(selected){
                    this.clearSelectedDocs();

                    if(selected){
                        this.uploadedDocuments.forEach(doc => {
                            if(doc.number !== -1) this.selectedDocuments.push(doc)
                        })
                    }
                }
            }
        },
        methods:{
            isDocCorrect(document){
                return document.docNumber !== -1;
            },
            clearSelectedDocs(){
                console.log('selected docs cleared');
                this.selectedDocuments = [];
            }
        },
        watch:{
            selectedDocuments(val){
                this.$store.commit('uploadStore/setSelectedDocumentsMutation',val);
            }
        },
        filters:{
            formatStation(station){
                return '(' + station.code + ') ' + station.rusName;
            },
            formatClient(client){
                return '(' + client.railroadCode + ') ' + client.name;
            }
        },
        mounted(){
            this.$store.dispatch('uploadStore/fetchTempUploadedDocs');
        }
    }
</script>

<style scoped>
    .incorrect{
        text-decoration: line-through;
        font-style: italic;
        font-weight: bold;
        color: #eb2400;
    }
    td{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    td:hover {
        overflow: visible;
        text-overflow: inherit;
        white-space: inherit;
        word-wrap: inherit;
    }
    hr {
        margin-top: .1rem;
        margin-bottom: .1rem;
        border: 0;
        border-top: 1px solid rgba(0,0,0,.1);
    }
    .table td, .table th {
        padding: .15rem .3rem;
        vertical-align: middle;
        border-top: 1px solid #dee2e6;
        max-width: 280px;
    }
</style>