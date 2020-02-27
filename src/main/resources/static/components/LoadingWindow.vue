<template>
    <div>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loadingPage">
            Загрузить перечни
        </button>
        <!-- Modal -->
        <div class="modal fade" id="loadingPage" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="false">
            <div class="modal-dialog" role="document">
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
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        <button type="button" class="btn btn-primary" @click="saveSelected()">Сохранить перечни в БД</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

</template>

<script>
    import axios from 'axios'
    import UploadedListTable from '../components/UploadedListTable.vue'
    export default {
        name: "LoadingWindow",
        components:{
            UploadedListTable
        },
        data: function(){
            return{
                files:[],
                loadedPayments:[],
                selectedPayments:[],
                chosenFile:'no file'
            }
        },
        methods:{
            addFile(event){
                var selectedFiles = event.target.files;
                console.log("event.target.files:")
                console.log(event.target.files);
                Array.from(selectedFiles).forEach(file =>{
                    this.files.push(file);
                });
            },
            submitFileUpload(){
                var formData = new FormData();

                for(var index = 0; index < this.files.length; index++) {
                    formData.append("files", this.files[index]);
                }

                axios.post('/api/upload-multiple',
                    formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }

                    }
                ).then(response =>{
                    if(response.status == 200){
                        this.files = []
                        this.loadedPayments = [];
                        console.log(response)
                        response.data.forEach(list => this.loadedPayments.push(list))
                    }
                    else if(response.status == 204){
                        console.log("no content!");
                    }
                }).catch((error) => console.log(error));

            },
            saveSelected(){
                // var formData = new FormData();
                // this.selectedPayments.forEach(element =>{
                //     formData.append("selected", "" + element.number)
                // });

                axios.post('/api/save-selected',
                    this.selectedPayments, {
                        // headers: {
                        //     'Content-Type': 'multipart/form-data'
                        // }

                    }
                ).then(response =>{
                    if(response.status == 200){
                        console.log(response)
                    }

                }).catch((error) => console.log(error));
            },
            changeSelected(selected){
                this.selectedPayments = selected;
            }
        },
        created: function(){



        },
        mounted: function(){
            axios.get('/api/download-temp')
                .then(response =>{
                if(response.status == 200){
                    this.loadedPayments = response.data;
                    console.log(response)
                }

            }).catch((error) => console.log(error));
        }

    }
</script>

<style scoped>

</style>