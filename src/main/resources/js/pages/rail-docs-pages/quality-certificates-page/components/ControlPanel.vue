<template>
    <div class="btn-group" role="group">

        <button type="button" class="btn btn-secondary"
                @click="showModal = true">
            <i class="fas fa-search"></i>
            Поиск</button>

        <button type="button" class="btn btn-secondary" @click="clearSearchAndUpdate">
            <i class="fas fa-search-minus"></i>
            Сбросить поиск</button>

        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-file-archive"></i>
            Скачать архив</button>



        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-file-excel"></i>
            Скачать реестр</button>

        <button type="button" class="btn btn-secondary disabled">
            <i class="fas fa-cogs"></i>
            Настройки реестра</button>

        <certificates-search-form
                v-show="showModal"
                @close-modal="closeModal"></certificates-search-form>

    </div>
</template>

<script>
    import CertificatesSearchForm from "./CertificatesSearchForm.vue";
    import {mapActions,mapMutations, mapGetters} from 'vuex';

    export default {
        name: "ControlPanel",
        components: {CertificatesSearchForm},
        data(){
            return {
                showModal : false
            }
        },
        computed:{
            ...mapGetters({
                storedParams: 'certStore/storedRequestParams',
            }),
        },
        methods:{
            ...mapActions({
                fetchDataFromServer: 'certStore/fetchRailroadDocumentsAction'
            }),
            ...mapMutations({
                setCurrentPage: 'certStore/setCurrentPageMutation',
                storeRequestParams:'certStore/setRequestParamsMutation'
            }),
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
        }
    }
</script>

<style scoped>

</style>