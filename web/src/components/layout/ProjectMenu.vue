<template>
    <div style="width: 100%;height: 100%;padding: 10px;display: flex;flex-direction: column;">
        <el-select v-model="activeDb" placeholder="请选择数据库" style="width: 100%" @change="v => selectDb(v)" ref="selectDbEl">
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
        <div v-else-if="!fileTree.length"
            style="display: flex;flex:1;width: 100%;justify-content: center;align-items: center;">
            <el-button type="text" icon="Plus" @click="openAddFile">添加文件</el-button>
        </div>
        <div v-else style="display: flex;flex:1;width: 100%;margin-top: 10px;display: flex;flex-direction: column;">
            <div style="display: flex;">
                <el-button icon="Plus" @click="openAddFile" size="small"></el-button>
            </div>
            <el-tree :data="fileTree" :props="{ children: 'children', label: 'label' }"
                style="width: 100%;margin-top: 10px;" default-expand-all :expand-on-click-node="false">
                <template #default="{ data }">
                    <div style="display: flex;justify-content: space-between;width: 100%;align-items: center;">
                        <template v-if="data.type == 'FILE'">
                            <div>
                                <el-tag effect="plain" round size="small">
                                    {{ data.origin.fileType }}
                                </el-tag>
                                {{ data.label }}
                            </div>
                            <div>
                                <el-button type="text" size="mini">
                                    替换
                                </el-button>
                                <el-button type="text" size="mini">
                                    删除
                                </el-button>
                            </div>
                        </template>
                        <template v-else>
                            <div>
                                {{ data.label }}
                            </div>
                            <div>
                                <el-button type="text" size="mini">
                                    查看
                                </el-button>
                            </div>
                        </template>
                    </div>
                </template>
            </el-tree>
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
            dbs: [],
            activeDb: "",
            fileTree: [],
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
                let fileTree = [];
                for (let f of res.data) {
                    let file = { label: f.fileName, children: [], type: "FILE", origin: f };
                    for (let t of f.tables) {
                        file.children.push({ label: t, children: [], type: "TABLE" });
                    }
                    fileTree.push(file);
                }
                this.fileTree = fileTree;
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
        }
    }
}
</script>
