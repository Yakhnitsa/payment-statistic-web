import axios from 'axios'

export default {
    getStations: () => axios.get('/api/stations'),
}
