<template>
    <nav id="sidebar">
        <div class="sidebar-header">
            <h3>Панель навигации</h3>
            <strong>BS</strong>
        </div>

        <ul class="list-unstyled components">
            <li>
                <a href="#payerCodeMenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                    <i class="fa fa-exchange-alt"></i>
                    Код {{payerCode}}
                </a>
                <ul class="collapse list-unstyled" id="payerCodeMenu">
                    <li v-for="code in paymentCodes">
                        <a
                                :class="payerCode === code ? 'active': ''"
                                class="text-center dropdown-item"  @click="payerCode = code">
                            {{code}}
                        </a>
                    </li>
                </ul>
            </li>
            <li>
                <router-link to="/">
                    <i class="fa fa-chart-line"></i>
                    Графики
                </router-link>
            </li>
            <li>
                <router-link to="/daily-statistic">
                    <i class="fa fa-table"></i>
                    Дневная статистика
                </router-link>
            </li>
            <li>
                <router-link to="/payments">
                    <i class="fa fa-th-list"></i>
                    Список перечней
                </router-link>
            </li>
            <li>
                <router-link to="/payment-details">
                    <i class="fa fa-tasks"></i>
                    Детали платежей
                </router-link>
            </li>

        </ul>

    </nav>
</template>

<script>
    import {mapGetters} from 'vuex'

    export default {
        name: "Sidebar",
        data(){
            return{
                collapsed: true,
            }
        },


        methods:{

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
        }


    }
</script>

<style scoped>
    @import "../../css/sidebar_style.css";

    /*#sidebar {*/
         /*min-width: 250px;*/
         /*max-width: 250px;*/
         /*background: #ffffff;*/
         /*color: #1d1d1d;*/
         /*transition: all 0.3s;*/
         /*border-right: 2px solid #a09ea0;*/
     /*}*/

    /*#sidebar.active {*/
        /*min-width: 80px;*/
        /*max-width: 80px;*/
        /*text-align: center;*/
    /*}*/

    /*#sidebar.active .sidebar-header h3,*/
    /*#sidebar.active .CTAs {*/
        /*display: none;*/
    /*}*/

    /*#sidebar.active .sidebar-header strong {*/
        /*display: block;*/
    /*}*/

    /*#sidebar ul li a {*/
        /*text-align: left;*/
        /*color: #636363;*/
        /*text-decoration: none;*/
        /*background-color: transparent;*/
    /*}*/

    /*.dropdown-item.active, .dropdown-item:active {*/
        /*color: #fff;*/
        /*text-decoration: none;*/
        /*background-color: #999799;*/
    /*}*/

    /*#sidebar.active ul li a {*/
        /*padding: 20px 10px;*/
        /*text-align: center;*/
        /*font-size: 0.85em;*/
    /*}*/

    /*#sidebar.active ul li a i {*/
        /*margin-right: 0;*/
        /*display: block;*/
        /*font-size: 1.8em;*/
        /*margin-bottom: 5px;*/
    /*}*/

    /*#sidebar.active ul ul a {*/
        /*padding: 10px !important;*/
    /*}*/

    /*.dropdown-toggle {*/
        /*white-space: normal!important;*/
    /*}*/
    /*#sidebar.active .dropdown-toggle::after {*/
        /*top: auto;*/
        /*bottom: 10px;*/
        /*right: 50%;*/
        /*-webkit-transform: translateX(50%);*/
        /*-ms-transform: translateX(50%);*/
        /*transform: translateX(50%);*/

    /*}*/

    /*#sidebar .sidebar-header {*/
        /*padding: 10px;*/
        /*background: #b9eaea;*/
        /*text-align: center;*/
    /*}*/

    /*#sidebar .sidebar-header strong {*/
        /*display: none;*/
        /*font-size: 1.2em;*/
        /*text-align: center;*/
    /*}*/

    /*#sidebar ul.components {*/
        /*padding: 20px 0;*/
        /*border-bottom: 1px solid #47748b;*/
    /*}*/

    /*#sidebar ul li a {*/
        /*padding: 10px;*/
        /*font-size: 1.1em;*/
        /*display: block;*/
    /*}*/

    /*#sidebar ul li a:hover {*/
        /*color: #1d1d1d;*/
        /*background: #fff;*/
    /*}*/

    /*#sidebar ul li a i {*/
        /*margin-right: 10px;*/
    /*}*/

    /*#sidebar ul li.active>a,*/
    /*a[aria-expanded="true"] {*/
        /*color: #111111;*/
        /*background: #d7d9d9;*/
    /*}*/

    /*a[data-toggle="collapse"] {*/
        /*position: relative;*/
    /*}*/

    /*.dropdown-toggle::after {*/
        /*display: block;*/
        /*position: absolute;*/
        /*top: 50%;*/
        /*right: 20px;*/
        /*transform: translateY(-50%);*/
    /*}*/

    /*ul ul a {*/
        /*font-size: 0.9em !important;*/
        /*padding-left: 30px !important;*/
        /*background: #eef2f0;*/
    /*}*/

    /*ul.CTAs {*/
        /*padding: 20px;*/
    /*}*/

    /*ul.CTAs a {*/
        /*text-align: center;*/
        /*font-size: 0.9em !important;*/
        /*display: block;*/
        /*border-radius: 5px;*/
        /*margin-bottom: 5px;*/
    /*}*/

    /*@media (max-width: 768px) {*/
        /*#sidebar {*/
            /*min-width: 80px;*/
            /*max-width: 80px;*/
            /*text-align: center;*/
            /*margin-left: -80px !important;*/
        /*}*/
        /*.dropdown-toggle::after {*/
            /*top: auto;*/
            /*bottom: 10px;*/
            /*right: 50%;*/
            /*-webkit-transform: translateX(50%);*/
            /*-ms-transform: translateX(50%);*/
            /*transform: translateX(50%);*/
        /*}*/
        /*#sidebar.active {*/
            /*margin-left: 0 !important;*/
        /*}*/
        /*#sidebar .sidebar-header h3,*/
        /*#sidebar .CTAs {*/
            /*display: none;*/
        /*}*/
        /*#sidebar .sidebar-header strong {*/
            /*display: block;*/
        /*}*/
        /*#sidebar ul li a {*/
            /*padding: 20px 10px;*/
        /*}*/
        /*#sidebar ul li a span {*/
            /*font-size: 0.85em;*/
        /*}*/
        /*#sidebar ul li a i {*/
            /*margin-right: 0;*/
            /*display: block;*/
        /*}*/
        /*#sidebar ul ul a {*/
            /*padding: 10px !important;*/
        /*}*/
        /*#sidebar ul li a i {*/
            /*font-size: 1.3em;*/
        /*}*/
        /*#sidebar {*/
            /*margin-left: 0;*/
        /*}*/
        /*#sidebarCollapse span {*/
            /*display: none;*/
        /*}*/
    /*}*/

</style>