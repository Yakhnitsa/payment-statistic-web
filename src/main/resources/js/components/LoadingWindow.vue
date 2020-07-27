<template>
    <div>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loadingPage">
            Загрузить перечни <span v-show="countOfTempLists > 0" class="badge badge-light">{{countOfTempLists}}</span>
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
                        <div>
                            <uploaded-list-table :payments="loadedPayments"
                                                 v-on:change-selected="changeSelected($event)"></uploaded-list-table>
                        </div>
                        <div>
                            <ul >
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
                                        <button class="btn btn-secondary" type="button" @click="scanFromMail">Сканировать почту</button>
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
                                        <button class="btn btn-outline-secondary"
                                                @click="submitFileUpload()" type="button">Загрузить</button>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <button type="button" class="btn btn-primary" @click="saveSelected()">Сохранить перечни в БД</button>
                            <button type="button" class="btn btn-primary" @click="deleteSelected()">Удалить выбранные</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        </div>


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
            }),
            countOfTempLists(){
                return this.loadedPayments.length;
            }
        },
        data: function(){
            return{
                selectedPayments:[],
                chosenFile:'no file',
                lastUpdate:''
            }
        },
        methods:{
            ...mapMutations(['addChosenFilesMutation']),
            ...mapActions(['uploadListsOnServerAction',
                'deleteSelectedListsAction',
                'saveSelectedListsAction',
                'loadTempListsFromServerAction',
                'getPaymentListsAction',
                'scanFromMailAction'
            ]),
            addFile(event){
                var selectedFiles = event.target.files;
                this.addChosenFilesMutation(Array.from(selectedFiles))
            },
            submitFileUpload(){
                this.uploadListsOnServerAction();

            },
            saveSelected(){
                this.saveSelectedListsAction(this.selectedPayments)
                this.getPaymentListsAction()
            },
            deleteSelected(){
                this.deleteSelectedListsAction(this.selectedPayments)
            },
            changeSelected(selected){
                this.selectedPayments = selected;
            },
            scanFromMail(){
                // const params = {
                //     lastUpdate: this.lastUpdate,
                //
                // };
                this.scanFromMailAction(this.lastUpdate);
            }
        },
        created: function(){
        },
        mounted: function(){
            this.loadTempListsFromServerAction()
        }

    }
</script>

<style scoped>

</style>