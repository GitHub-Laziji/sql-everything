<template>
    <el-tabs type="border-card" style="width: 100%;height: 100%;" v-model="tabIndex">
        <el-tab-pane label="SQL" name="SQL">
            <div style="padding: 10px;">
                <el-input v-model="queryForm.form.sql" style="width: 100%;" type="textarea" class="sql-input"
                    placeholder="SQL" />
            </div>
            <div style="display: flex;border-top: 1px solid #CDD0D6; width: 100%;padding: 10px;">
                <el-button @click="query">查询</el-button>
            </div>
        </el-tab-pane>
        <el-tab-pane label="结果" name="RESULT">
            <div style="padding: 10px;">
                <el-table :data="data" height="calc(100vh - 61px)" style="width: 100%" border>
                    <el-table-column v-for="col in columns" :key="col" :prop="col" :label="col" min-width="180" />
                </el-table>
            </div>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
export default {
    data() {
        return {
            queryForm: {
                form: {
                    sql: ""
                }
            },
            columns: [],
            data: [],
            tabIndex: "SQL"
        }
    },
    mounted() {

    },
    methods: {
        query() {
            this.$http.post("/api/query", { sql: this.queryForm.form.sql }).then(res => {
                this.data = res.data;
                this.columns = [];
                for (let k in this.data[0] || {}) {
                    this.columns.push(k);
                }
                this.tabIndex = "RESULT";
            });
        }
    }
}
</script>

<style scoped lang="less">
.sql-input {
    height: calc(100vh - 114px) !important;

    /deep/ textarea {
        height: calc(100vh - 113px) !important;
    }
}
</style>