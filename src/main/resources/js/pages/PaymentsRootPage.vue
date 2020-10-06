<template>
    <div class="app-content" :style="appContentStyle">
        <div class="position-fixed sidebar">
            <sidebar :sidebarSize.sync="sidebarSize"></sidebar>
        </div>
        <div class="mainbar" :style="mainBarStyle">
            <router-view></router-view>
        </div>

    </div>
</template>

<script>
    import DailyStatistic from './DailyStatistic.vue'
    import PaymentList from './payments-pages/payment-list-page/PaymentsList.vue'
    import LoadingWindow from './payments-pages/payment-list-page/components/LoadingWindow.vue'
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
                sidebarSize: "60px"
            }
        },
        computed:{
            ...mapGetters(['inDeveloperMode'],{
                
            }),
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

            appContentStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 10 + 'px';
                return{
                    position: 'fixed',
                    top: marginTop,
                }
            },
            mainBarStyle(){
                return {
                    'margin-left' : this.sidebarSize
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

        created(){
            this.paymentCodes = paymentCodes;
            this.setDefaultCode();
        },
        mounted(){

        }

    }

</script >

<style scoped>

    .app-content {
        width: 100%;
        padding-left: 5px;
    }

</style>