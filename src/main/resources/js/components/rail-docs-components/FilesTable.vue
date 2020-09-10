<template>
    <div>
        <ul class="list-group">
            <li class="list-group-item"
                v-for="file in files">{{file}}</li>
        </ul>
    </div>
</template>

<script>
    export default {
        name: "FilesTable",
        props:['files']

    }
</script>

<style scoped>

</style>