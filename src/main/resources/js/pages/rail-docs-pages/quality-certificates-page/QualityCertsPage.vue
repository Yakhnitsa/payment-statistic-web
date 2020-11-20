<template>
    <div>
        <div class="row mx-0 mt-2">
            <control-panel></control-panel>
        </div>
        <div class="mt-2">
            <quality-cert-table></quality-cert-table>
        </div>


        <div class="row mx-0 mt-2 float-right">
            <pageable :current-page="currentPage" :total-pages="totalPages" @changePage="changeCurrentPage"></pageable>
        </div>

    </div>
</template>

<script>
    import { createNamespacedHelpers } from 'vuex';
    import QualityCertTable from "./components/QualityCertTable.vue";
    import Pageable from '../../../shared/components/Pageable.vue'
    import ControlPanel from "./components/ControlPanel.vue";
    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('certStore');

    export default {
        name: "QualityCertsList",
        components: {ControlPanel, QualityCertTable, Pageable},
        computed:{
            ...mapGetters({
                    currentPage: 'currentPage',
                    totalPages: 'totalPages',
                    totalElements: 'totalElements',
                    railroadDocuments: 'documents',
                })
        },
        methods:{
            ...mapActions(['fetchRailroadDocumentsAction']),
            ...mapMutations(['setCurrentPageMutation']),
            changeCurrentPage(page){
                this.setCurrentPageMutation(page);
                this.fetchRailroadDocumentsAction();
            }
        },

        created(){
            this.fetchRailroadDocumentsAction();
        }


    }
</script>

<style scoped>

</style>