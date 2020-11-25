<template>
    <div class="btn-group" role="group">

        <button type="button" class="btn btn-secondary"
                @click="showModal = true">
            <i class="fas fa-search"></i>
            Поиск</button>

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

        <certificates-search-form
                v-show="showModal"
                @close-modal="closeModal"></certificates-search-form>

    </div>
</template>

<script>
    import CertificatesSearchForm from "./CertificatesSearchForm.vue";
    export default {
        name: "ControlPanel",
        components: {CertificatesSearchForm},
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