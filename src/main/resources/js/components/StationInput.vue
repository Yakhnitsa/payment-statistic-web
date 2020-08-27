<template>
    <div>
        <!--<label>({{station.code}})</label>-->
        <input v-model=stationSearch type="text" list="stations"/>
        <datalist id="stations">
            <option v-for="station in filteredStations" v-bind:value="station.code">
                ({{station.code}}) {{station.rusName}}
            </option>
        </datalist>
    </div>

    
</template>

<script>
    export default {
        name: "StationInput",
        props:['station','stations'],
        data(){
            return{
                stationSearch:'',
                // filteredStations:[]
            }
        },
        computed:{
            filteredStations(){
                if(this.stationSearch.length < 3 ) return [];

                const filterArray = this.stations.filter(station => this.checkStation(this.stationSearch,station));
                if(filterArray.length === 1){
                    console.log(filterArray)
                    this.stationSearch = filterArray[0].rusName;
                    this.$emit('update:station', filterArray[0]);
                    return [];
                }
                return filterArray;

            }
        },
        methods:{
            checkStation(patt,station){
                const regexp = new RegExp(patt,"i");
                return regexp.test(station.code) || regexp.test(station.rusName) || regexp.test(station.ukrName);
            }
        },
        watch:{
            // stationSearch(value){
            //     if(value.length < 3 ) return [];
            //
            //     const filteredArray = this.stations.filter(station => this.checkStation(this.stationSearch,station));
            //     if(filteredArray.length === 1){
            //         this.stationSearch = filteredArray[0].code;
            //         this.$emit('update:station', filteredArray[0]);
            //         this.filteredStations = []
            //     }
            //     this.filteredStations = filteredArray;
            // }
        }

    }
</script>

<style scoped>

</style>