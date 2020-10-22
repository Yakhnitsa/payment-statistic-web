<template>
    <div>
        <button type="button" @click="test">test button</button>
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

    const { mapState, mapActions, mapMutations, mapGetters } = createNamespacedHelpers('railDocsStore');


    export default {
        name: "RailDocumentsPage",
        components: {Pageable, RailroadDocumentsTable},
        computed:{
            ...mapState({
                railroadDocuments: state => state.documents,
                // currentPage: state => state.currentPage,
                totalPages: state => state.totalPages,
                totalElements: state => state.totalElements,
                // b: state => state.b
            }),
            ...mapGetters({
                currentPage: 'currentPage'
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
                console.log(this.currentPage)
            }
        },
        mounted(){
            this.test();
        }
    }
</script>

<style scoped>

</style>