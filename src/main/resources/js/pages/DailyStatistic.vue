<template>
    <div class="container-fluid mx-1">
        <form class="mx-3">
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

        <daily-statistic-table
                tableHeader="Статистика по перечням"
                :dateArray="dateArray"
                :dataList="paymentList">
        </daily-statistic-table>

        <daily-statistic-table
                @get-cell-data="showDescriptionByType"
                tableHeader="Детализация по видам платежей"
                :dateArray="dateArray"
                :dataList="detailsList"
                showSummary="true">
        </daily-statistic-table>

        <daily-statistic-table
                @get-cell-data="showDescriptionByStation"
                tableHeader="Затраты по станциям"
                :dateArray="dateArray"
                :dataList="expensesByStation"
                showSummary="true">
        </daily-statistic-table>

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
            },
            payerCode(){
                return this.$store.state.payerCode;
            }

        },

        methods:{
            ...mapActions(['getDailyStatisticAction']),
            ...mapMutations(['setDailyStatisticPeriod']),
            submitForm(){
                const params = {
                    dateFrom: this.dateFrom,
                    dateUntil: this.dateUntil,
                    payerCode: this.payerCode
                };
                this.$store.commit('setDailyStatisticPeriod',params)
                this.getDailyStatisticAction(params)
            },
            showDescriptionByType(data){
                const route = {
                    name: 'payment-details',
                    params: {
                        redirectParams:{
                            payerCode: this.payerCode,
                            dateFrom: data.day,
                            dateUntil: data.day,
                            paymentType: data.key
                        }
                    }
                }
                this.$router.push(route);
            },
            showDescriptionByStation(data){
                const stCode = data.key.match(/\d{6}/i)[0]
                const route = {
                    name: 'payment-details',
                    params: {
                        redirectParams:{
                            payerCode: this.payerCode,
                            dateFrom: data.day,
                            dateUntil: data.day,
                            stationCode: stCode
                        }
                    }
                }
                this.$router.push(route);

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

        watch:{
            payerCode(newVal){
                alert("Код плательщика изменен на: " + newVal)
                this.submitForm()
            }
        },
        filters:{

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