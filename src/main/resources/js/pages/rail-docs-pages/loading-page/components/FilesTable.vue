<template>
    <div>
        <ul class="list-group">
            <div class="sticky-top">
                <li class="sticky-top list-group-item list-group-item-primary">
                    <input type="checkbox" v-model="allSelected"/>
                    <span>Всего файлов</span>
                    <span class="badge badge-light">{{files.length}}</span>
                    <span>Выбрано файлов</span>
                    <span class="badge badge-light">{{selectedFiles.length}}</span>
                </li>
            </div>

            <!--<upload-file-component v-for="file in files" :file="file"></upload-file-component>-->

            <li class="list-group-item"
                v-for="file in files">
                <input
                        :disabled="!hasValidFormat(file)"
                        type="checkbox"
                        :checked="file.selected"
                        @change="selectFile(file)"
                >
                <span :class="getFileTypeStyle(file)"></span>
                <span v-if="file.uploaded" class="far fa-check-circle text-success"></span>
                <span :class="fileNameClass(file)">{{file.name}}</span>
            </li>
        </ul>
    </div>
</template>

<script>

    import {mapGetters} from 'vuex';

    export default {
        name: "FilesTable",
        components: {},
        computed: {
            ...mapGetters({
                files: 'uploadStore/files',
                selectedFiles:'uploadStore/selectedFiles',
            }),
            xmlFiles() {
                return this.files.filter(file => file.name.toLowerCase().endsWith('.xml'));
            },
            pdfFiles() {
                return this.files.filter(file => file.name.toLowerCase().endsWith('.pdf'))
            },
            uploadedFiles(){
                return this.files.filter(file => file.uploaded);
            },

            allSelected:{
                get(){
                    return this.files.length === this.selectedFiles.length;
                },
                set(newValue){
                    this.files.forEach(file => {
                        if (file.selected !== newValue) { this.selectFile(file) }
                    })
                }
            }
        },
        methods: {

            getFileTypeStyle(file){
                const filename = file.name.toLocaleLowerCase();
                if(filename.endsWith('.xml')){
                    return 'far fa-file-code'
                }else if(filename.endsWith(".pdf")){
                    return 'far fa-file-pdf'
                }else return 'fa fa-file-alt text-danger'

            },
            fileNameClass(file){
                return file.uploaded ? 'uploaded-file' :
                    this.hasValidFormat(file) ? 'valid-file-format' : 'invalid-file-format';
            },
            hasValidFormat(file){
                const filename = file.name.toLowerCase();
                return  filename.endsWith('.xml') ? true : filename.endsWith('.pdf');
            },
            selectFile(file){
                if(this.hasValidFormat(file)){
                    this.$store.commit('uploadStore/setFileSelectedMutation',file);
                }

            },


        }
    }
</script>

<style scoped>
    .valid-file-format{

    }
    .invalid-file-format{
        text-decoration: line-through;
        color: indianred;

    }
    .uploaded-file{
        font-style: italic;
        color: gray;
    }

    .list-group-item{
        padding: .15rem 0.75rem;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .sticky-top{
        background-color: whitesmoke;
    }
    .list-group-item:hover{
        white-space: normal;
    }

</style>