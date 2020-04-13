<template>
    <div class="zui-wrapper ml-3">
        <div class="zui-scroller">
            <table class="zui-table">
                <thead>
                <tr>
                    <th class="zui-sticky-col">Name</th>
                    <th>Number</th>
                    <th>Position</th>
                    <th>Height</th>
                    <th>Born</th>
                    <th>Salary</th>
                    <th>Prior to NBA/Country</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td class="zui-sticky-col">DeMarcus Cousins</td>
                    <td>15</td>
                    <td>C</td>
                    <td>6'11"</td>
                    <td>08-13-1990</td>
                    <td>$4,917,000</td>
                    <td>Kentucky/USA</td>
                </tr>
                <tr>
                    <td class="zui-sticky-col">Isaiah Thomas</td>
                    <td>22</td>
                    <td>PG</td>
                    <td>5'9"</td>
                    <td>02-07-1989</td>
                    <td>$473,604</td>
                    <td>Washington/USA</td>
                </tr>
                <tr>
                    <td class="zui-sticky-col">Ben McLemore</td>
                    <td>16</td>
                    <td>SG</td>
                    <td>6'5"</td>
                    <td>02-11-1993</td>
                    <td>$2,895,960</td>
                    <td>Kansas/USA</td>
                </tr>
                <tr>
                    <td class="zui-sticky-col">Marcus Thornton</td>
                    <td>23</td>
                    <td>SG</td>
                    <td>6'4"</td>
                    <td>05-05-1987</td>
                    <td>$7,000,000</td>
                    <td>Louisiana State/USA</td>
                </tr>
                <tr>
                    <td class="zui-sticky-col">Jason Thompson</td>
                    <td>34</td>
                    <td>PF</td>
                    <td>6'11"</td>
                    <td>06-21-1986</td>
                    <td>$3,001,000</td>
                    <td>Rider/USA</td>
                    <td class="zui-last-col">Rider/USA</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                stickyHeader: true,
                noCollapse: false,
                fields: [
                    { key: 'id', stickyColumn: true, isRowHeader: true, variant: 'primary' },
                    'a',
                    'b',
                    { key: 'c', stickyColumn: true, variant: 'info' },
                    'd',
                    'e',
                    'f',
                    'g',
                    'h',
                    'i',
                    'j',
                    'k',
                    'l'
                ],
                items: [
                    { id: 1, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 2, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 3, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 4, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 5, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 6, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 7, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 8, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 9, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 },
                    { id: 10, a: 0, b: 1, c: 2, d: 3, e: 4, f: 5, g: 6, h: 7, i: 8, j: 9, k: 10, l: 11 }
                ]
            }
        }
    }
</script>

<style scoped>
    .zui-table {
        /*border: none;*/
        /*border-right: solid 1px #DDEFEF;*/
        border-collapse: separate;
        border-spacing: 1px;
        font: normal 13px Arial, sans-serif;
    }
    .zui-table thead th {
        background-color: #DDEFEF;
        border: none;
        color: #336B6B;
        padding: 10px;
        /*text-align: left;*/
        /*text-shadow: 1px 1px 1px #fff;*/
        white-space: nowrap;
    }
    .zui-table tbody td {
        border-bottom: solid 1px #DDEFEF;
        color: #333;
        padding: 10px;
        /*text-shadow: 1px 1px 1px #fff;*/
        white-space: nowrap;
    }
    .zui-wrapper {
        position: relative;
    }
    .zui-scroller {
        margin-left: 201px;
        overflow-x: scroll;
        overflow-y: visible;
        padding-bottom: 5px;
        width: 500px;
    }
    .zui-table .zui-sticky-col {
        border-left: solid 1px #DDEFEF;
        border-right: solid 1px #DDEFEF;
        left: 0;
        position: absolute;
        top: auto;
        width: 200px;
    }
    .zui-last-col {
        margin-left: 701px;
        border-left: solid 1px #DDEFEF;
        border-right: solid 1px #DDEFEF;
        left: 0;
        position: absolute;
        top: auto;
        width: auto;
    }
</style>