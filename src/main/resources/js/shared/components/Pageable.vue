<template>
    <nav v-show="totalPages > 1">
        <ul class="pagination">
            <li     v-show="totalPages > 5"
                    :class="{disabled:  currentPage <= 0 }"
                    class="page-item">
                <a class="page-link" @click="setCurrentPage(0)">
                    <i class="fas fa-angle-double-left"></i>
                </a>
            </li>
            <li     v-show="totalPages > 3"
                    :class="{disabled:  currentPage <= 0 }"
                    class="page-item">
                <a class="page-link" @click="setCurrentPage(currentPage - 1)">
                    <i class="fas fa-angle-left"></i>
                </a>
            </li>
            <li     v-for="page in pagesRange"
                    @click="setCurrentPage(page)"
                    :class="{ active : page === currentPage, disabled: page === currentPage || page === null}"
                    class="page-item">
                <a class="page-link">{{page === null ? '...' : page + 1}}</a>
            </li>
            <li
                    v-show="totalPages > 3"
                    :class="{disabled: currentPage >= totalPages - 1}"
                    class="page-item">
                <a class="page-link"
                   @click="setCurrentPage(currentPage + 1)">
                    <i class="fas fa-angle-right"></i>
                </a>
            </li>
            <li
                    v-show="totalPages > 5"
                    :class="{disabled: currentPage >= totalPages - 1}"
                    class="page-item">
                <a class="page-link"
                   @click="setCurrentPage(totalPages -1)">
                    <i class="fas fa-angle-double-right"></i>
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
                    if(i === this.currentPage - 2 || i === this.currentPage + 2 )
                        range.push(null);
                    else if(i >= this.currentPage -1 && i <= this.currentPage + 1)
                        range.push(i);
                }
                return range;
            }
        },
        methods:{
            setCurrentPage(page){
                if(page < 0 || page >= this.totalPages || page === this.currentPage) return;
                this.$emit('changePage',page);
            }
        }
    }
</script>

<style scoped>
    .page-link {
        padding: .4rem .6rem;
    }

</style>