<template>
    <div class="container-fluid">

        <div class="row">
            <!--Окно файлов для загрузки файлов-->
            <div class="col-md-6">
                <div class="flex-container">
                    <div class="h5">Список файлов</div>
                    <div v-if="files.length > 0" class="flex-element">

                        <files-table  :files="files"></files-table>

                    </div>

                    <div v-else class="placeholder">
                        <h5 class="card-title">Нет данных для отображения</h5>
                        <p class="card-text">Загрузите ЖД накладные</p>
                    </div>

                    <div class="control-panel my-1">
                        <div class="input-group">
                            <div class="custom-file">
                                <input type="file" id="customFile"
                                       v-on:change="addFiles($event)"
                                       multiple
                                       class="custom-file-input"
                                       enctype="multipart/form-data">
                                <label class="custom-file-label" for="customFile">{{files.length}}</label>
                            </div>


                            <button type="button" class="btn btn-primary mx-1" @click="saveSelected()">Загрузить
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <!--Окно загруженных накладных-->
            <div class="col-md-6">
                <div class="flex-container">
                    <div class="h5">Загруженные накладные</div>
                    <div class="flex-element">
                        <ul class="list-group">
                            <li class="list-group-item">New <span class="badge">12</span></li>
                            <li class="list-group-item">Deleted <span class="badge">5</span></li>
                            <li class="list-group-item">Warnings <span class="badge">3</span></li>
                            <li class="list-group-item">Warnings <span class="badge">3</span></li>
                            <li class="list-group-item">Warnings <span class="badge">3</span></li>
                            <li class="list-group-item">Warnings <span class="badge">3</span></li>
                            <li class="list-group-item">Warnings <span class="badge">3</span></li>
                        </ul>
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

        <!--<div class="row">-->
        <!--</div>-->
        <!--<div class="row">-->
            <!--<div class="col-md">-->
                <!--<div class="flex-grow h-75">-->
                    <!--<files-table :files="files"></files-table>-->
                <!--</div>-->


                <!--<div class="input-group flex-box">-->
                    <!--<div class="custom-file">-->
                        <!--<input type="file" id="customFile"-->
                               <!--v-on:change="addFiles($event)"-->
                               <!--multiple-->
                               <!--class="custom-file-input"-->
                               <!--enctype="multipart/form-data">-->
                        <!--<label class="custom-file-label" for="customFile">{{files.length}}</label>-->
                    <!--</div>-->


                    <!--<button type="button" class="btn btn-primary mx-1" @click="saveSelected()">Загрузить-->
                    <!--</button>-->
                <!--</div>-->

            <!--</div>-->


            <!--<div class="col-md">-->
                <!--<div class="flex-grow h-75">-->
                    <!--Downloaded data-->
                <!--</div>-->

                <!--<div class="input-group flex-box ">-->
                    <!--<button type="button" class="btn btn-primary mx-1" @click="saveSelected()">Сохранить в БД-->
                    <!--</button>-->

                    <!--<button type="button" class="btn btn-primary mx-1" @click="deleteSelected()">Удалить-->
                        <!--выбранные-->
                    <!--</button>-->
                <!--</div>-->
            <!--</div>-->
        <!--</div>-->

        <!--<div class="row">-->
        <!--</div>-->

        <!--<div>-->

        <!--</div>-->
        <!--<div class="row">-->
            <!--<div class="col-6">-->


            <!--</div>-->


        <!--</div>-->

    </div>

</template>

<script>
    import FilesTable from "../../components/rail-docs-components/FilesTable.vue";
    export default {
        name: "LoadingPage",
        components: {FilesTable},
        data() {
            return {
                files: [],
                uploadedDocs: []
            }
        },
        methods: {
            choseFile() {

            },
            uploadFiles() {

            },
            saveSelected() {

            },
            deleteSelected() {

            },
            addFiles(event) {
                this.files = event.target.files;
                // this.addChosenFilesMutation(Array.from(selectedFiles))
            },
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
    .control-panel{

    }

    .placeholder {
        margin: 5px;
        text-align: center;
        flex-grow: 3;
        border: 3px dashed gainsboro;
        border-radius: 5px;
        flex-grow: 3
    }

</style>