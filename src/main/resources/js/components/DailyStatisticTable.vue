<template>
    <div class="container-fluid ml-0">
        <h3>{{tableHeader}}</h3>
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
                        <th  v-show="showSummary" class="sticky-right-col text-center">Сумма</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(value ,key) in dataList">
                        <td class="zui-sticky-col pl-3">{{key}}</td>
                        <td v-for="day in dateArray"
                            @dblclick="handleClickOnCell($event,{key,day})"
                            :class="{ 'font-weight-lighter' : getDataFromList(value,day) === '' }"
                            class="text-right text-nowrap">
                            {{getDataFromList(value,day) | formatPayment}}
                        </td>
                        <td v-show="showSummary" class="sticky-right-col right">{{getSummaryFromList(value) | formatPayment}}</td>
                    </tr>

                    </tbody>
                </table>
            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: "DailyStatisticTable",
        props: ['tableHeader','dateArray','dataList','showSummary'],
        methods:{
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
                    if(list.dateStamp === day){
                        return list[prop];
                    }
                }

                return 0;
            },
            handleClickOnCell(event, data){
                this.$emit('get-cell-data',data)
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
    .text-center {
        text-align: center;
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

    .sticky-right-col{
        position: sticky;
        right: 0px;
        background-color: aliceblue;
        border-left: solid 2px #DDEFEF;
        /*border-right: solid 2px #DDEFEF;*/

    }
    .font-weight-lighter{
        color: lightgrey!important;
    }

</style>