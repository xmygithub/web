module.exports = {
    lintOnSave: false,
    devServer: {
        host: "localhost",
        // host: "0.0.0.0",
        port: 21091,
        https: false,
        proxy: "http://localhost:21090",
        overlay: {
            warning: false,
            errors: false
        },
    }
}