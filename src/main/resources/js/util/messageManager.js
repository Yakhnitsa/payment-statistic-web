export default{
    showSecurityException403: () => alert("У вас нет прав для совершения действия, 403"),
    showNotFoundException404: () => alert("Запрашиваемый ресурс не найден, 404"),
    showOnLoadException: (filename) => alert("Ошибка при загрузке файла: " + filename),
    showSuccessfullyUploadedDocs: (docs) => {
        docs.forEach(doc =>{
            console.log(doc);
        })
    }
}
