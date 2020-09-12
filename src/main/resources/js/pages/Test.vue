<template>
    <div>
        <div>Test component</div>
        <station-input :station.sync="station" :stations="stations"></station-input>
        <span>Station found:</span>
        <span>{{station}}</span>
        <div>{{dateFrom}}</div>
        <div>{{dateUntil}}</div>
        <div>{{paymentType}}</div>
        <button class="btn btn-outline-secondary" @click="routeTo()">Перейти...</button>
        <router-link
                :to="{name: 'payment-details',
                    params: {
                        redirectParams:{
                            payerCode: '8210260',
                            dateFrom: '2020-07-20',
                            dateUntil: '2020-08-03'
                        }
                    }
                }"
                class="nav-item nav-link">With name test</router-link>

        <router-link
                :to="{path: 'payment-details',
                    params: {
                        hasRedirectParams: true,
                        redirectParams:{
                            payerCode: '8210260',
                            dateFrom: '2020-07-20',
                            dateUntil: '2020-08-03'
                        }
                    }
                }"
                class="nav-item nav-link">With path test</router-link>


    </div>


</template>

<script>
    import StationInput from "../components/StationInput.vue";

    export default {
        components: {StationInput},
        props:['dateFrom','dateUntil','paymentType'],
        data(){
            return{
                station:'',
                options: [
                    {
                        name: 'Duplicate',
                        slug: 'duplicate'
                    },
                    {
                        name: 'Edit',
                        slug: 'edit'
                    },
                    {
                        name: 'Delete',
                        slug: 'delete'
                    }
                ],
                items: [
                    {
                        name: 'Jim',
                        job: 'Salesman'
                    },
                    {
                        name: 'Dwight',
                        job: 'Assistant to the Regional Manager'
                    },
                    {
                        name: 'Pam',
                        job: 'Receptionist'
                    }
                ],
                // stations:[
                //     {
                //         code: 403002,
                //         rusName:'ДАРНИЦА',
                //         ukrName:'ДАРНИЦЯ'
                //
                //     },
                //     {
                //         code: 320401 ,
                //         rusName:'ГРУШКИ',
                //         ukrName:'ГРУШКИ'
                //
                //     },                    {
                //         code: 323804,
                //         rusName:'НОСОВКА',
                //         ukrName:'НОСІВКА'
                //
                //     },                    {
                //         code: 323700,
                //         rusName:'КОБЫЖЧИ',
                //         ukrName:'КОБИЖЧА'
                //
                //     },                    {
                //         code: 323607,
                //         rusName:'БОБРОВИЦЫ',
                //         ukrName:'БОБРОВИЦЯ'
                //
                //     },                    {
                //         code: 322206,
                //         rusName:'ИРПЕНЬ',
                //         ukrName:'ІРПІНЬ'
                //
                //     },                    {
                //         code: 327504,
                //         rusName:'ВИРЕВКА',
                //         ukrName:'ВИРІВКА'
                //
                //     },                    {
                //         code: 326304,
                //         rusName:'ДОЧЬ',
                //         ukrName:'ДОЧ'
                //
                //     },                    {
                //         code: 321705,
                //         rusName:'БУЯН',
                //         ukrName:'БУЯН'
                //
                //     },                    {
                //         code: 324008,
                //         rusName:'НЕЖИН',
                //         ukrName:'НІЖИН'
                //
                //     },
                // ]
            }
        },
        computed:{
            stations(){
                return this.$store.getters.stations
            }
        },
        methods:{
            routeTo(){
                const route = {
                    name: 'payment-details',
                    params: {
                        redirectParams:{
                            payerCode: '8210260',
                            dateFrom: '2020-07-20',
                            dateUntil: '2020-08-03'
                        }
                    }
                }
                this.$router.push(route);
            },

        }
    }
</script>

<style scoped>

</style>