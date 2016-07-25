const path = require('path');
const webpack = require('webpack');

const env = process.env;

const definePlugin = new webpack.DefinePlugin({
    'process.env': {
        NODE_ENV: JSON.stringify(env.NODE_ENV || 'development'),
        APP: JSON.stringify(env.APP || 'app'),
    },
});

let plugins,
    entry,
    loaders;

if (env.NODE_ENV === 'production') {
    plugins = [
        definePlugin,
        new webpack.optimize.OccurenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin({
            sourceMap: false,
            mangle: {
                except: [
                    'onSelect',
                    'initSelect',
                    'componentDidMount',
                    'componentDidUpdate',
                    'getSelect',
                    'renderOptions',
                ],
            },
            compressor: {
                warnings: false,
                drop_console: true,
                screw_ie8: true,
            },
        }),
    ];
    entry = ['./src/main/assets/js/index'];
    loaders = ['babel'];
} else {
    plugins = [
        new webpack.HotModuleReplacementPlugin(),
    ];
    entry = [
        'webpack-dev-server/client?http://localhost:3000',
        'webpack/hot/only-dev-server',
        './src/main/assets/js/index'];
    loaders = ['react-hot', 'babel'];
}

module.exports = {
    devtool: 'eval',
    entry,
    output: {
        path: path.join(__dirname, 'src/main/resources/'),
        filename: 'bundle.js',
        publicPath: '',
        library: 'Bundle',
        libraryTarget: 'umd',
    },
    plugins,
    module: {
        loaders: [{
            test: /\.js$/,
            loaders: loaders,
            include: path.join(__dirname, 'src'),
        }],
    },
};
