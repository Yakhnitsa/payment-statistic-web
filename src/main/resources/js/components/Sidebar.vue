<template>
    <div id="sidebar-container"
         :class="collapsed ? 'sidebar-collapsed' : 'sidebar-expanded'"
         :style="{width: sidebarWidth}"
         class="d-none d-md-block">

        <ul class="list-group ">
            <div class="dropright">
                <div class="list-group-item list-group-item-action" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <span class="fa fa-exchange-alt fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Код: {{payerCode}}</span>
                </div>
                <div class="dropdown-menu">
                    <h6 class="dropdown-header">Код плательщика</h6>
                    <div class="dropdown-divider"></div>
                    <a v-for="code in paymentCodes"
                            :class="payerCode === code ? 'active': ''"
                            class="dropdown-item text-center"  @click="payerCode = code">
                        {{code}}
                    </a>
                </div>
            </div>

            <router-link to="/" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-start align-items-center">
                    <span class="fa fa-chart-line fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Графики</span>
                </div>
            </router-link>
            <router-link to="/daily-statistic" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-start align-items-center">
                    <span class="fa fa-table fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Статистика по дням</span>
                </div>
            </router-link>
            <router-link to="/payments" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-start align-items-center">
                    <span class="fa fa-th-list fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Список перечней</span>
                </div>
            </router-link>
            <router-link to="/payment-details" href="#" class="list-group-item list-group-item-action">
                <div class="d-flex w-100 justify-content-start align-items-center">
                    <span class="fa fa-tasks fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Детали платежей</span>
                </div>
            </router-link>

            <li class="logo-separator"></li>

            <a @click="toggleSidebar" class="list-group-item list-group-item-action d-flex align-items-center">
                <div >
                    <span
                            :class="collapsed ? 'fa-angle-double-right': 'fa-angle-double-left'"
                            class="fa fa-fw mr-3"></span>
                    <span v-show="!collapsed" class="menu-collapsed">Скрыть</span>
                </div>
            </a>
        </ul>

    </div>
</template>

<script>
    import {mapGetters} from 'vuex'

    export default {
        name: "Sidebar",
        props:['sidebarSize'],
        data(){
            return{
                collapsed: true,
            }
        },


        methods:{
            toggleSidebar(){
                this.collapsed = !this.collapsed;
                this.$emit('update:sidebarSize', this.sidebarWidth);
            }

        },
        computed:{
            ...mapGetters(['inDeveloperMode','paymentCodes']),
            payerCode:{
                get() {
                    return this.$store.state.payerCode;
                },
                set(code){
                    this.$store.commit('setPayerCodeMutation',code)
                }
            },
            sidebarWidth(){
                return this.collapsed ? '60px' : '230px'
            }

        }


    }
</script>

<style scoped>

    #sidebar-container {
        /*min-height: 100vh;*/
        background: #70fff7;
        padding-right: 2px;
        padding-bottom: 2px;
        padding-top: 2px;
        border-right: 2px black;
    }

    .sidebar-expanded {
        width: 230px;
    }
    .sidebar-collapsed {
        width: 60px;
    }

    /* Menu item*/
    #sidebar-container .list-group a {
        height: 50px;
        color: darkslategray;


    }

    /* Submenu item*/
    #sidebar-container .list-group .sidebar-submenu a {
        height: 45px;
        padding-left: 30px;
        margin-right: 50px;

    }


    /* Separators */
    .sidebar-separator-title {
        background-color: whitesmoke;
        height: 35px;
    }
    .sidebar-separator {
        background-color: red;
        height: 25px;
    }
    .logo-separator {
        background-color: whitesmoke;
        height: 10px;
    }

    /* Closed submenu icon */
    #sidebar-container .list-group .list-group-item[aria-expanded="false"] .submenu-icon::after {
        content: " \f0d7";
        display: inline;
        text-align: right;
        padding-left: 10px;
    }
    /* Opened submenu icon */
    #sidebar-container .list-group .list-group-item[aria-expanded="true"] .submenu-icon::after {
        content: " \f0da";
        display: inline;
        text-align: right;
        padding-left: 10px;
    }
    .active{
        background-color: lightgrey;
    }


</style>