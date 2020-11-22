<template>
    <div class="btn-group" role="group">

        <button type="button" class="btn btn-secondary"
                @click="showModal = true">
            <i class="fas fa-search"></i>
            Поиск</button>
        <documents-search-form
                v-show="showModal"
                @close-modal="closeModal"></documents-search-form>


        <button type="button" class="btn btn-secondary">
            <i class="fas fa-search-minus"></i>
            Сбросить поиск</button>

        <button type="button" class="btn btn-secondary">
            <i class="fas fa-file-archive"></i>
            Скачать архив</button>

        <button type="button" class="btn btn-secondary">
            <i class="fas fa-file-excel"></i>
            Скачать реестр</button>

        <button type="button" class="btn btn-secondary">
            <i class="fas fa-cogs"></i>
            Настройки реестра</button>


    </div>
</template>

<script>
    import DocumentsSearchForm from "./DocumentsSearchForm.vue";
    export default {
        name: "ControlPanel",
        components: {DocumentsSearchForm},
        data(){
            return {
                showModal : false
            }
        },
        methods:{
            closeModal(){
                this.showModal = false;
            }
        }
    }
</script>

<style scoped>

</style>