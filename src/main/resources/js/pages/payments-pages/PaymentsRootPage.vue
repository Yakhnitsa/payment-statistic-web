<template>
    <div class="wrapper" :style="appContentStyle">
        <!-- Sidebar  -->
        <sidebar></sidebar>

        <!-- Page Content  -->
        <div class="content-bar">
            <button type="button"
                    class="sidebar-toggle-button btn btn-info"
                    :style="toggleButtonStyle"
                    @click="toggleSidebar"
                    id="sidebarCollapse">
                <i class="fas fa-align-left"></i>
                <!--<span>Отобразить/скрыть</span>-->
            </button>
            <div class="router-container mt-2">
                <router-view></router-view>
            </div>

        </div>
    </div>
</template>

<script>

    import Sidebar from './Sidebar.vue'
    import {mapGetters} from 'vuex'

    export default{
        components:{
            Sidebar
        },
        data: function() {
            return{

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

<style>

    @import "../../css/mainbar_style.css";

</style>