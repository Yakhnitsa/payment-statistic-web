<template>
    <div class="container-fluid mx-5">
        <!--TODO Вытянуть отдельно статистику по платежам
         - Изменить способ переполнения данных для названий перечней
         - Выделить другим стилем детали перечней
        -->

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
        <h3>Статистика по перечням</h3>
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
                            <!--<span v-bind:class="details ? 'fas fa-minus-square' : 'fas fa-caret-square-down'"></span>-->
                        </td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getPropertyByDate(day,'paymentVsTaxes')  | formatPayment}}
                        </td>
                    </tr>
                    <!--<tr v-show="details" v-for="(value ,key) in this.detailsList">-->
                        <!--<td class="zui-sticky-col pl-3">{{key}}</td>-->
                        <!--<td v-for="day in dateArray" class="text-right text-nowrap">-->
                            <!--{{getDataFromList(value,day) | formatPayment}}-->
                        <!--</td>-->
                    <!--</tr>-->

                    </tbody>
                </table>
            </div>
        </div>

        <h3>Детализация затрат</h3>
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
                    <tr v-show="details" v-for="(value ,key) in this.detailsList">
                        <td class="zui-sticky-col pl-3">{{key}}</td>
                        <td v-for="day in dateArray" class="text-right text-nowrap">
                            {{getDataFromList(value,day) | formatPayment}}
                        </td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>

        <p class="h2">Затраты по станциям</p>
        <p>TODO Реализовать затраты по станциям</p>

    </div>
</template>

<script>
    import { mapGetters,mapState,mapActions } from 'vuex'
    export default {
        name: "DailyStatistic",
        props: [''],
        data: function(){
            return{
                dateFrom: '',
                dateUntil: '',
                details: true,

            }
        },
        computed:{
            ...mapState(['dailyStatistic']),
            list: function(){
                return this.dailyStatistic.payments
            },
            detailsList: function(){
                return this.dailyStatistic.details
            },

            dateArray: function(){
                const startPeriod = new Date(this.dateFrom + 'T00:00:00.000+0200');
                const endPeriod = new Date(this.dateUntil + 'T00:00:00.000+0200');
                const days = [];
                let currentDay = startPeriod;
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
                console.log(this.dateArray[0])
                let day = this.dateArray[0]

                var dateString = this.getStringDate(day);

                console.log(dateString);
            },

            getStringDate(date){
                return new Date(date.getTime() - (date.getTimezoneOffset() * 60000 ))
                    .toISOString()
                    .split("T")[0];
            },

            getDataFromList(dailyStatistic,lookupDay){
                let dayString = this.getStringDate(lookupDay)
                for (let day in dailyStatistic) {
                    if(day == dayString){
                        return dailyStatistic[day]
                    }
                }
                return ''
            },

            getPropertyByDate(day,prop){
                let dayString = day.toISOString().substring(0, 10);
                for(let i in this.list){
                    let list = this.list[i];
                    if(list.date == dayString){
                        return list[prop];
                    }
                }

                return 0;
            },

            setDefaultPeriod(){
                if((this.dateFrom == '') && (this.dateUntil == '')){
                    let today = new Date();
                    let weekAgo = new Date(today.setDate(today.getDate()-7));
                    this.dateFrom = weekAgo.toISOString().substring(0, 10);
                    this.dateUntil = new Date().toISOString().slice(0,10);

                }

                const today = new Date();

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
        },
        created(){
           this.setDefaultPeriod();
           this.submitForm();
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