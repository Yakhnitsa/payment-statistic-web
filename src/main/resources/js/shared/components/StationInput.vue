<template>
    <div>
        <!--<label>({{station.code}})</label>-->
        <input v-model=stationSearch type="search"
               :list="id"
               :class="[{ 'is-valid' : station.code }, inputClass]"
        />
        <datalist :id="id">
            <option v-for="station in filteredStations" v-bind:value="station.code">
                ({{station.code}}) {{station.rusName}}
            </option>
        </datalist>
    </div>

    
</template>

<script>
    export default {
        name: "StationInput",
        props:['station','stations','input-class','stationCode'],
        data(){
            return{
                stationSearch:'',
                id:'',
            }
        },

        computed:{
            filteredStations(){
                if(this.stationSearch.length < 3 ) return [];

                const filterArray = this.stations.filter(station => this.checkStation(this.stationSearch,station));
                if(filterArray.length === 1){
                    this.stationSearch = filterArray[0].rusName;
                    this.$emit('update:station', filterArray[0]);
                    return [];
                }
                return filterArray;
            },
        },
        methods:{
            checkStation(patt,station){
                const regexp = new RegExp(patt,"i");
                return regexp.test(station.code) || regexp.test(station.rusName) || regexp.test(station.ukrName);
            }
        },
        watch:{
            stationSearch(value){
                if(value=== ''){
                    this.$emit('update:station', {code:'',rusName:'',ukrName:''});
                }
            },
            stationCode(value){
                this.stationSearch = value;
            }

        },
        created(){
            this.id = `stations_f${(~~(Math.random()*1e8)).toString(16)}`;
        }

    }
</script>

<style scoped>

</style>