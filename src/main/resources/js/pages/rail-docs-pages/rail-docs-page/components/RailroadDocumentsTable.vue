<template>
    <div class="scrollable-table">
        <table class="table table-striped table-hover table-sm">
            <thead>
            <tr>
                <th class="sticky-first-column text-center">№ документа</th>
                <th class="sticky-second-column text-center">Дата</th>
                <th class="text-center">ст. Відправлення</th>
                <th class="text-center">ст. Призначення</th>
                <th class="text-center">Відправник</th>
                <th class="text-center">Отримувач</th>
                <th class="text-center">Вантаж</th>
                <th class="text-center">К-ть вагонів</th>
                <th class="text-center">Маса вантажу</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="document in railroadDocuments">
                <td class="sticky-first-column text-center">
                    <span class="show-on-hover btn btn-outline-secondary btn-sm link-button"
                            @click="downloadPdf(document)">
                        <i class="fa fa-file-pdf"></i>
                    </span>
                    {{document.docNumber}}</td>
                <td class="sticky-second-column text-center">
                    {{document.docDate | formatDate}}
                    <span class="show-on-hover">{{document.docDate | formatTime }}</span>
                </td>
                <td class="text-capitalize">{{document.sendStation | formatStation}}</td>
                <td class="text-capitalize">{{document.receiveStation | formatStation}}</td>
                <td class="text-capitalize">{{document.cargoSender | formatClient}}</td>
                <td class="text-capitalize">{{document.cargoReceiver | formatClient}}</td>
                <td class="text-capitalize">({{document.cargoCode}}) {{document.cargoName}}</td>
                <td class="text-right">{{document.vagonCount}}</td>
                <td class="text-right">{{document.fullWeight | formatPayment}}</td>
                <!--<td class="text-capitalize">{{value.receiveStation | formatStation}}</td>-->
                <!--<td>{{value}}</td>-->
            </tr>
            </tbody>
        </table>

    </div>
</template>

<script>

    import {mapActions} from 'vuex';
    export default {
        name: "RailroadDocumentsTable",
        props:['railroadDocuments'],
        methods:{
            ...mapActions({
                downloadPdf: 'downloadStore/downloadPdfFileAction',
                downloadXml: 'downloadStore/downloadXmlFileAction'
            }),
        },
        filters:{
            formatStation(station){
                return '(' + station.code + ') ' + station.rusName.toLowerCase();
            },
            formatClient(client){
                return '(' + client.railroadCode + ') ' + client.name;
            }
        }
    }
</script>

<style>
    @import "../../../../css/railroad-documents/tables-style.css";

    .show-on-hover{
        display: none;
    }
    td:hover .show-on-hover{
        display: inline-block;
        color: darkslategray;
    }
    .link-button{
        padding: .1rem .35rem;
        font-size: .7rem;
        line-height: 1.5;
        border-radius: 50%!important;
    }

    /*:root{*/
        /*--header-bg-color: #d5e7e7;*/
    /*}*/


    /*.scrollable-table {*/
        /*max-width: 100%;*/
        /*max-height: 30em;*/
        /*overflow: scroll;*/
        /*position: relative;*/
    /*}*/

    /*table {*/
        /*font-size: .9em;*/
    /*}*/

    /*thead th {*/
        /*position: -webkit-sticky;*/
        /*position: sticky;*/
        /*top: 0;*/
        /*border-top-width: 0;*/
        /*background-color: var(--header-bg-color);*/
        /*z-index: 1;*/
    /*}*/
    /*.table th {*/
        /*border-top: 0;*/
    /*}*/

    /*.sticky-first-column {*/
        /*font-weight: 500;*/
        /*position: -webkit-sticky; !* for Safari *!*/
        /*position: sticky;*/
        /*left: 0;*/
        /*min-width: 8em;*/
        /*max-width: 8em;*/
        /*white-space: nowrap;*/
        /*background-color: var(--header-bg-color);*/
    /*}*/
    /*th.sticky-first-column{*/
        /*z-index: 2;*/
    /*}*/

    /*.sticky-second-column {*/
        /*font-weight: 500;*/
        /*position: -webkit-sticky; !* for Safari *!*/
        /*position: sticky;*/
        /*left: 8em;*/
        /*max-width: 8em;*/
        /*white-space: nowrap;*/
        /*background-color: var(--header-bg-color);*/
    /*}*/
    /*th.sticky-second-column{*/
        /*z-index: 2;*/
    /*}*/

    /*td, th{*/
        /*white-space: nowrap;*/
        /*overflow: hidden;*/
        /*text-overflow: ellipsis;*/
        /*max-width: 15em;*/
        /*padding: .1rem .3rem;*/
    /*}*/

    /*td:hover{*/
        /*white-space: normal;*/
        /*font-weight: 500;*/
    /*}*/



</style>