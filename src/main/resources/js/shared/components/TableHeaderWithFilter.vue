<template>
    <div>
        <span v-if="!filter.active">{{header}}</span>
        <span v-else>
             <input :type="inputType" class="form-control"
                    :placeholder="inputPlaceholder"
                    v-model="value">
        </span>
        <span class="text-secondary" @click="switchFilter">
             <i :class="['fas', filter.active ? 'fa-level-up-alt' : 'fa-filter','fa-sm']"></i>
        </span>
    </div>
</template>

<script>
    export default {
        name: "TableHeaderWithFilter",
        props:['filter', 'inputType','inputPlaceholder','header'],
        computed:{
            value:{
                get(){
                    return this.filter.value;
                },
                set(val){
                    const filter = {active: true, value: val};
                    this.$emit('update:filter', filter);
                }
            }
        },
        methods:{
            switchFilter(){
                const filter = {
                    active: !this.filter.active,
                    value: this.value};
                this.$emit('update:filter', filter);
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