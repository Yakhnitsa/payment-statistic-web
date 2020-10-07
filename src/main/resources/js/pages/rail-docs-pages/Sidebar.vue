<template>
    <nav id="sidebar">
        <div class="sidebar-header">
            <h3>Панель навигации</h3>
            <strong>BS</strong>
        </div>

        <ul class="list-unstyled components">
            <li>
                <router-link to="/">
                    <i class="fa fa-chart-line"></i>
                    ЖД накладные
                </router-link>
            </li>
            <li>
                <router-link to="/certificates">
                    <i class="fa fa-table"></i>
                    Реестр сертификатов
                </router-link>
            </li>
            <li>
                <router-link to="/upload">
                    <i class="fa fa-th-list"></i>
                    Страница загрузки
                </router-link>
            </li>

        </ul>

    </nav>
</template>

<script>
    export default {
        name: "SideBar"
    }
</script>

<style scoped>
    @import "../../css/sidebar_style.css";

</style>