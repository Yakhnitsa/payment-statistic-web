const merge = require('webpack-merge');
const common = require('./webpack.common.js');

const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const bungleAnalyzerOptions = {
    analyzerPort: 8081,
};

module.exports = merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8000,
        allowedHosts: [
            'localhost:9000'
        ],
        // настройки логирования
        stats: 'errors-only',
        clientLogLevel: 'error'
    },

    plugins: [
        new BundleAnalyzerPlugin()
    ],
});

