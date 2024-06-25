const { defineConfig } = require('@vue/cli-service')
require('dotenv').config()

/** API 호스트 */
const apiHost = process.env.API_HOST

/** 프록시 설정 경로 */
const proxyConfig = [
  '/boards/free',
  '/categories',
  '/comments',
  '/files'
];

/** 프록시 설정 */
const proxy = proxyConfig.reduce((proxyObject, path) => {
  proxyObject[path] = {
    target: apiHost,  // 현재 경로(path)로 들어온 요청을 API 호스트 주소로 전송
    changeOrigin: true  // cross origin 허용
  };

  return proxyObject;
}, {});

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../backend/src/main/resources/static",  // 빌드 타켓 디렉토리
  devServer: {
    proxy
  }
})
