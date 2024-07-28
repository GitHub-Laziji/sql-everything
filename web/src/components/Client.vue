<template>
    <div style="width: 100vw;height: 100vh;display: flex;flex-direction: column;">
        <div style="width: 100%;flex: 1;display: flex;">
            <div
                style="width: 300px;height: 100%;border-top: 1px solid #CDD0D6;padding: 10px;display: flex;flex-direction: column;">
                <el-select v-model="activeDb" placeholder="请选择数据库" style="width: 100%" @change="v => selectDb(v)"
                    ref="selectDbEl">
                    <el-option v-for="item in dbs" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                    <template #footer>
                        <div v-if="newDbForm.visible" style="display: flex;width: 100%;">
                            <el-input v-model="newDbForm.form.name" style="flex:1" size="small"></el-input>
                            <el-button size="small" style="margin-left: 8px;" icon="Check"
                                @click="newDb(newDbForm.form.name)"></el-button>
                            <el-button @click="newDbForm.visible = false" size="small" style="margin-left: 8px;"
                                icon="Close"></el-button>
                        </div>
                        <el-button v-else style="width: 100%;" icon="Plus" @click="newDbForm.visible = true" size="small">
                            新建数据库
                        </el-button>
                    </template>
                </el-select>
                <div v-if="!activeDb" style="display: flex;flex:1;width: 100%;justify-content: center;align-items: center;">
                    请选择数据库
                </div>
                <div v-else-if="!files.length"
                    style="display: flex;flex:1;width: 100%;justify-content: center;align-items: center;">
                    <el-button type="text" icon="Plus" @click="openAddFile">添加文件</el-button>
                </div>
                <div v-else style="display: flex;flex:1;width: 100%;margin-top: 10px;display: flex;flex-direction: column;">
                    <div style="display: flex;">
                        <el-button icon="Plus" @click="openAddFile" size="small"></el-button>
                    </div>
                    <el-collapse accordion style="width: 100%;margin-top: 10px;">
                        <el-collapse-item v-for="item in files" :key="item.id" :name="id">
                            <template #title>
                                <Collection style="width: 1em; height: 1em; margin-right: 8px"></Collection>
                                {{ item.fileName }}<el-tag type="primary" size="small" style="margin-left: 8px">{{
                                    item.fileType }}</el-tag>
                            </template>
                            <div
                                style="display: flex;flex-direction: column;border: 1px solid #EBEEF5;border-radius: 4px;padding:10px">
                                <div v-for="tb in item.tables" :key="tb">
                                    <Memo style="width: 1em; height: 1em; margin-right: 8px"></Memo>{{ tb }}
                                </div>
                            </div>
                        </el-collapse-item>
                    </el-collapse>
                </div>
            </div>
            <div style="height: 100%;flex: 1;">

                <el-tabs type="border-card" style="width: 100%;height: 100%;">
                    <el-tab-pane label="SQL">
                        <div style="padding: 10px;">
                            <el-input v-model="queryForm.form.sql" style="width: 100%;" type="textarea" class="sql-input"
                                placeholder="SQL" />
                        </div>
                        <div style="display: flex;border-top: 1px solid #CDD0D6; width: 100%;padding: 10px;">
                            <el-button @click="query">查询</el-button>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="结果"></el-tab-pane>
                </el-tabs>

            </div>
        </div>

        <el-dialog v-model="addFileForm.visible" title="添加文件" width="500">
            <el-form :model="addFileForm.form" label-width="auto" style="max-width: 600px">
                <el-form-item label="文件">
                    <div style="display: flex;">
                        <el-input v-model="addFileForm.form.fileName" :disabled="true"></el-input>
                        <el-upload ref="uploadRef" :auto-upload="false" :show-file-list="false" :on-change="selectFile">
                            <el-button type="primary" style="margin-left: 10px;">点击上传</el-button>
                        </el-upload>
                    </div>
                </el-form-item>
                <el-form-item label="文件类型">
                    <el-radio-group v-model="addFileForm.form.fileType">
                        <el-radio value="JSON">JSON</el-radio>
                        <el-radio label="CSV">CSV</el-radio>
                        <el-radio label="XLSX">Excel2007</el-radio>
                        <el-radio label="XLS">Excel2003</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="别名">
                    <el-input v-model="addFileForm.form.config.alias"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="addFileForm.visible = false">取消</el-button>
                    <el-button type="primary" @click="addFile">
                        添加
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            newDbForm: {
                visible: false,
                form: {
                    name: ""
                }
            },
            addFileForm: {
                visible: false,
                form: {
                    fileBase64: "",
                    fileName: "",
                    fileType: "",
                    config: {
                        alias: ""
                    }
                }
            },
            queryForm: {
                form: {
                    sql: ""
                }
            },
            dbs: [],
            activeDb: "",
            files: [],
        }
    },
    mounted() {
        this.listDbs();
    },
    methods: {
        listDbs() {
            this.$http.post("/api/listDbs").then(res => {
                this.dbs = res.data;
            });
        },
        selectDb(id) {
            this.$http.post("/api/selectOrNewDb", { id }).then(res => {
                this.listFiles();
            });
        },
        newDb(name) {
            this.$http.post("/api/selectOrNewDb", { name }).then(res => {
                this.activeDb = res.data;
                this.dbs.push({ id: res.data, name });
                this.newDbForm.form.name = "";
                this.newDbForm.visible = false;
                this.$message({
                    message: '添加成功',
                    type: 'success',
                })
                this.$refs.selectDbEl.blur();
                this.listFiles();
            });
        },
        listFiles() {
            this.$http.post("/api/listFiles").then(res => {
                this.files = res.data;
            });
        },
        openAddFile() {
            this.addFileForm.visible = true;
        },
        selectFile(file) {
            this.addFileForm.form.fileName = file.name;
            if (/\.json$/i.test(file.name)) {
                this.addFileForm.form.fileType = "JSON";
            } else if (/\.csv$/i.test(file.name)) {
                this.addFileForm.form.fileType = "CSV";
            } else if (/\.xlsx$/i.test(file.name)) {
                this.addFileForm.form.fileType = "XLSX";
            } else if (/\.xls$/i.test(file.name)) {
                this.addFileForm.form.fileType = "XLS";
            }
            let reader = new FileReader();
            reader.onload = (e) => {
                console.log(e.target.result)
                this.addFileForm.form.fileBase64 = e.target.result.substring(e.target.result.indexOf(",") + 1);
            }
            reader.readAsDataURL(file.raw);
        },
        addFile() {
            this.$http.post("/api/addFile", this.addFileForm.form).then(res => {
                this.addFileForm.visible = false;
                this.$message.success('添加成功');
                this.listFiles();
            });
        },
        query() {
            this.$http.post("/api/query", { sql: this.queryForm.form.sql }).then(res => {
                console.log(res.data);
            });
        }
    }
}
</script>

<style>
.sql-input {
    height: calc(100vh - 114px) !important;
}

.sql-input textarea {
    height: calc(100vh - 113px) !important;
}
</style>