<template>
    <div>
        <div class="scrollable-table">
            <table class="table table-striped table-hover table-sm">
                <thead>
                <tr>
                    <th class="sticky-first-column text-center">Відправник</th>
                    <th class="text-center">ст. Відправлення</th>
                    <th class="text-center">Дата</th>
                    <th class="text-center">№ Накладної</th>
                    <th class="text-center">№ Вагону</th>
                    <th class="text-center">[ ]</th>
                    <th class="text-center">ст Призначення</th>
                    <th class="text-center">Отримувач</th>
                    <th class="text-center">Вантаж</th>
                    <th class="text-center">Тара</th>
                    <th class="text-center">Нетто</th>
                    <th class="text-center">Додаткова інформація</th>
                </tr>
                </thead>
                <tbody>
                    <template v-for="doc in railroadDocuments">
                        <tr v-for="vagon in doc.vagonList">
                            <td class="text-capitalize">{{doc.cargoSender | formatClient}}</td>
                            <td class="text-capitalize">{{doc.sendStation | formatStation}}</td>
                            <td>{{doc.docDate | formatDate}}</td>
                            <td>{{doc.docNumber}}</td>
                            <td>{{vagon.number}}</td>
                            <td>
                                <input
                                        type="checkbox"
                                        :checked="hasCert(vagon)"
                                        @change="setCertSelected(vagon)"
                                >
                            </td>
                            <td class="text-capitalize">{{doc.receiveStation | formatStation}}</td>
                            <td class="text-capitalize">{{doc.cargoReceiver | formatClient}}</td>
                            <td>({{doc.cargoCode}}) {{doc.cargoName}}</td>
                            <td>{{vagon.tareWeight}}</td>
                            <td>{{vagon.netWeight}}</td>
                        </tr>
                    </template>
                <!--<tr v-for="(value ,key) in railroadDocuments">-->
                    <!--<tr>-->
                    <!--<tr>-->
                    <!--<td class="sticky-first-column text-center">{{value.docNumber}}</td>-->
                    <!--<td class="sticky-second-column text-center">{{value.dateStamp | formatDate}}</td>-->
                    <!--<td class="text-capitalize">{{value.sendStation | formatStation}}</td>-->
                    <!--<td class="text-capitalize">{{value.receiveStation | formatStation}}</td>-->
                    <!--<td class="text-capitalize">{{value.cargoSender | formatClient}}</td>-->
                    <!--<td class="text-capitalize">{{value.cargoReceiver | formatClient}}</td>-->
                    <!--<td class="text-capitalize">({{value.cargoCode}}) {{value.cargoName}}</td>-->
                    <!--<td class="text-right">{{value.vagonCount}}</td>-->
                    <!--<td class="text-right">{{value.fullWeight | formatPayment}}</td>-->
                <!--</tr>-->
                </tbody>
            </table>
        </div>

    </div>
</template>

<script>

    import { createNamespacedHelpers } from 'vuex';
    const { mapActions, mapMutations, mapGetters } = createNamespacedHelpers('certStore');
    export default {
        name: "QualityCertTable",
        // props:['railroadDocuments'],
        computed:{
            ...mapGetters({
                railroadDocuments: 'documents',
            })
        },

        methods:{
            ...mapMutations(['addChangesMutation']),
            addChanges(event){
                console.log(event);
            },
            setCertSelected(vagon){
                const hasCert = vagon.vagonInfo ? !vagon.vagonInfo.hasCert : true;
                const vagonId = vagon.id;
                const changes = {
                    hasCert: hasCert
                };
                this.addChangesMutation({vagonId, changes});
            },
            // addChanges(vagonId,changes){
            //     this.addChangesMutation({vagonId, changes});
            // }
            hasCert(vagon){
                return vagon.vagonInfo ? vagon.vagonInfo.hasCert : false;
            }
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

    :root{
        --header-bg-color: #d5e7e7;
    }

    .scrollable-table {
        max-width: 100%;
        max-height: 30em;
        overflow: scroll;
        position: relative;
    }

    table {
        font-size: .9em;
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