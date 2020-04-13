<template>
    <div class="container-fluid mx-5">
        <!--<h3> Daily statistic component</h3>-->
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

        <div class="zui-wrapper">
            <div class="zui-scroller">
                <table class="zui-table">
                    <thead>
                        <tr>
                            <th class="zui-sticky-col">Наименование платежа</th>
                            <th v-for="day in dateArray"
                                class="text-center">
                                {{day | formatDate}}
                            </th>
                            <th class="sticky-right-col">Сумма</th>
                        </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="zui-sticky-col">Сальдо на початок розрахункової доби</td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'openingBalance')  | formatPayment}}
                        </td>
                    </tr>
                    <tr>
                        <td class="zui-sticky-col">Сальдо на кінець розрахункової доби</td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'closingBalance')  | formatPayment}}
                        </td>
                    </tr>
                    <tr>
                        <td class="zui-sticky-col" @click="details = !details">Всього проведено платежів
                            <span v-bind:class="details ? 'fas fa-minus-square' : 'fas fa-caret-square-down'"></span>
                        </td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'paymentVsTaxes')  | formatPayment}}
                        </td>
                    </tr>
                    <tr v-show="details" v-for="(value ,key) in this.detailsList">
                        <td class="zui-sticky-col pl-3">{{key}}</td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getDataFromList(value,day) | formatPayment}}
                        </td>
                    </tr>
                    <!--<tr v-show="details" v-for="(value ,key) in this.detailsList">-->
                        <!--<th class="text-nowrap zui-sticky-col pl-4">{{key}}</th>-->
                        <!--<td v-for="day in dateArray" class="text-right text-nowrap">-->
                            <!--{{getDataFromList(value,day) | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->

                    </tbody>
                </table>
            </div>
        </div>

        <!--<div style="overflow-x:auto;">-->
            <!--<table class="table table-striped table-sm">-->
                <!--<thead>-->
                <!--<tr>-->
                    <!--<th scope="col" class="col-3" >Наименование платежа</th>-->
                    <!--<th v-for="day in dateArray" class="text-center">{{day | formatDate}}</th>-->
                <!--</tr>-->
                <!--</thead>-->
                <!--<tbody>-->
                    <!--<tr>-->
                        <!--<th scope="col" class="col-3" >Сальдо на початок розрахункової доби</th>-->
                        <!--<td v-for="day in dateArray" class="text-right text-nowrap">-->
                            <!--{{getPropertyByDate(day,'openingBalance')  | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->

                    <!--<tr>-->
                        <!--<th>Сальдо на кінець розрахункової доби</th>-->
                        <!--<td v-for="day in dateArray" class="text-right text-nowrap">-->
                            <!--{{getPropertyByDate(day,'closingBalance')  | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->

                    <!--<tr>-->
                        <!--<th scope="col" @click="details = !details">Всього проведено платежів-->
                            <!--<span v-bind:class="details ? 'fas fa-minus-square' : 'fas fa-caret-square-down'"></span>-->
                        <!--</th>-->
                        <!--<td v-for="day in dateArray" class="text-right text-nowrap">-->
                            <!--{{getPropertyByDate(day,'paymentVsTaxes')  | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->

                    <!--<tr v-show="details" v-for="(value ,key) in this.detailsList">-->
                        <!--<th class="text-nowrap text-sm pl-4">{{key}}</th>-->
                        <!--<td v-for="day in dateArray" class="text-right text-sm text-nowrap">-->
                            <!--{{getDataFromList(value,day) | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->
                <!--</tbody>-->
            <!--</table>-->
        <!--</div>-->
    </div>
</template>

<script>
    import axios from 'axios'
    import { mapGetters,mapState,mapActions } from 'vuex'
    export default {
        name: "DailyStatistic",
        props: [''],
        data: function(){
            return{
                dateFrom: '2020-02-10',
                dateUntil: '2020-02-15',
                details: true,

            }
        },
        computed:{
            ...mapState(['dailyStatistic']),
            paymentLists: function(){
                return this.dailyStatistic.payments
            },
            detailsList: function(){
                return this.dailyStatistic.details
            },

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
            ...mapActions(['getDailyStatisticAction']),
            submitForm(){
                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                };
                this.getDailyStatisticAction(params)
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

    .scrolling-y {
        height:150px;
        overflow-y: scroll;
    }
    .right {
        text-align: right;
        margin-right: 1em;
    }

    .left {
        text-align: left;
        margin-left: 1em;
    }
    .zui-table {
        /*border: none;*/
        /*border-right: solid 1px #DDEFEF;*/
        border-collapse: separate;
        border-spacing: 1px;
        font: normal 13px Arial, sans-serif;
    }
    .zui-table thead th {
        background-color: #DDEFEF;
        border: none;
        color: #336B6B;
        padding: 10px;
        /*text-align: left;*/
        /*text-shadow: 1px 1px 1px #fff;*/
        white-space: nowrap;
    }
    .zui-table tbody td {
        border-bottom: solid 1px #DDEFEF;
        color: #333;
        padding: 10px;
        /*text-shadow: 1px 1px 1px #fff;*/
        white-space: nowrap;
    }
    .zui-wrapper {
        position: relative;
    }

    .zui-table .zui-sticky-col {
        border-left: solid 1px #DDEFEF;
        border-right: solid 1px #DDEFEF;
        left: 0;
        position: absolute;
        top: auto;
        width: 25%;
    }
    .zui-scroller {
        margin-left: 25%;
        overflow-x: scroll;
        overflow-y: visible;
        padding-bottom: 5px;
        width: 60%;
    }
    .zui-last-col {
        margin-left: 85%;
        border-left: solid 1px #DDEFEF;
        border-right: solid 1px #DDEFEF;
        left: 0;
        position: absolute;
        top: auto;
        width: auto;
    }
    .sticky-right-col{
        position: sticky;
        right: 1px;

    }

</style>