const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = {
    // entry: path.join(__dirname, 'src', 'main', 'resources', 'js', 'payments.js'),
    entry: {
        payments: './payments.js',
        documents: './documents.js'
        // about: './about.js',
        // contact: './contact.js'
    },
    context: path.resolve(__dirname, 'src', 'main', 'resources', 'js'),
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ]
            }
        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    //Разрешение для импортов (Необходимо для импорта компонентов Vue.js
    resolve: {
        modules: [
            path.join(__dirname, 'src', 'main', 'resources', 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    },
    optimization: {
        splitChunks: {
            cacheGroups: {
                // moment: {
                //     test: /[\\/]node_modules[\\/]moment[\\/]/,
                //     name: 'moment',
                //     chunks: 'all',
                // },
                // // отделяет в отдельный файл содержимое node_modules/chart.js/dist
                // chart: {
                //     test: /[\\/]node_modules[\\/]chart.js[\\/]/,
                //     name: 'chartjs',
                //     chunks: 'all',
                // },
                //отделяет в отдельный файл содержимое node_modules/vue
                vuelib: {
                    test: /[\\/]node_modules[\\/]vue[\\/]/,
                    name: 'vuelib',
                    chunks: 'all',
                },
                corejs: {
                    test: /[\\/]node_modules[\\/]core-js[\\/]/,
                    name: 'core-js',
                    chunks: 'all',
                },
                // sockjs:{
                //     test: /[\\/]node_modules[\\/]sockjs-client[\\/]/,
                //     name: 'sockjs',
                //     chunks: 'all',
                // }
            }

        }
    },
};