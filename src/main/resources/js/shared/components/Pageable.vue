<template>
    <nav>
        <ul class="pagination">
            <li class="page-item">
                <a class="page-link">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>
            <li     v-for="page in pagesRange"
                    @click="setCurrentPage(page)"
                    :disabled=" page === currentPage || page "
                    :class="{ active : page === currentPage, disabled: page === currentPage}"
                    class="page-item">
                <a class="page-link">{{page+1}}</a>
            </li>
            <li class="page-item">
                <a class="page-link">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
        </ul>
    </nav>
</template>

<script>
    export default {
        name: "pageable",
        props:['total-pages','current-page'],
        computed:{
            pagesRange(){
                const range = [];
                for(let i = 0; i < this.totalPages; i++){
                    range.push(i);
                }
                return range;
            }
        },
        methods:{
            setCurrentPage(page){
                if(page < 0 || page >= this.totalPages || page=== this.currentPage) return;
                this.$emit('changePage',page);
            }
        }
    }
</script>

<style scoped>

</style>