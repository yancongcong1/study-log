const path = require('path');

module.exports = {
    context: path.resolve(__dirname, '../'),
    // entry: './src/index.js',
    // entry: ['./src/index.js', './src/print.js'],
    // entry: {
    //     index: './src/index.js',
    //     print: './src/print.js'
    // },
    entry: () => ['./src/index.js', './src/print.js'],
    output: {
        filename: '[name].bundle.js',
        path: path.resolve('dist')
        // __dirname是node中的全局变量，表示当前模块的为文件夹的绝对路径，webpack.conf.js的__dirname为D://....../lesson2
    }
};