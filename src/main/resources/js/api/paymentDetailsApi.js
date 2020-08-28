import axios from 'axios'

export default{
    getPaymentTypes: () => axios.get('/api/payment-details/payment-types'),
    getPayments: params => axios.get('/api/payment-details',{params}),


}