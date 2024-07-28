import './assets/base.css'

import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import { Http } from "./utils/http.js"
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);


app.use(ElementPlus);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.config.globalProperties.$http = Http;
app.mount('#app')
