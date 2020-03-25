<template>
    <div class="container mx-5">
        <h3> Daily statistic component</h3>
        <!--<button type="button" class="btn btn-primary" v-on:click="test">test</button>-->
        <form>
            <div class="form-row">
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateFrom" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <input type="date" v-model="dateUntil" class="form-control"/>
                </div>
                <div class="form-group col-md-2">
                    <button type="button" class="btn btn-primary" v-on:click="submitForm">Получить данные</button>
                </div>
            </div>
        </form>

        <div style="overflow-x:auto;">
            <table class="table table-striped table-sm">
                <thead>
                <tr>
                    <th >Наименование платежа</th>
                    <th v-for="day in dateArray" class="text-center">{{day | formatDate}}</th>
                </tr>
                </thead>
                <tbody>
                    <tr>
                        <th>Сальдо на початок розрахункової доби</th>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'openingBalance')  | formatPayment}}
                        </td>
                    </tr>

                    <tr>
                        <th>Сальдо на кінець розрахункової доби</th>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'closingBalance')  | formatPayment}}
                        </td>
                    </tr>

                    <tr>
                        <th scope="col" @click="details = !details">Всього проведено платежів
                            <span v-bind:class="details ? 'fas fa-minus-square' : 'fas fa-caret-square-down'"></span>
                        </th>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'paymentVsTaxes')  | formatPayment}}
                        </td>
                    </tr>

                    <tr v-show="details" v-for="(value ,key) in this.detailsList">
                        <th class="text-nowrap text-sm pl-4">{{key}}</th>
                        <td v-for="day in dateArray" class="text-right text-sm text-nowrap">
                            {{getDataFromList(value,day) | formatPayment}}
                        </td>
                    </tr>
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
                paymentLists:[],
                detailsList:[],
                details: false,

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
                    this.detailsList = response.data.details;
                    this.paymentLists = response.data.payments;
                })
                    .catch((error) => console.log(error))
            },

            test(){;
                console.log("test begin");
                var day = this.dateArray[1];
                console.log(day)

                var data = this.getPropertyByDate(day,'paymentVsTaxes');
                console.log('data from method=' + data);
            },

            getDataFromList(obj,day){
                for (let prop in obj) {
                    if(day.getTime() == Number(new Date(prop))){
                        return obj[prop]
                    }

                }
                return ''
            },

            getPropertyByDate(day,prop){
                for(let i in this.paymentLists){
                    let list = this.paymentLists[i];
                    let listDate = Number(new Date(list.date + 'T00:00:00.000+0200'));
                    if(listDate == day.getTime()){
                        return list[prop];
                    }
                }
                return 0;
            }
        },
        filters:{
            formatPayment(num) {
                num = num/100;
            return (
                num
                    .toFixed(2) // always two decimal digits
                    .replace('.', ',') // replace decimal point character with ,
                    .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1 ') // use ' ' as a separator
                )
            },

            formatDate(dateLong){
                return new Date(dateLong).toLocaleDateString()
            }
        }


    }
</script>

<style scoped>
    .text-sm{
        font-size: smaller;
    }

    /*.scrolling-y {*/
        /*height:150px;*/
        /*overflow-y: scroll;*/
    /*}*/
    /*.right {*/
        /*text-align: right;*/
        /*margin-right: 1em;*/
    /*}*/

    /*.left {*/
        /*text-align: left;*/
        /*margin-left: 1em;*/
    /*}*/
</style>