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
    }
};