<template>
    <span @click="changeSorting">
        <i v-if="!active" class="fas fa-sort text-muted"></i>
        <i v-else-if="asc" class="fas fa-sort-up"></i>
        <i v-else-if="desc" class="fas fa-sort-down"></i>
    </span>
</template>

<script>
    export default {
        name: "SortIcon",
        props:['sorting','sort-field'],
        computed:{
            active(){
                return this.sorting.field === this.sortField;
            },
            asc(){
                return this.sorting.asc;
            },
            desc(){
                return !this.sorting.asc;
            }
        },
        methods:{
            changeSorting(){
                this.$emit('change-sorting',this.sortField);
            }
        }
    }
</script>

<style scoped>

</style>