<template>
    <div class="container-fluid ml-0">
        <h3>{{tableHeader}}</h3>
        <div class="table-responsive-md scrollable-table">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th class="text-center">Наименование платежа</th>
                    <th v-for="day in dateArray"
                        class="text-center">
                        {{day | formatDate}}
                    </th>
                    <th v-show="showSummary" class="sticky-right-header text-center">Сумма</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(value ,key) in dataList">
                    <td class="sticky-left-column pl-3">{{key}}</td>
                    <td v-for="day in dateArray"
                        @dblclick="handleClickOnCell($event,{key,day})"
                        :class="{ 'font-weight-lighter' : getDataFromList(value,day) === '' }"
                        class="text-right text-nowrap">
                        {{getDataFromList(value,day) | formatPayment}}
                    </td>
                    <td v-show="showSummary" class="sticky-right-col text-right">{{getSummaryFromList(value) | formatPayment}}</td>
                </tr>

                </tbody>
            </table>
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
    .scrollable-table {
        max-width: 80%;
        max-height: 25em;
        overflow: scroll;
        position: relative;
    }

    table {
        position: relative;
        border-collapse: collapse;
    }

    td,
    th {
        padding: 0.25em;
        font-size: 0.9em;
        color: #555555;
    }

    thead th {
        position: -webkit-sticky;
        position: sticky;
        top: 0;
        background-color: #cee0e0;
        color: #336B6B;
    }

    thead th:first-child {
        left: 0;
        z-index: 1;
        border-right: solid 3px #cfe1e1;
    }

    .sticky-left-column {
        border-right: solid 3px #cfe1e1;
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        left: 0;
        background-color: #DDEFEF;
        max-width: 20em;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .sticky-left-column:hover {
        overflow: auto;
        white-space: pre-wrap;
        text-overflow: inherit;
        word-wrap: inherit;
    }

    .sticky-right-col{
        position: -webkit-sticky;
        position: sticky;
        right: 0;
        font-weight: 500;
        z-index: 1;
        background-color: #DDEFEF;
        border-left: solid 2px #DDEFEF;
    }
    .sticky-right-header{
        position: -webkit-sticky;
        position: sticky;
        right: 0;
        z-index: 2;
    }

    .font-weight-lighter{
        color: lightgrey!important;
    }

    td{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    td:hover {
        overflow: auto;
        white-space: pre-wrap;
        text-overflow: inherit;
        word-wrap: inherit;
    }

</style>