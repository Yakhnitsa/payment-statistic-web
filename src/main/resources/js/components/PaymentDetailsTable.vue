<template>
    <div class="container-fluid">
        <table id="payments-table" class="table table-fixed table-sm table-striped">
            <thead class="thead-light">
            <tr class="text-center">
                <th scope="col" class="col-2">№ перечня</th>
                <th scope="col" class="col-2">Тип платежа</th>
                <th scope="col" class="col-2">Дата</th>
                <th scope="col" class="col-2">(код) Станция</th>
                <th scope="col" class="col-2">Документ</th>
                <th scope="col" class="col-2">Всего</th>

            </tr>
            </thead>
            <tbody>
            <tr class="text-right text-nowrap" v-for="item in 40">
                <td scope="col" class="col-2 text-left">{{item}}</td>
                <td scope="col" class="col-2 text-left">some value...</td>
                <td scope="col" class="col-2 text-center">some value...</td>
                <td scope="col" class="col-2 text-left">some value...</td>
                <td scope="col" class="col-2 text-center">some value...</td>
                <td scope="col" class="col-2 text-right">some value...</td>

            </tr>
            </tbody>
        </table>
    </div>

</template>

<script>
    export default {
        name: "PaymentDetailsTable"
    //    TODO запрос перечней из store и отображение в таблице
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
</style>