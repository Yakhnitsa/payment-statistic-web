<template>
    <div>
        <button type="button" @click="showModal = true">test button</button>
        <rail-documents-search-modal :show-modal.sync="showModal"></rail-documents-search-modal>
        <h1>
            RailDocumentsPage
        </h1>

        <railroad-documents-table :railroadDocuments="railroadDocuments"></railroad-documents-table>
        <div class="row mx-0 mt-2 float-right">
            <pageable @changePage="changePage" :total-pages="10" :current-page="currentPage"></pageable>
        </div>
    </div>

</template>

<script>
    import RailroadDocumentsTable from "./components/RailroadDocumentsTable.vue";

    import { createNamespacedHelpers } from 'vuex';
    import Pageable from "../../../shared/components/Pageable.vue";
    import RailDocumentsSearchModal from "./components/RailDocumentsSearchModal.vue";

    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('railDocsStore');


    export default {
        name: "RailDocumentsPage",
        components: {RailDocumentsSearchModal, Pageable, RailroadDocumentsTable},
        data(){
            return{
                showModal: false,
            }
        },

        computed:{
            ...mapGetters({
                currentPage: 'currentPage',
                totalPages: 'totalPages',
                totalElements: 'totalElements',
                railroadDocuments: 'documents',
            })
            // railroadDocuments(){
            //
            // }
        },
        methods:{
            ...mapActions(['fetchRailroadDocumentsAction'],{

            }),
            ...mapMutations(['setCurrentPageMutation']),
            test(){
                this.fetchRailroadDocumentsAction();
            },
            changePage(page){
                this.setCurrentPageMutation(page);
                this.fetchRailroadDocumentsAction();
            },
        },
        mounted(){
            this.test();
        }
    }
</script>

<style scoped>

</style>