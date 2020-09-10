<template>
    <div class="container-fluid app-content" :style="mainBarStyle">
        <h1>Railroad documents page</h1>
        <div class="container-fluid">
            <router-link to="/">main page</router-link>
            <router-link to="/certificates">cert page</router-link>
            <router-link to="/upload">upload</router-link>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default {
        name: "DocumentsPage",
        computed:{
            mainBarStyle(){
                const headerSize = document.querySelector('#site-header').scrollHeight;
                const marginTop = headerSize + 10 + 'px';

                return {
                    'margin-top' : marginTop,
                }
            }
        }
    }
</script>

<style scoped>

</style>