const { defineConfig } = require('@vue/cli-service')
var path = require('path')
module.exports = defineConfig({
    transpileDependencies: true,
    configureWebpack: {
        resolve: {
            modules: [
                path.resolve("./src"),
                path.resolve("./node_modules")
            ]
        }
    }
})