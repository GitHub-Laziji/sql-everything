<template>
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
</template>

<script>
export default {
    data() {
        return {
            queryForm: {
                form: {
                    sql: ""
                }
            }
        }
    },
    mounted() {

    },
    methods: {
        query() {
            this.$http.post("/api/query", { sql: this.queryForm.form.sql }).then(res => {
                console.log(res.data);
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