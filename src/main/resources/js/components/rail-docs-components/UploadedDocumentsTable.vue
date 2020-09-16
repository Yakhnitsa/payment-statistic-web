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
            <th>№ документа</th>
            <th>Дата</th>
            <th>Отправитель</th>
            <th>ст Отправления</th>
            <th>ст Назначения</th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="document in documents">
            <td>
                <label class="form-checkbox">
                    <input type="checkbox" :value="document" v-model="selectedDocuments">
                    <i class="form-icon"></i>
                </label>
            </td>
            <td>{{document.number}}</td>
            <td>{{document.dateStamp | formatDate}}</td>
            <td>{{document.sender}}</td>
            <td class="text-right">{{document.sendStation | formatStation}}</td>
            <td class="text-right">{{document.receiveStation | formatStation}}</td>
        </tr>
        </tbody>
    </table>
</template>

<script>
    export default {
        name: "UploadedDocumentsTable"
        data(){
            return{
                selectedDocuments:[],
                documents:[]
            }
        },
        filters:{
            formatStation(station){
                return '(' + station.code + ')' + station.rusName;
            }
        }
    }
</script>

<style scoped>

</style>