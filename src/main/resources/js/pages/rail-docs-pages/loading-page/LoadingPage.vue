<template>
    <div class="container-fluid">


        <div class="row">
            <!--Окно файлов для загрузки файлов-->
            <div class="col-md-5">
                <div class="flex-container">
                    <div class="h5 text-center">Список файлов</div>
                    <div v-if="files.length > 0" class="flex-element">
                        <files-table></files-table>

                    </div>

                    <div v-else class="placeholder">
                        <h5 class="card-title">Нет данных для отображения</h5>
                        <p class="card-text">Загрузите ЖД накладные</p>
                    </div>

                    <div class="progress" v-show="showProgressBar">
                        <div class="progress-bar" role="progressbar"
                             :style="progressStyle" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
                    </div>

                    <div class="control-panel-md my-1">
                        <div class="input-group">
                            <div class="custom-file">
                                <input type="file" id="customFile"
                                       v-on:change="addFiles($event)"
                                       multiple
                                       class="custom-file-input"
                                       enctype="multipart/form-data">
                                <label class="custom-file-label" for="customFile">{{files.length}}</label>
                            </div>
                            <button type="button" class="btn btn-primary mx-1" @click="deleteSelectedFiles">
                                Удалить выбранные
                            </button>
                            <button type="button" class="btn btn-primary mx-1" @click="deleteAllFiles">
                                Удалить все
                            </button>

                            <button type="button" class="btn btn-primary mx-1" @click="uploadFiles">
                                Загрузить
                                <span v-if="selectedFiles.length > 0" class="badge badge-light">{{selectedFiles.length}}</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!--Окно загруженных накладных-->
            <div class="col-md-7">
                <div class="flex-container">
                    <div class="h5">Загруженные накладные</div>
                    <div class="flex-element">
                        <uploaded-documents-table ref="uploadDocs">

                        </uploaded-documents-table>

                    </div>
                    <div class="control-panel my-1">
                        <div class="input-group">
                            <button type="button" class="btn btn-primary mx-1" @click="saveSelected()">
                                Сохранить в БД
                            </button>

                            <button type="button" class="btn btn-primary mx-1" @click="deleteSelected()">
                                Удалить выбранные
                            </button>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>

</template>

<script>
    import FilesTable from "./components/FilesTable.vue";
    import UploadedDocumentsTable from "./components/UploadedDocumentsTable.vue";

    import {mapMutations, mapActions, mapGetters} from 'vuex';

    export default {
        name: "LoadingPage",
        components: {UploadedDocumentsTable, FilesTable},
        data() {
            return {

            }
        },
        computed: {
            ...mapGetters({
                    files: 'uploadStore/files',
                uploadedDocuments: 'uploadStore/uploadedDocuments',
                selectedFiles:'uploadStore/selectedFiles',
                loadingProgress:'uploadStore/onUploadProgress',
                hasEditorPermission:'commonStore/hasEditorPermission'
            }),
            progressStyle(){
                return 'width: '+ this.loadingProgress + '%';
            },
            showProgressBar(){
                return this.loadingProgress > 0 && this.loadingProgress < 100;
            },


        },
        methods: {
            ...mapMutations('uploadStore/',{
                addFilesToStorage: 'addFilesMutation',
                deleteSelectedFiles: 'deleteSelectedFilesMutation',
                deleteAllFiles: 'deleteAllFilesMutation',
            }),
            ...mapActions('uploadStore/',{
                uploadFiles: 'uploadFilesOnServerAction',
                saveSelectedDocuments: 'saveSelectedDocumentsToMainDbAction',
                deleteSelectedDocuments: 'deleteSelectedDocumentsFromTempDbAction'
            }),
            addFiles(event) {
                this.addFilesToStorage([...event.target.files]);
            },
            saveSelected(){
                this.clearSelectedDocs();
                this.saveSelectedDocuments();
            },
            deleteSelected(){
                this.clearSelectedDocs();
                this.deleteSelectedDocuments();
            },
            clearSelectedDocs(){
                this.$refs.uploadDocs.clearSelectedDocs();
            }
        },
        watch: {
            loadingProgress(value) {
                // console.log(value);
            }
        }
    }
</script>

<style scoped>
    .flex-container {
        display: flex;
        flex-direction: column;
        height: 75vh;
    }

    .flex-element {
        /*background: yellow;*/
        overflow-y: scroll;
        flex-grow: 3
    }

    .control-panel {

    }

    .placeholder {
        margin: 5px;
        text-align: center;
        flex-grow: 3;
        border: 3px dashed gainsboro;
        border-radius: 5px;
        flex-grow: 3
    }
    .progress{
        height: 8px;
    }

</style>