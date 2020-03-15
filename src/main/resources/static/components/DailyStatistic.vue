<template>
    <div class="container">
        <h3> Daily statistic component</h3>
        <button type="button" class="btn btn-primary" v-on:click="test">test</button>
        <form>
            <div class="form-row">
                <div class="form-group col-md-2">
                    <!--<label for="dateFrom">Date from</label>-->
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <!--<label for="dateUntil">Date until</label>-->
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button type="button" class="btn btn-primary" v-on:click="submitForm">Получить данные</button>
                </div>
            </div>
        </form>

        <div class="table-responsive">
            <table class="table table-sm table-striped table-hover">
                <thead>
                <tr>
                    <th scope="col">Наименование платежа</th>
                    <th v-for="day in dateArray">{{day.getDate()}}</th>
                </tr>
                </thead>
                <tr v-for="(value ,key) in this.detailsList">
                    <th>{{key}}</th>
                    <td v-for="day in dateArray">{{showProps(value,day)}}</td>
                </tr>
                <tbody>

                </tbody>
            </table>
        </div>


    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "DailyStatistic",
        props: [''],
        data: function(){
            return{
                dateFrom: '2020-02-01',
                dateUntil: '2020-02-05',
                detailsList:[]
            }
        },
        computed:{
            dateArray: function(){
                var startPeriod = new Date(this.dateFrom + 'T00:00:00.000+0200');
                var endPeriod = new Date(this.dateUntil + 'T00:00:00.000+0200');
                var days = [];
                var currentDay = startPeriod;
                while(currentDay <= endPeriod){
                    days.push(new Date(currentDay))
                    currentDay.setDate(currentDay.getDate()+1)
                }
                return days;
            }

        },
        methods:{
            submitForm(){

                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                };

                axios.get('/api/daily-statistic',
                    {params}, {
                        // headers: {
                        //     'Content-Type': 'multipart/form-data'
                        // }

                    }
                ).then(response => {
                    this.detailsList = response.data;
                })
                    .catch((error) => console.log(error))
            },

            test(){;
                console.log("test begin");
                var arrival = this.detailsList['Прибуття - імпорт']
                // this.showProps(arrival);
                for(let day in this.dateArray){
                    let data = this.showProps(arrival,this.dateArray[day]);
                    console.log('found property='+data);
                }
                // for (var prop in arrival) {
                //     // console.log('day=' + day);
                //     console.log('property=' + prop);
                //     // console.log("values: " + day.getTime() + '|' + new Date(prop).getTime() + '==' + (day.getTime()==new Date(prop).getTime()));
                //     // if( obj.hasOwnProperty( prop ) ) {
                //     //     if(day.getTime() == new Date(prop).getTime()){
                //     //         console.log(prop + " = " + obj[prop]);
                //     //         return obj[prop]
                //     //     }
                //     // }
                // }

            },

            getDataFromList(obj,day){
                for (let prop in obj) {
                    // console.log('day=' + day);
                    // console.log('property=' + prop);
                    // console.log("values: " + day.getTime() + '|' + new Date(prop).getTime() + '==' + (day.getTime()==new Date(prop).getTime()));
                    if( obj.hasOwnProperty( prop ) ) {
                        if(day.getTime() == new Date(prop).getTime()){
                            // console.log(prop + " = " + obj[prop]);
                            return obj[prop]
                        }
                    }
                    return '';
                }
            },
            showProps(obj,day){
                for (let prop in obj) {
                    if(day.getTime() == Number(new Date(prop))){
                        // console.log(day.getTime())
                        // console.log(new Date(prop).getTime())
                        return obj[prop]
                    }

                }
                return ''
            }

        }


    }
</script>

<style scoped>

</style>