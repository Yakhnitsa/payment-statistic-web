import Vue from "vue";

export default{
    showSecurityException403(){
        "У вас нет прав для совершения действия, 403"
    },
    showNotFoundException404() {
        alert("Запрашиваемый ресурс не найден, 404")
    },
    showOnLoadException(filename){
        alert("Ошибка при загрузке файла: " + filename)
    },
    showSuccessfullyUploadedDocs(docs) {
        console.log('susessfuly uploaded: ' + docs.length + ' documents');
    },
    testMessage(message){
        Vue.$toast("My toast content", {

        });
    },
}
