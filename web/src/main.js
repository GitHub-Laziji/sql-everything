import './assets/base.css'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { Http } from "./utils/http.js"

const app = createApp(App);


app.use(ElementPlus);
app.config.globalProperties.$http = Http;
app.mount('#app')
