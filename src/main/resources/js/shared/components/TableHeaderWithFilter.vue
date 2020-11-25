<template>
    <div>
        <span v-if="!filter.active">
            {{header}}
            <span class="text-secondary" @click="switchFilter">
                <i class="fas fa-filter fa-sm"></i>
            </span>
        </span>
        <span v-else>

             <!--<input :type="inputType" class="form-control"-->
                    <!--:placeholder="inputPlaceholder"-->
                    <!--v-model="value">-->

             <div class="input-group">
                 <input :type="inputType" class="form-control" :placeholder="inputPlaceholder" v-model="value">
                 <div class="input-group-append">
                    <span @click="switchFilter" class="input-group-text" id="inputGroupPrepend">
                        <i class="fas fa-level-up-alt fa-sm"></i>
                    </span>
                 </div>
             </div>
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
            },
        },
        methods:{
            switchFilter(){
                const filter = {
                    active: !this.filter.active,
                    value: this.filter.active ? '' : this.value};
                this.$emit('update:filter', filter);
            }
        }
    }
</script>

<style scoped>
    .form-control {
        display: inline-block;
        /*width: 7em;*/
        max-width: 12em;
        height: 2em;
        padding: inherit;
        font-size: inherit;
    }

    .input-group-text {
        padding: .1rem .3rem
    }

</style>