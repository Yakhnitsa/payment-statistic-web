<template>

    <div class="container-fluid">
        <table id="payments-table" class="table table-fixed table-sm table-striped">
            <thead class="thead-light">
            <tr class="text-center">
                <th scope="col" class="col-1">Перечень</th>
                <th scope="col" class="col-1">Дата</th>
                <th scope="col" class="col-3">Тип платежа</th>
                <th scope="col" class="col-3">(код) Станция</th>
                <th scope="col" class="col-2">Документ</th>
                <th scope="col" class="col-2">Всего</th>

            </tr>
            </thead>
            <tbody>
            <tr class="text-right text-nowrap" v-for="item in paymentDetails">
                <td scope="col" class="col-1 text-center">{{item.paymentListNumber}}</td>
                <td scope="col" class="col-1 text-center">{{item.dateStamp | formatDate}}</td>
                <td scope="col" class="col-3 text-left">{{item.type}}</td>
                <td scope="col" class="col-3 text-left">({{item.stationCode}}) {{item.stationName}}</td>
                <td scope="col" class="col-2 text-center">{{item.documentNumber}}</td>
                <td scope="col" class="col-2 text-right">{{item.totalPayment | formatPayment}}</td>
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
    .table-fixed thead {
        width: 100%;
    }
    .table-fixed tbody {
        height: 400px;
        overflow-y: auto;
        width: 100%;
    }
    .table-fixed thead, .table-fixed tbody, .table-fixed tr, .table-fixed td, .table-fixed th {
        display: block;
    }
    .table-fixed tbody td, .table-fixed thead > tr> th {
        float: left;
        border-bottom-width: 0;
    }
    td{
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    td:hover {
        overflow: inherit;
        text-overflow: inherit;
        white-space: inherit;
        word-wrap: inherit;
    }

</style>