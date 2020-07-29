<template>
    <div>
        <!--TODO Настроить Header с путями к разным данным приложения
            - Ежедневная статистика
            - Список перечней
            - Окно загрузки перечней
            - Прочья хрень
        -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">

            <select class="selectpicker btn btn-light show-tick" v-model="payerCode">
                <option v-for="code in paymentCodes">{{code}}</option>
            </select>

            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                <div class="navbar-nav">
                    <router-link to="/" class="nav-item nav-link">Графики затрат</router-link>
                    <router-link to="/daily-statistic" class="nav-item nav-link">Статистика по дням</router-link>
                    <router-link to="/payments" class="nav-item nav-link">Список перечней</router-link>
                    <router-link to="/test" class="nav-item nav-link">Test</router-link>
                    <!--<a class="nav-item nav-link" href="#">Pricing</a>-->
                    <!--<a class="nav-item nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Disabled</a>-->
                </div>
            </div>
        </nav>

        <div class="app-content">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import DailyStatistic from './DailyStatistic.vue'
    import PaymentList from './PaymentsList.vue'
    import LoadingWindow from '../components/LoadingWindow.vue'


    export default{
        components:{
            DailyStatistic,
            PaymentList,
            LoadingWindow,
        },
        data: function() {
            return{
                paymentCodes:[],
                payments:[],
                loadedPayments:[],
            }
        },
        computed:{
            payerCode:{
                get() {
                    return this.$store.state.payerCode;
                },
                set(code){

                    this.$store.commit('setPayerCodeMutation',code)
                }
            }
        },
        created: function(){
            this.paymentCodes = paymentCodes;
            this.setDefaultCode()
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
        }
    }

</script >

<style scoped>
    .app-content {
        margin-top: 80px
    }
</style>