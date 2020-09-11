<template>
    <div>
        <ul class="list-group">
            <li class="list-group-item"
                v-for="file in files">
                <span :class="getFileTypeStyle(file)"></span>
                <span>{{file.name}}</span>
            </li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: "FilesTable",
        props: ['files'],
        computed: {
            xmlFiles() {
                return [...this.files].filter(file => file.name.toLowerCase().endsWith('.xml'));
            },
            pdfFiles() {
                return [...this.files].filter(file => file.name.toLowerCase().endsWith('.pdf'))
            }
        },
        methods: {
            getPdfForXml(xmlFile) {

            },
            getFileTypeStyle(file){
                const filename = file.name.toLocaleLowerCase();
                if(filename.endsWith('.xml')){
                    return 'far fa-file-code'
                }else if(filename.endsWith(".pdf")){
                    return 'far fa-file-pdf'
                }else return 'fa fa-file-alt text-danger'

            },
            hasValidFormat(file){
                const filename = file.name.toLowerCase();
                return  filename.endsWith('.xml') ? true : filename.endsWith('.pdf');
            }
        }
    }
</script>

<style scoped>

    .list-group-item{
        padding: .15rem 0.75rem;
    }

</style>