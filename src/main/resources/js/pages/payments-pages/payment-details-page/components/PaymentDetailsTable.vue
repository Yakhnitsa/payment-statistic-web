<template>
    <div class="scrollable-table">
        <table id="payments-table" class="table table-fixed table-sm table-striped">
            <thead class="thead-light">
            <tr class="text-center">
                <th>Перечень</th>
                <th>Дата</th>
                <th>Тип платежа</th>
                <th>(код) Станция</th>
                <th>Документ</th>
                <th>Оплачено</th>
                <th>НДС</th>
                <th>Всего с НДС</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in paymentDetails">
                <td class="text-center">{{item.paymentListNumber}}</td>
                <td class="text-center">{{item.date | formatDate}}</td>
                <td class="text-left">{{item.type}}</td>
                <td class="text-left">({{item.stationCode}}) {{item.stationName}}</td>
                <td class="text-center">{{item.documentNumber}}</td>
                <td class="text-right">{{item.payment | formatPayment}}</td>
                <td class="text-right">{{item.taxPayment | formatPayment}}</td>
                <td class="text-right">{{item.totalPayment | formatPayment}}</td>
            </tr>
            </tbody>
        </table>

    </div>

</template>

<script>
    import PaymentDetailsRequestForm from "./PaymentDetailsRequestForm.vue";
    export default {
        name: "PaymentDetailsTable",
        components: {PaymentDetailsRequestForm},
        computed:{
            paymentDetails(){
                return this.$store.state.paymentDetailsPage.paymentDetails
            }
        }
    }
</script>

<style scoped>
    .scrollable-table {
        width: 100%;
        max-height: 40em;
        overflow-y: scroll;
        position: relative;
    }

    table {
        max-width: 100%;
    }

    thead th {
        position: -webkit-sticky;
        position: sticky;
        top: 0;
        background-color: var(--header-bg-color);
        z-index: 1;
    }

    .sticky-first-column {
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        left: 0;
        min-width: 8em;
        max-width: 8em;
        white-space: nowrap;
        background-color: var(--header-bg-color);
    }
    th.sticky-first-column{
        z-index: 2;
    }

    .sticky-second-column {
        font-weight: 500;
        position: -webkit-sticky; /* for Safari */
        position: sticky;
        left: 8em;
        max-width: 8em;
        white-space: nowrap;
        background-color: var(--header-bg-color);
    }
    th.sticky-second-column{
        z-index: 2;
    }

    td, th{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 15em;
    }

    td:hover{
        white-space: normal;
        font-weight: 500;
    }

</style>