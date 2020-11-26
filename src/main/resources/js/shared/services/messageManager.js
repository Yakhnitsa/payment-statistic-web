import Vue from "vue";

export default{
    showSecurityException403(){
        this.showErrorMessage("У вас нет прав для совершения действия, 403");
    },
    showNotFoundException404() {
        this.showErrorMessage("Запрашиваемый ресурс не найден, 404");
    },
    showOnLoadException(filename){
        this.showErrorMessage("Ошибка при загрузке файла: " + filename)
    },
    showSuccessfullyUploadedDocs(docs) {
        this.showInfoMessage('Успешно загружено: ' + docs.length + ' файлов');
    },


    showErrorMessage(message){
        Vue.$toast.error(message, {
            timeout: 5500,
            showCloseButtonOnHover: false,
        });
    },
    showInfoMessage(message){
        Vue.$toast.info(message, {
            timeout: 2000,
            showCloseButtonOnHover: false,
        });
    },
    showSuccessMessage(message){
        Vue.$toast.success(message, {
            timeout: 3000,
            showCloseButtonOnHover: false,
        });
    },

    testMessage(message){
        Vue.$toast("My toast content", {

        });
    },
}
