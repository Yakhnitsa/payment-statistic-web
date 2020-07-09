import Vue from 'vue'


Vue.filter('formatPayment', numb => {
    numb = numb/100;
    return (
        numb
            .toFixed(2) // always two decimal digits
            .replace('.', ',') // replace decimal point character with ,
            .replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1 ') // use ' ' as a separator
    )
});

Vue.filter('formatDate', date => {
    return new Date(date).toLocaleDateString()
});