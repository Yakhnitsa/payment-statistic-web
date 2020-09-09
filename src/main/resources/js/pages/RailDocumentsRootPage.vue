<template>
    <div>
        <div class="app-content" :style="mainBarStyle">
            <h1>Railroad documents page</h1>
            <div>
                <router-link to="/">main page</router-link>
                <router-link to="/certificates">main page</router-link>
                <router-view></router-view>
            </div>
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