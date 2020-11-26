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
            timeout: 6000,
            showCloseButtonOnHover: false,

        });
    },
    showWarningMessage(message){
        Vue.$toast.warning(message, {
            timeout: 5000,
            showCloseButtonOnHover: false,
        });
    },
    showInfoMessage(message){
        Vue.$toast.info(message, {
            timeout: 2000,
            showCloseButtonOnHover: false,
            hideProgressBar: true,
        });
    },
    showSuccessMessage(message){
        Vue.$toast.success(message, {
            timeout: 3000,
            showCloseButtonOnHover: false,
            hideProgressBar: true,
        });
    },

    testMessage(message){
        Vue.$toast("My toast content", {

        });
    },
}
