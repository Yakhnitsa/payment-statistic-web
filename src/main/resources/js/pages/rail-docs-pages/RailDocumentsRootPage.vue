<template>
    <div class="wrapper" :style="appContentStyle">
        <!-- Sidebar  -->
        <sidebar></sidebar>

        <!-- Page Content  -->
        <div class="content-bar">
            <button type="button"
                    class="sidebar-toggle-button btn btn-info"
                    :style="toggleButtonStyle"
                    @click="toggleSidebar">
                <i class="fas fa-align-left"></i>
                <!--<span>Отобразить/скрыть</span>-->
            </button>
            <div class="router-container">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import Sidebar from './Sidebar.vue';
    import MessageManager from '../../shared/services/messageManager';

    export default {
        name: "DocumentsPage",
        components:{
            Sidebar
        },
        computed:{
            ...mapGetters(['inDeveloperMode'],{

            }),
            mainBarStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 10 + 'px';

                return {
                    'margin-top' : marginTop,
                }
            },
            toggleButtonStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 5 + 'px';
                return {
                    top: marginTop,

                }
            },
            appContentStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                return {
                    'margin-top': headerSize + 'px',
                }
            },



        },
        methods:{
            toggleSidebar(){
                $('#sidebar').toggleClass('active');
            },
        },
    }
</script>

<style scoped>
    @import "../../css/mainbar_style.css";

</style>