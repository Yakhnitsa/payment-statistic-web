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
                <!--<span>Отобразить/скрыть</span>-->
            </button>
            <div class="container-fluid mt-2">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapGetters} from 'vuex';
    import Sidebar from './Sidebar.vue';

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
                    position : 'sticky',
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
            }
        },
    }
</script>

<style scoped>

    .wrapper {
        display: flex;
        align-items: stretch;
    }

    #content {
        width: 100%;
        padding: 5px;
        min-height: 100vh;
        transition: all 0.3s;
        overflow: hidden;
    }

</style>