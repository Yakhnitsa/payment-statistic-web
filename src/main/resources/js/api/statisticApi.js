import axios from 'axios'

export default{
    getDailyStatistic: params => axios.get('/api/statistic/daily-statistic',{params}),
    getDailyChart: params => axios.get('/api/statistic/daily-chart',{params}),
    getAnnualChart: params => axios.get('/api/statistic/annual-chart',{params})

}