module.exports = {
  publicPath: '/student/',
  outputDir: 'dist',
  devServer: {
    port: 8081,
    proxy: {
      '/student': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
