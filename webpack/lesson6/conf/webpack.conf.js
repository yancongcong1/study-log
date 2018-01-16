const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')

module.exports = {
    context: path.resolve(__dirname, '../'),
    entry: ['./src/index.css', './src/print.js'],
    // entry: () => ['./src/index.js', './src/print.js'],
    output: {
        filename: 'main.bundle.js',
        path: path.resolve('dist'),
        // library: 'Test',
        // libraryExport: "default",
        libraryTarget: 'umd'
        // __dirname是node中的全局变量，表示当前模块的为文件夹的绝对路径，webpack.conf.js的__dirname为D://....../lesson2
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                    { loader: 'style-loader' },
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true
                        }
                    }
                ]
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin(),
        new UglifyJsPlugin()
    ]
};