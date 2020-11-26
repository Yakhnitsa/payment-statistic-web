<template>
    <div class="btn-group" role="group">

        <button type="button" class="btn btn-secondary"
                @click="showModal = true">
            <i class="fas fa-search"></i>
            Поиск</button>
        <documents-search-form
                v-show="showModal"
                @close-modal="closeModal"></documents-search-form>


        <button type="button" class="btn btn-secondary" @click="clearSearchAndUpdate">
            <i class="fas fa-search-minus"></i>
            Сбросить поиск</button>

        <div class="btn-group" role="group">
            <button id="downloadArchiveDropdown" type="button" class="btn btn-secondary"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-file-archive"></i>
                Скачать архив
            </button>
            <div class="dropdown-menu" aria-labelledby="downloadArchiveDropdown">
                <a class="dropdown-item" @click="downloadAll">Все документы</a>
                <a class="dropdown-item" @click="downloadSelected">Выбранные документы</a>
            </div>
        </div>

        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-file-excel"></i>
            Скачать реестр</button>

        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-cogs"></i>
            Настройки реестра</button>


    </div>
</template>

<script>
    import DocumentsSearchForm from "./DocumentsSearchForm.vue";
    import {mapActions,mapMutations, mapGetters} from 'vuex';

    import MessageManager from '../../../../shared/services/messageManager'

    export default {
        name: "ControlPanel",
        components: {DocumentsSearchForm},
        data(){
            return {
                showModal : false,
                arcLimit: 250,
            }
        },
        computed:{
            ...mapGetters({
                storedParams: 'railDocsStore/storedRequestParams',
                filteredDocuments: 'railDocsStore/filteredDocuments',
                selectedDocuments : 'railDocsStore/selectedDocuments'
            }),
        },
        methods:{
            ...mapActions({
                downloadArchiveAll: 'railDocsStore/downloadFilteredDocumentsArchiveAction',
                downloadArchiveSelected: 'railDocsStore/downloadSelectedDocumentsArchiveAction',
                fetchDataFromServer: 'railDocsStore/fetchRailroadDocumentsAction'
            }),
            ...mapMutations({
                setCurrentPage: 'railDocsStore/setCurrentPageMutation',
                storeRequestParams:'railDocsStore/setRequestParamsMutation'
            }),

            downloadSelected(){
                this.selectedDocuments.length > this.arcLimit ?
                    this.showAchLimitMessage() : this.downloadArchiveSelected();
            },

            downloadAll(){
                this.filteredDocuments.length > this.arcLimit ?
                    this.showAchLimitMessage() : this.downloadArchiveAll();
            },

            closeModal(){
                this.showModal = false;
            },
            clearSearchAndUpdate(){
                this.setCurrentPage(0);
                const cleanedParams = {};
                for (const [key, value] of Object.entries(this.storedParams)) {
                    cleanedParams[key] = '';
                }
                this.storeRequestParams(cleanedParams);

                this.fetchDataFromServer();
            },
            showAchLimitMessage(){
                let message = "Ошибка загрузки архива, \n";
                message += "пшевышен лимит на скачивание ";
                message += this.arcLimit + " документов";
                MessageManager.showWarningMessage(message);

            },
        }
    }
</script>

<style scoped>

</style>