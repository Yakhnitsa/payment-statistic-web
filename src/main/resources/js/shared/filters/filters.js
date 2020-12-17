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

Vue.filter('formatWeight', numb => {
    numb = numb/1000;
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

Vue.filter('capitalizeText', text =>{
    return text === null ? '':
        text.toLowerCase().replace(/^\w/, (c) => c.toUpperCase());

});

Vue.filter('formatTime', dateString =>{
    const date = new Date(dateString);
    const hours  = date.getHours();
    let minutes = date.getMinutes();
    minutes = minutes < 10 ? '0' + minutes : minutes;
    let seconds = date.getSeconds();
    seconds = seconds < 10? '0' + seconds : seconds;
    return hours + ':' + minutes + ':' + seconds;
});
