<template>
    <div>
        <!--<button type="button" @click="showModal = true">test button</button>-->
        <!--<rail-documents-search-modal-->
                <!--v-show="showModal"-->
                <!--@close-modal="closeModal"></rail-documents-search-modal>-->
        <div class="row mx-0 my-1">
            <control-panel></control-panel>
        </div>


        <railroad-documents-table :railroadDocuments="railroadDocuments"></railroad-documents-table>
        <div class="row mx-0 mt-2" style="display: flow-root">
            <div class="float-left">
                <span class="font-weight-bold">Документов:</span>
                <span> Всего: {{totalElements}}</span>
                <span> На странице: {{filteredDocuments.length}}</span>
                <span> Выбрано: {{selectedDocuments.length}}</span>
            </div>
            <div class="float-right">
                <pageable @changePage="changePage" :total-pages="totalPages" :current-page="currentPage"></pageable>
            </div>


        </div>
    </div>

</template>

<script>
    import RailroadDocumentsTable from "./components/RailroadDocumentsTable.vue";

    import { createNamespacedHelpers } from 'vuex';
    import Pageable from "../../../shared/components/Pageable.vue";
    import RailDocumentsSearchModal from "./components/DocumentsSearchForm.vue";
    import ControlPanel from "./components/ControlPanel.vue";

    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('railDocsStore');


    export default {
        name: "RailDocumentsPage",
        components: {ControlPanel, RailDocumentsSearchModal, Pageable, RailroadDocumentsTable},
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
                selectedDocuments: 'selectedDocuments',
                filteredDocuments: 'filteredDocuments'
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