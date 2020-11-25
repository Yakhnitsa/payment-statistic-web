<template>
    <div>
        <span v-if="!filterIsActive">{{header}}</span>
        <span v-else>
             <input :type="inputType" class="form-control"
                    :placeholder="inputPlaceholder"
                    v-model="value">
        </span>
        <span class="text-secondary" @click="switchFilter">
             <i :class="['fas', filterIsActive ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
        </span>
    </div>
</template>

<script>
    export default {
        name: "TableHeaderWithFilter",
        props:['filter','filterIsActive', 'inputType','inputPlaceholder','filterValue','header'],
        computed:{
            value:{
                get(){
                    return this.filterValue;
                },
                set(val){
                    this.$emit('update:filterValue', val);
                }
            }
        },
        methods:{
            switchFilter(){
                this.$emit('update:filterIsActive', !this.filterIsActive);
            }
        }
    }
</script>

<style scoped>
    .form-control {
        display: inline-block;
        width: 7em;
        height: 2em;
        padding: inherit;
        font-size: inherit;
    }

</style>