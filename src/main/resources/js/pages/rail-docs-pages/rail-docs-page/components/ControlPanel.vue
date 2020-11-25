<template>
    <div class="btn-group" role="group">

        <button type="button" class="btn btn-secondary"
                @click="showModal = true">
            <i class="fas fa-search"></i>
            Поиск</button>
        <documents-search-form
                v-show="showModal"
                @close-modal="closeModal"></documents-search-form>


        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-search-minus"></i>
            Сбросить поиск</button>

        <div class="btn-group" role="group">
            <button id="downloadArchiveDropdown" type="button" class="btn btn-secondary"
                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="fas fa-file-archive"></i>
                Скачать архив
            </button>
            <div class="dropdown-menu" aria-labelledby="downloadArchiveDropdown">
                <a class="dropdown-item" @click="downloadArchiveAll">Все документы</a>
                <a class="dropdown-item" @click="downloadArchiveSelected">Выбранные документы</a>
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
    import {mapActions} from 'vuex';

    export default {
        name: "ControlPanel",
        components: {DocumentsSearchForm},
        data(){
            return {
                showModal : false
            }
        },
        methods:{

            ...mapActions({
                downloadArchiveAll: 'railDocsStore/downloadFilteredDocumentsArchiveAction',
                downloadArchiveSelected: 'railDocsStore/downloadSelectedDocumentsArchiveAction'
            }),

            closeModal(){
                this.showModal = false;
            }
        }
    }
</script>

<style scoped>

</style>