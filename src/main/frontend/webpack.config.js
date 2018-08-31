import path from "path";
import webpack from "webpack";
import ExtractTextPlugin from "extract-text-webpack-plugin";

module.exports = {
    entry: [
      './src/main.js',
      './src/main.less'
    ],
    output: {
        path: path.join(__dirname, '/dist'),
        filename: 'main.js',
    },
    module: {
      rules: [
        // Loader for project js files
        {
            test: /\.js$/,
            exclude: /(node_modules|bower_components)/,
            loader: 'babel-loader',
        },
        {
          test: /\.less$/,
          use: ExtractTextPlugin.extract({
            use: [{
                loader: "css-loader"
            }, {
                loader: "less-loader"
            }],
            // use style-loader in development
            fallback: "style-loader"
          })
        },

          // Load images
        { test: /\.jpg/, use: "url-loader?limit=10000&mimetype=image/jpg" },
        { test: /\.gif/, use: "url-loader?limit=10000&mimetype=image/gif" },
        { test: /\.png/, use: "url-loader?limit=10000&mimetype=image/png" },
        { test: /\.svg/, use: "url-loader?limit=10000&mimetype=image/svg" },

        // Load fonts
        { test: /\.woff(\?v=[0-9]\.[0-9]\.[0-9])?$/, use: "url-loader?limit=10000&mimetype=application/font-woff" },
        { test: /\.woff[0-9]$/, use: "url-loader?limit=10000&mimetype=application/font-woff" },
        { test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, use: "file-loader" },
      ]
    },
    resolve: {
        modules: [
          "node_modules",
          path.resolve(__dirname, "src")
        ],
        extensions: [".js"]
    },
    plugins: [
      new ExtractTextPlugin("main.css"),
      new webpack.ProvidePlugin({
          jQuery: 'jquery',
          $: 'jquery',
          jquery: 'jquery'
      }),
    //   new webpack.optimize.UglifyJsPlugin({
    //     compress: {
    //         warnings: false,
    //         comparisons: false,  // don't optimize comparisons. It causes problems in mapboxgl
    //     },
    //   })
    ]
};
