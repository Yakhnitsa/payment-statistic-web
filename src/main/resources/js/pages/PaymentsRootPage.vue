<template>
    <div class="wrapper" :style="appContentStyle">
        <!-- Sidebar  -->
        <sidebar></sidebar>

        <!-- Page Content  -->
        <div id="content">
            <button type="button"
                    :style="toggleButtonStyle"
                    @click="toggleSidebar"
                    id="sidebarCollapse" class="btn btn-info">
                <i class="fas fa-align-left"></i>
                <span>Отобразить/скрыть</span>
            </button>
            <div class="container-fluid mt-2">
                <router-view></router-view>
            </div>

        </div>
    </div>
</template>

<script>
    import DailyStatistic from './payments-pages/daily-statistic-page/DailyStatistic.vue'
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
                sidebarSize: "60px",
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
                // const marginTop = headerSize + 10 + 'px';
                return{
                    // position: 'fixed',
                    'margin-top': headerSize + 'px',
                }
            },
            toggleButtonStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 5 + 'px';
                return {
                    top: marginTop,
                    position : 'sticky',
                }
            },

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
            },
            toggleSidebar(){
                $('#sidebar').toggleClass('active');
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

    .wrapper {
        display: flex;
        align-items: stretch;
    }

    .dropdown-toggle {
        white-space: normal!important;
    }

    #content {
        width: 100%;
        padding: 5px;
        min-height: 100vh;
        transition: all 0.3s;
    }

    /* ---------------------------------------------------
        MEDIAQUERIES
    ----------------------------------------------------- */

    @media (max-width: 768px) {

        .dropdown-toggle::after {
            top: auto;
            bottom: 10px;
            right: 50%;
            -webkit-transform: translateX(50%);
            -ms-transform: translateX(50%);
            transform: translateX(50%);
        }
    }

</style>