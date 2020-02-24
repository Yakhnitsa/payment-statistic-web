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
                        <p>{{loadedPayments}}</p>
                        <ul>
                            <li v-for="file in files">{{file.name}}</li>
                        </ul>

                    </div>
                    <div class="modal-footer">
                        <div class="form-row">
                            <div class="col">
                                <input type="file" ref="file" id="customFile"
                                       v-on:change="addFile($event)"
                                       class="custom-file-input"
                                       enctype="multipart/form-data">
                                <label class="custom-file-label" for="customFile">{{chosenFile}}</label>
                            </div>
                        </div>
                        <button type="button" class="btn btn-secondary" @click="submitFileUpload()">Отправить на сервер</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        <button type="button" class="btn btn-primary">Сохранить перечни в БД</button>
                    </div>
                </div>
            </div>
        </div>

    </div>

</template>

<script>
    import axios from 'axios'
    export default {
        name: "LoadingWindow",
        // props:['loadedPayments'],
        data: function(){
            return{
                files:[],
                loadedPayments:[],
                chosenFile:'no file'
            }
        },
        methods:{
            addFile(event){
                var file = event.target.files[0]
                this.file = file
                this.chosenFile = file.name
                this.files.push(file);
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
                ).then(function () {
                    console.log('SUCCESS!')
                })
                    .catch((error) => console.log(error))


            }
        }
    }
</script>

<style scoped>

</style>