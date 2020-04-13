<template>
    <div>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loadingPage">
            Загрузить перечни
        </button>
        <!-- Modal -->
        <div class="modal fade" id="loadingPage" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="false">
            <div class="modal-dialog modal-lg" role="dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalLabel">Загрузка перечней</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <!--Таблица загруженных перечней во временную ДБ-->
                        <uploaded-list-table :payments="loadedPayments"
                            v-on:change-selected="changeSelected($event)"
                        ></uploaded-list-table>
                        <ul>
                            <li v-for="file in files">{{file.name}}</li>
                        </ul>

                    </div>
                    <div class="modal-footer">
                        <div class="form-row">
                            <div class="col">
                                <input type="file" ref="file" id="customFile"
                                       v-on:change="addFile($event)"
                                       multiple
                                       class="custom-file-input"
                                       enctype="multipart/form-data">
                                <label class="custom-file-label" for="customFile">{{chosenFile}}</label>
                            </div>
                        </div>
                        <button type="button" class="btn btn-secondary" @click="submitFileUpload()">Отправить на сервер</button>
                        <button type="button" class="btn btn-primary" @click="saveSelected()">Сохранить перечни в БД</button>
                        <button type="button" class="btn btn-primary" @click="deleteSelected()">Удалить выбранные</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

</template>

<script>
    import axios from 'axios'
    import UploadedListTable from './UploadedListTable.vue'
    import { mapGetters } from 'vuex';
    import { mapMutations } from 'vuex';
    import { mapActions } from 'vuex';

    export default {
        name: "LoadingWindow",
        components:{
            UploadedListTable
        },
        computed:{
            ...mapGetters({
                files: 'chosenFiles',
                loadedPayments: 'tempUploadedLists',
            })
        },
        data: function(){
            return{
                selectedPayments:[],
                chosenFile:'no file'
            }
        },
        methods:{
            ...mapMutations(['addChosenFilesMutation']),
            ...mapActions(['uploadListsOnServerAction']),
            addFile(event){
                var selectedFiles = event.target.files;
                this.addChosenFilesMutation(Array.from(selectedFiles))
            },
            submitFileUpload(){
                console.log('uploading files....')
                this.uploadListsOnServerAction();

            },
            saveSelected(){
                // axios.post('/api/save-temp-selected',
                //     this.selectedPayments, {
                //     }
                // ).then(response =>{
                //     if(response.status == 200){
                //         console.log(response)
                //         this.loadedPayments = []
                //         response.data.forEach(list => this.loadedPayments.push(list))
                //
                //         this.$emit('update-list')
                //     }
                // }).catch((error) => console.log(error));
            },
            deleteSelected(){
                // axios.post('/api/delete-temp-selected',this.selectedPayments)
                //     .then(response =>{
                //         if(response.status ==200){
                //             console.log(response)
                //             this.loadedPayments = []
                //             response.data.forEach(list => this.loadedPayments.push(list))
                //      }
                // }).catch(error => console.log(error))
            },
            changeSelected(selected){
                this.selectedPayments = selected;
            }
        },
        created: function(){
        },
        mounted: function(){
            // axios.get('/api/download-temp')
            //     .then(response =>{
            //     if(response.status == 200){
            //         this.loadedPayments = response.data;
            //     }
            //
            // }).catch((error) => console.log(error));
        }

    }
</script>

<style scoped>

</style>