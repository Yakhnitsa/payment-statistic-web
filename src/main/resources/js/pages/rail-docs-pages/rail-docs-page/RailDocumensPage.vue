<template>
    <div>
        <button type="button" @click="test">test button</button>
        <h1>
            RailDocumentsPage
        </h1>
        <railroad-documents-table :railroadDocuments="railroadDocuments"></railroad-documents-table>
        <div class="row mx-0 mt-2 float-right">
            <span>pageable...</span>
        </div>
    </div>

</template>

<script>
    import RailroadDocumentsTable from "./components/RailroadDocumentsTable.vue";

    import { createNamespacedHelpers } from 'vuex';

    const { mapState, mapActions, mapMutations } = createNamespacedHelpers('railDocsStore');


    export default {
        name: "RailDocumentsPage",
        components: {RailroadDocumentsTable},
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
            }
        },
        mounted(){
            this.test();
        }
    }
</script>

<style scoped>

</style>