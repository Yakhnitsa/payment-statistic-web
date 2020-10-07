<template>
    <div>

        <button type="button"
                :disabled="!hasEditorPermission"
                class="btn btn-primary" data-toggle="modal" data-target="#loadingPage">
            <span data-toggle="tooltip"
                  data-placement="bottom"
                  :title="hasEditorPermission ? 'Загрузка с почты или файла' : 'Недостаточно прав для совершения действия'">
                Загрузить перечни <span v-show="countOfTempLists > 0"
                                        class="badge badge-light">{{countOfTempLists}}</span>
            </span>

        </button>


        <!-- Modal -->
        <div class="modal fade" id="loadingPage" tabindex="-1" role="dialog" aria-labelledby="modalLabel"
             aria-hidden="false">
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
                        <div>
                            <uploaded-list-table :payments="loadedPayments"
                                                 v-on:change-selected="changeSelected($event)"></uploaded-list-table>
                        </div>
                        <div>
                            <ul>
                                <li v-for="file in files">{{file.name}}</li>
                            </ul>
                        </div>


                    </div>
                    <div class="modal-footer">
                        <div class="row">
                            <div class="col">
                                <div class="input-group">
                                    <input class="form-control" type="date" v-model="lastUpdate">
                                    <div class="input-group-append">
                                        <button class="btn btn-secondary"
                                                :disabled="lastUpdate === ''|| mailUpdateAwait"
                                                type="button" @click="scanFromMail">
                                            <span v-show="mailUpdateAwait"
                                                  class="spinner-border spinner-border-sm" role="status"
                                                  aria-hidden="true"></span>
                                            {{mailUpdateAwait ? 'Сканирование...' : 'Сканировать почту'}}
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col">
                                <div class="input-group">
                                    <div class="custom-file">
                                        <input type="file" ref="file" id="customFile"
                                               v-on:change="addFile($event)"
                                               multiple
                                               class="custom-file-input"
                                               enctype="multipart/form-data">
                                        <label class="custom-file-label" for="customFile">{{chosenFile}}</label>
                                    </div>
                                    <div class="input-group-append">
                                        <button class="btn btn-outline-secondary" :disabled="files < 1"
                                                @click="submitFileUpload()" type="button">Загрузить
                                            <span v-show="files.length > 0"
                                                  class="badge badge-light">{{files.length}}</span>
                                        </button>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <button type="button" class="btn btn-primary mx-1" @click="saveSelected()">Сохранить перечни
                                в БД
                            </button>
                            <button type="button" class="btn btn-primary mx-1" @click="deleteSelected()">Удалить
                                выбранные
                            </button>
                            <button type="button" class="btn btn-secondary mx-1" data-dismiss="modal">Закрыть</button>
                        </div>


                    </div>
                </div>
            </div>
        </div>

    </div>

</template>

<script>
    import UploadedListTable from './UploadedListTable.vue'
    import {mapGetters,mapMutations,mapActions} from 'vuex';

    export default {
        name: "LoadingWindow",
        components: {
            UploadedListTable
        },
        computed: {
            ...mapGetters({
                files: 'uploadStore/chosenFiles',
                loadedPayments: 'uploadStore/tempUploadedLists',
                mailUpdateAwait: 'uploadStore/mailUpdateAwait'
            }),
            countOfTempLists() {
                return this.loadedPayments.length;
            },
            hasEditorPermission() {
                return this.$store.getters.userRoles.includes('ROLE_EDITOR');
            }
        },
        data: function () {
            return {
                selectedPayments: [],
                chosenFile: 'no file',
                lastUpdate: ''
            }
        },
        methods: {
            ...mapMutations({
                addFilesToUpload: 'uploadStore/addChosenFilesMutation',
            }),
            ...mapActions('uploadStore/',{
                submitFileUpload: 'uploadListsOnServerAction',
                deleteSelectedFromMainDb: 'deleteSelectedListsAction',
                saveSelectedToMainDb: 'saveSelectedListsAction',
                loadTempListsFromServer: 'loadTempListsFromServerAction',
                startScanFromMail: 'scanFromMailAction',
            }),
            addFile(event) {
                const selectedFiles = event.target.files;
                this.addFilesToUpload(Array.from(selectedFiles))
            },

            saveSelected() {
                this.saveSelectedToMainDb(this.selectedPayments);
            },
            deleteSelected() {
                this.deleteSelectedFromMainDb(this.selectedPayments)
            },
            changeSelected(selected) {
                this.selectedPayments = selected;
            },
            scanFromMail() {
                this.startScanFromMail(this.lastUpdate);
            }
        },
        created: function () {
        },
        mounted: function () {
            this.loadTempListsFromServer()
        }

    }
</script>

<style scoped>

</style>