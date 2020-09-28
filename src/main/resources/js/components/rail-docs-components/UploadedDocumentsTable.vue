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
            <th>№ документа/Дата</th>

            <th>ст Отправления/Назначения</th>
            <th>Отправитель/получатель</th>
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
            <td  scope="col" class="col-2">
                {{document.docNumber}}
                <hr>
                {{document.dateStamp | formatDate}}
            </td>
            <td scope="col" class="col-4">
                {{document.sendStation | formatStation}}
                <hr>
                {{document.receiveStation | formatStation}}
            </td>
            <td scope="col" class="col-4">
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
        overflow: inherit;
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
        padding: .15rem;
        vertical-align: top;
        border-top: 1px solid #dee2e6;
    }
</style>