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

        <daily-statistic-table
                tableHeader="Детализация по видам платежей"
                :dateArray="dateArray"
                :dataList="detailsList">
        </daily-statistic-table>

        <daily-statistic-table
                tableHeader="Затраты по станциям"
                :dateArray="dateArray"
                :dataList="expensesByStation"
        ></daily-statistic-table>

    </div>
</template>

<script>
    import { mapGetters, mapState ,mapActions, mapMutations } from 'vuex'
    import DailyStatisticTable from '../components/DailyStatisticTable.vue'

    export default {
        name: "DailyStatistic",
        props: [''],
        components:{DailyStatisticTable},
        data: function(){
            return{
                dateFrom: '',
                dateUntil: '',
                details: true,

            }
        },
        computed:{
            ...mapState(['dailyStatistic']),
            paymentList(){
                return this.dailyStatistic.payments
            },
            detailsList(){
                return this.dailyStatistic.details
            },
            expensesByStation(){
                return this.dailyStatistic.expensesByStation;
            },

            dateArray(){
                return this.dailyStatistic.dates;
            }

        },

        methods:{
            ...mapActions(['getDailyStatisticAction']),
            ...mapMutations(['setDailyStatisticPeriod']),
            submitForm(){
                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                };
                this.$store.commit('setDailyStatisticPeriod',params)
                this.getDailyStatisticAction(params)
            },

            test(){
                // console.log("test begin");
                // console.log(this.dateArray[0])
                // let day = this.dateArray[0]
                //
                // var dateString = this.getStringDate(day);
                //
                // console.log(dateString);
            },

            getDataFromList(dailyStatistic,lookupDay){
                for (let day in dailyStatistic) {
                    if(day === lookupDay){
                        return dailyStatistic[day]
                    }
                }
                return ''
            },
            getSummaryFromList(list){
                let values = Object.values(list);
                return values.reduce((a, b) => a + b, 0)
            },

            getPropertyByDate(day,prop){
                for(let i in this.paymentList){
                    let list = this.paymentList[i];
                    if(list.date === day){
                        return list[prop];
                    }
                }

                return 0;
            },

            setDefaultPeriod(){
                this.dateFrom = this.$store.state.dailyStatistic.dateFrom
                this.dateUntil = this.$store.state.dailyStatistic.dateUntil

                if((this.dateFrom == '') && (this.dateUntil == '')){
                    let today = new Date();
                    let weekAgo = new Date(today.setDate(today.getDate()-7));
                    this.dateFrom = weekAgo.toISOString().substring(0, 10);
                    this.dateUntil = new Date().toISOString().slice(0,10);
                    let period = {
                        dateFrom: this.dateFrom,
                        dateUntil: this.dateUntil
                    }
                    this.$store.commit('setDailyStatisticPeriod',period)

                }
            }
        },
        filters:{
            // formatPayment(num) {
            //     num = num/100;
            //     return (
            //         num
            //             .toFixed(2) // always two decimal digits
            //             .replace('.', ',') // replace decimal point character with ,
            //             .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1 ') // use ' ' as a separator
            //         )
            // },

            formatDate(date){
                return new Date(date).toLocaleDateString()
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
        right: 0px;
        background-color: aliceblue;
        border-left: solid 2px #DDEFEF;
        /*border-right: solid 2px #DDEFEF;*/

    }

</style>