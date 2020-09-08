<template>
    <div>
        <!--<nav class="navbar navbar-expand-lg navbar-light bg-light">-->

            <!--<select class="selectpicker btn btn-light show-tick" v-model="payerCode">-->
                <!--<option v-for="code in paymentCodes">{{code}}</option>-->
            <!--</select>-->

            <!--<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">-->
                <!--<span class="navbar-toggler-icon"></span>-->
            <!--</button>-->
            <!--<div class="collapse navbar-collapse" id="navbarNavAltMarkup">-->
                <!--<div class="navbar-nav">-->
                    <!--<router-link to="/" class="nav-item nav-link">Графики затрат</router-link>-->
                    <!--<router-link to="/daily-statistic" class="nav-item nav-link">Статистика по дням</router-link>-->
                    <!--<router-link to="/payments" class="nav-item nav-link">Список перечней</router-link>-->
                    <!--<router-link to="/payment-details" class="nav-item nav-link">Детали платежей</router-link>-->
                    <!--<router-link-->
                            <!--v-if="inDeveloperMode"-->
                            <!--:to="{name: 'test', params: {dateFrom: '2020-07-20', dateUntil: '2020-08-03'}}"-->
                            <!--class="nav-item nav-link">Test</router-link>-->
                    <!--&lt;!&ndash;<a class="nav-item nav-link" href="#">Pricing</a>&ndash;&gt;-->
                    <!--&lt;!&ndash;<a class="nav-item nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>&ndash;&gt;-->
                <!--</div>-->
            <!--</div>-->
        <!--</nav>-->
        <div class="position-fixed" :style="sidebarStyle" id="side-bar">
            <sidebar></sidebar>
        </div>
        <div class="app-content" :style="mainBarStyle">
            <router-view></router-view>
        </div>

    </div>
</template>

<script>
    import DailyStatistic from './DailyStatistic.vue'
    import PaymentList from './PaymentsList.vue'
    import LoadingWindow from '../components/LoadingWindow.vue'
    import Sidebar from '../components/Sidebar.vue'
    import {mapGetters} from 'vuex'

    export default{
        components:{
            DailyStatistic,
            PaymentList,
            LoadingWindow,
            Sidebar
        },
        data: function() {
            return{
                paymentCodes:[],
                payments:[],
                loadedPayments:[],

            }
        },
        computed:{
            ...mapGetters(['inDeveloperMode']),
            payerCode:{
                get() {
                    return this.$store.state.payerCode;
                },
                set(code){
                    this.$store.commit('setPayerCodeMutation',code)
                }
            },
            userRoles(){
                return  this.$store.getters.userRoles;
            },
            sidebarStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 10 + 'px'
                return{
                    position: 'fixed',
                    top: marginTop,
                    'z-index': 1
                }
            },
            mainBarStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 10 + 'px';

                return {
                    'margin-top' : marginTop,
                    'margin-left' : '60px'
                }
            }
        },
        methods:{
            test(event){
                console.log(event)
            },
            setDefaultCode(){
                let code = this.$store.state.payerCode;
                if(code === '' && paymentCodes.length > 0){
                    this.$store.commit('setPayerCodeMutation',paymentCodes[0])
                }
            }
        },

        created: function(){
            this.paymentCodes = paymentCodes;
            this.setDefaultCode()

        },
        mounted(){
            const header = document.querySelector('#side-bar');
            console.log(header.clientWidth);

        }

    }

</script >

<style scoped>

    .app-content {
        /*margin-top: 60px;*/
        /*margin-left: 60px;*/
    }

</style>