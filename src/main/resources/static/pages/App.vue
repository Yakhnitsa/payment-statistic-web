<template>
    <div>


        <h1>Перечни УТЛ-2</h1>

        <p>{{message}}</p>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loadingPage">
            Загрузить перечни
        </button>
        <!-- Modal -->
        <div class="modal fade" id="loadingPage" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalLabel">Загрузка перечней</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <loading-window :loadedPayments="loadedPayments"></loading-window>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary">Выбрать файлы</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        <button type="button" class="btn btn-primary">Сохранить перечни в БД</button>
                    </div>
                </div>
            </div>
        </div>
        <daily-statistic></daily-statistic>
        <payment-list :payments="payments"></payment-list>
    </div>
</template>

<script>
    import axios from 'axios'
    import DailyStatistic from '../components/DailyStatistic.vue'
    import PaymentList from '../components/PaymentsList.vue'
    import LoadingWindow from '../components/LoadingWindow.vue'

    export default{
        components:{
            DailyStatistic,
            PaymentList,
            LoadingWindow
        },
        data: function() {
            return{
                message: 'Yahooo!!!, эта хрень еще и работает!!!',
                payments:[],
                loadedPayments:[],
            }
        },
        created: function(){
            axios.get('/payments')
                .then(response => {
                    console.log(response)
                    response.data.forEach( payment => this.payments.push(payment))
                }

            )
        }
    }

</script>

<style>

</style>