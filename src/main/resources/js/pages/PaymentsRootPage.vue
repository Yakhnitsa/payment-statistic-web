<template>
    <div class="wrapper" :style="appContentStyle">
        <!-- Sidebar  -->
        <nav id="sidebar">
            <div class="sidebar-header">
                <h3>Панель навигации</h3>
                <strong>BS</strong>
            </div>

            <ul class="list-unstyled components">
                <li>
                    <a href="#payerCodeMenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                        <i class="fa fa-exchange-alt"></i>
                        Код плательщика
                    </a>
                    <ul class="collapse list-unstyled" id="payerCodeMenu">
                        <li v-for="code in paymentCodes">
                            <a
                               :class="payerCode === code ? 'active': ''"
                               class="dropdown-item text-center"  @click="payerCode = code">
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

        <!-- Page Content  -->
        <div id="content">
            <button type="button"
                    :style="toggleButtonStyle"
                    @click="toggleSidebar"
                    id="sidebarCollapse" class="btn btn-info">
                <i class="fas fa-align-left"></i>
                <span>Скрыть панель</span>
            </button>

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
                    // position: 'fixed',
                    'margin-top': marginTop,
                }
            },
            toggleButtonStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 5 + 'px';
                return {
                    top: marginTop,
                    position : 'sticky',
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

    /*
    DEMO STYLE
*/

    @import "https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700";
    body {
        font-family: 'Poppins', sans-serif;
        background: #fafafa;
    }

    p {
        font-family: 'Poppins', sans-serif;
        font-size: 1.1em;
        font-weight: 300;
        line-height: 1.7em;
        color: #999;
    }

    a,
    a:hover,
    a:focus {
        color: inherit;
        text-decoration: none;
        transition: all 0.3s;
    }

    .navbar {
        padding: 15px 10px;
        background: #fff;
        border: none;
        border-radius: 0;
        margin-bottom: 40px;
        box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
    }

    .navbar-btn {
        box-shadow: none;
        outline: none !important;
        border: none;
    }

    .line {
        width: 100%;
        height: 1px;
        border-bottom: 1px dashed #ddd;
        margin: 40px 0;
    }

    i,
    span {
        display: inline-block;
    }

    /* ---------------------------------------------------
        SIDEBAR STYLE
    ----------------------------------------------------- */

    .wrapper {
        display: flex;
        align-items: stretch;
    }

    #sidebar {
        min-width: 250px;
        max-width: 250px;
        background: #7386D5;
        color: #fff;
        transition: all 0.3s;
    }

    #sidebar.active {
        min-width: 80px;
        max-width: 80px;
        text-align: center;
    }

    #sidebar.active .sidebar-header h3,
    #sidebar.active .CTAs {
        display: none;
    }

    #sidebar.active .sidebar-header strong {
        display: block;
    }

    #sidebar ul li a {
        text-align: left;
    }

    #sidebar.active ul li a {
        padding: 20px 10px;
        text-align: center;
        font-size: 0.85em;
    }

    #sidebar.active ul li a i {
        margin-right: 0;
        display: block;
        font-size: 1.8em;
        margin-bottom: 5px;
    }

    #sidebar.active ul ul a {
        padding: 10px !important;
    }

    .dropdown-toggle {
        white-space: normal!important;
    }
    #sidebar.active .dropdown-toggle::after {
        top: auto;
        bottom: 10px;
        right: 50%;
        -webkit-transform: translateX(50%);
        -ms-transform: translateX(50%);
        transform: translateX(50%);

    }

    #sidebar .sidebar-header {
        padding: 20px;
        background: #6d7fcc;
    }

    #sidebar .sidebar-header strong {
        display: none;
        font-size: 1.8em;
    }

    #sidebar ul.components {
        padding: 20px 0;
        border-bottom: 1px solid #47748b;
    }

    #sidebar ul li a {
        padding: 10px;
        font-size: 1.1em;
        display: block;
    }

    #sidebar ul li a:hover {
        color: #7386D5;
        background: #fff;
    }

    #sidebar ul li a i {
        margin-right: 10px;
    }

    #sidebar ul li.active>a,
    a[aria-expanded="true"] {
        color: #fff;
        background: #6d7fcc;
    }

    a[data-toggle="collapse"] {
        position: relative;
    }

    .dropdown-toggle::after {
        display: block;
        position: absolute;
        top: 50%;
        right: 20px;
        transform: translateY(-50%);
    }

    ul ul a {
        font-size: 0.9em !important;
        padding-left: 30px !important;
        background: #6d7fcc;
    }

    ul.CTAs {
        padding: 20px;
    }

    ul.CTAs a {
        text-align: center;
        font-size: 0.9em !important;
        display: block;
        border-radius: 5px;
        margin-bottom: 5px;
    }

    a.download {
        background: #fff;
        color: #7386D5;
    }

    a.article,
    a.article:hover {
        background: #6d7fcc !important;
        color: #fff !important;
    }

    /* ---------------------------------------------------
        CONTENT STYLE
    ----------------------------------------------------- */

    #content {
        width: 100%;
        padding: 20px;
        min-height: 100vh;
        transition: all 0.3s;
    }

    /* ---------------------------------------------------
        MEDIAQUERIES
    ----------------------------------------------------- */

    @media (max-width: 768px) {
        #sidebar {
            min-width: 80px;
            max-width: 80px;
            text-align: center;
            margin-left: -80px !important;
        }
        .dropdown-toggle::after {
            top: auto;
            bottom: 10px;
            right: 50%;
            -webkit-transform: translateX(50%);
            -ms-transform: translateX(50%);
            transform: translateX(50%);
        }
        #sidebar.active {
            margin-left: 0 !important;
        }
        #sidebar .sidebar-header h3,
        #sidebar .CTAs {
            display: none;
        }
        #sidebar .sidebar-header strong {
            display: block;
        }
        #sidebar ul li a {
            padding: 20px 10px;
        }
        #sidebar ul li a span {
            font-size: 0.85em;
        }
        #sidebar ul li a i {
            margin-right: 0;
            display: block;
        }
        #sidebar ul ul a {
            padding: 10px !important;
        }
        #sidebar ul li a i {
            font-size: 1.3em;
        }
        #sidebar {
            margin-left: 0;
        }
        #sidebarCollapse span {
            display: none;
        }
    }

</style>