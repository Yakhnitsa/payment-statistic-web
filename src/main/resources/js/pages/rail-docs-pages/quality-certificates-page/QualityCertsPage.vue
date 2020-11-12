<template>
    <div>
        <h1> Реестр качественных </h1>
        <quality-cert-table></quality-cert-table>
    </div>
</template>

<script>
    import { createNamespacedHelpers } from 'vuex';
    import QualityCertTable from "./components/QualityCertTable.vue";
    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('certStore');

    export default {
        name: "QualityCertsList",
        components: {QualityCertTable},
        computed:{
            ...mapGetters({
                    currentPage: 'currentPage',
                    totalPages: 'totalPages',
                    totalElements: 'totalElements',
                    railroadDocuments: 'documents',
                })
        },
        methods:{
            ...mapActions(['fetchRailroadDocumentsAction'])
        },

        created(){
            this.fetchRailroadDocumentsAction();
        }


    }
</script>

<style scoped>


</style>