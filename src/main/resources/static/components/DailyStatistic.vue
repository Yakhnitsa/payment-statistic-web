<template>
    <div class="container">
        <h3> Daily statistic component</h3>
        <!--<button type="button" class="btn btn-primary" v-on:click="calculateDates">test</button>-->
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
                    <td>{{value.size}}</td>
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
                var startPeriod = new Date(this.dateFrom);
                var endPeriod = new Date(this.dateUntil);
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
                // var formData = new FormData();

                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                };

                // formData.append("dateFrom",this.dateFrom);
                // formData.append("dateUntil",this.dateUntil)

                axios.get('/api/daily-statistic',
                    {params}, {
                        // headers: {
                        //     'Content-Type': 'multipart/form-data'
                        // }

                    }
                ).then(response => {
                    console.log(response)
                    this.detailsList = response.data;
                })
                    .catch((error) => console.log(error))
            },

            calculateDates(){
                var days = [];
                var startPeriod = new Date(this.dateFrom);
                var endPeriod = new Date(this.dateUntil);
                var currentDay = startPeriod;
                while(currentDay <= endPeriod){
                    console.log(currentDay);
                    days.push(currentDay)
                    currentDay.setDate(currentDay.getDate()+1)
                }
            }



        }


    }
</script>

<style scoped>

</style>