<template>
    <div>
        <button type="button" @click="test">test button</button>
        <h1>
            RailDocumentsPage
        </h1>
        <railroad-documents-table :railroadDocuments="railroadDocuments"></railroad-documents-table>
        <div class="row mx-0 mt-2 float-right">
            <pageable @changePage="changePage" :total-pages="10" :current-page="5"></pageable>
        </div>
    </div>

</template>

<script>
    import RailroadDocumentsTable from "./components/RailroadDocumentsTable.vue";

    import { createNamespacedHelpers } from 'vuex';
    import Pageable from "../../../shared/components/Pageable.vue";

    const { mapState, mapActions, mapMutations } = createNamespacedHelpers('railDocsStore');


    export default {
        name: "RailDocumentsPage",
        components: {Pageable, RailroadDocumentsTable},
        computed:{
            ...mapState({
                railroadDocuments: state => state.documents,
                currentPage: state => state.currentPage,
                totalPages: state => state.totalPages,
                totalElements: state => state.totalElements,
                // b: state => state.b
            }),
            // railroadDocuments(){
            //
            // }
        },
        methods:{
            ...mapActions(['fetchRailroadDocumentsAction'],{

            }),
            test(){
                this.fetchRailroadDocumentsAction();
            },
            changePage(page){
                console.log(page);
            }
        },
        mounted(){
            this.test();
        }
    }
</script>

<style scoped>

</style>