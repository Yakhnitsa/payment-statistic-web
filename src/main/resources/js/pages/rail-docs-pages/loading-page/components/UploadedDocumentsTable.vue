<template>
    <table class="table table-striped table-hover">
        <thead class="sticky-header">
        <tr>
            <th>
                <label class="form-checkbox">
                    <input type="checkbox">
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
                    <input type="checkbox" :value="document" v-model="selectedDocuments">
                    <i class="form-icon"></i>
                </label>
            </td>
            <td  scope="col" class="text-center">
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
                documents:[]
            }
        },
        computed:{
            ...mapGetters({
                    uploadedDocuments: 'uploadStore/uploadedDocuments',
            }

            )
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