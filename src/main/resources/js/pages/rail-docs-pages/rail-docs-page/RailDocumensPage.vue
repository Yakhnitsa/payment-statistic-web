<template>
    <div>
        <button type="button" @click="showModal = true">test button</button>
        <rail-documents-search-modal
                v-show="showModal"
                @close-modal="closeModal"></rail-documents-search-modal>
        <h1>
            RailDocumentsPage
        </h1>

        <railroad-documents-table :railroadDocuments="railroadDocuments"></railroad-documents-table>
        <div class="row mx-0 mt-2 float-right">
            <span>Всего найдено накладных: {{totalElements}}</span>
            <pageable @changePage="changePage" :total-pages="totalPages" :current-page="currentPage"></pageable>
        </div>
    </div>

</template>

<script>
    import RailroadDocumentsTable from "./components/RailroadDocumentsTable.vue";

    import { createNamespacedHelpers } from 'vuex';
    import Pageable from "../../../shared/components/Pageable.vue";
    import RailDocumentsSearchModal from "./components/ModalSearchForm.vue";

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
            closeModal(){
                this.showModal = false;
            }
        },
        mounted(){
            this.test();
        }
    }
</script>

<style scoped>

</style>