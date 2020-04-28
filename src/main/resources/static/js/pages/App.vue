<template>
    <div>
        <!--TODO Настроить Header с путями к разным данным приложения
            - Ежедневная статистика
            - Список перечней
            - Окно загрузки перечней
            - Прочья хрень
        -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top">
            <a class="navbar-brand" href="#">ЖД статистика</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                <div class="navbar-nav">
                    <router-link to="/daily-statistic" class="nav-item nav-link">Статистика по дням</router-link>
                    <router-link to="/payments" class="nav-item nav-link">Список перечней</router-link>
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
            LoadingWindow
        },
        data: function() {
            return{
                payments:[],
                loadedPayments:[],
            }
        },
        created: function(){

        },
        methods:{
            test(event){
                console.log(event)
            },
        }
    }

</script >

<style scoped>
    .app-content {
        margin-top: 80px
    }
</style>