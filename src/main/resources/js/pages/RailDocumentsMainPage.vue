<template>
    <div>
        <div class="app-content" :style="mainBarStyle">
            <h1>Railroad documents page</h1>
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