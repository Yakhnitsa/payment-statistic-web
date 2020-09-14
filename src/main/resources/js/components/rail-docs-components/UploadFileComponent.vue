<template>
    <li class="list-group-item">
        <span :class="fileTypeStyle"></span>
        <span>{{file.name}}</span>
        <i v-if="file.uploaded"
           class="fa fa-check-circle-o"
        ></i>
        <input type="checkbox" v-model="file.selected">
        <span>{{ file.selected }}</span>
        <span>{{ file.uploaded }}</span>
        <button class="btn btn-secondary" @click="test">test</button>
    </li>
</template>

<script>
    export default {

        name: "UploadFileComponent",
        props:['file'],
        computed:{
            fileTypeStyle(){
                const filename = this.file.name.toLocaleLowerCase();
                if(filename.endsWith('.xml')){
                    return 'far fa-file-code'
                }else if(filename.endsWith(".pdf")){
                    return 'far fa-file-pdf'
                }else return 'fa fa-file-alt text-danger'
            },
        },
        methods:{
            test(){
                console.log(this.file);
                console.log(this.file.uploaded);
            }
        },
        mounted(){
             // this.$set(this.file, 'selected', false);
             // this.$set(this.file, 'uploaded', false);
        }
    }
</script>

<style scoped>

</style>