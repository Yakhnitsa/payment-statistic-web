<template>
    <div>
        <ul class="list-group">
            <!--<upload-file-component v-for="file in files" :file="file"></upload-file-component>-->
            <li class="list-group-item"
                v-for="file in files">
                <input
                        type="checkbox"
                        :checked="file.selected"
                        @change="selectFile(file)"
                >
                <span :class="getFileTypeStyle(file)"></span>
                <span>{{file.name}}</span>
                <span v-if="file.uploaded"
                      class="fa fa-check-circle-o"
                ></span>

                <span v-if="file.uploaded" class="far fa-check-circle text-success"></span>
                <span>{{ file.selected }}</span>
                <span>{{ file.uploaded }}</span>
                <!--<button class="btn btn-secondary" @click="test(file)">test</button>-->
            </li>
        </ul>
    </div>
</template>

<script>
    import UploadFileComponent from "./UploadFileComponent.vue";
    export default {
        name: "FilesTable",
        components: {UploadFileComponent},
        props: ['files'],
        computed: {
            xmlFiles() {
                return this.files.filter(file => file.name.toLowerCase().endsWith('.xml'));
            },
            pdfFiles() {
                return this.files.filter(file => file.name.toLowerCase().endsWith('.pdf'))
            },
            uploadedFiles(){
                return this.files.filter(file => file.uploaded);
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
            hasValidFormat(file){
                const filename = file.name.toLowerCase();
                return  filename.endsWith('.xml') ? true : filename.endsWith('.pdf');
            },
            selectFile(file){
                console.log(file.selected);
                this.$store.commit('uploadStore/setFileSelectedMutation',file);
            }

        }
    }
</script>

<style scoped>

    .list-group-item{
        padding: .15rem 0.75rem;
    }

</style>