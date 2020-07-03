import axios from 'axios'

export default{
    getDailyStatistic: params => axios.get('/api/statistic/daily-statistic',{params}),
    getDailyChart: params => axios.get('/api/statistic/daily-chart',{params}),
    getYearChart: params => axios.get('/api/statistic/year-chart',{params})

}