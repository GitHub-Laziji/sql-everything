<template>
    <div style="width: 100vw;height: 100vh;display: flex;flex-direction: column;">
        <div style="width: 100%;flex: 1;display: flex;">
            <div style="width: 300px;height: 100%;border-top: 1px solid #CDD0D6;">
                <ProjectMenu @view-table="viewTable"></ProjectMenu>
            </div>
            <div style="height: 100%;flex: 1;">
                <SqlBoard ref="sqlBoard"></SqlBoard>
            </div>
        </div>
    </div>
</template>

<script>
import ProjectMenu from "./layout/ProjectMenu.vue";
import SqlBoard from "./layout/SqlBoard.vue";
export default {
    components: {
        ProjectMenu,
        SqlBoard
    },
    data() {
        return {

        }
    },
    mounted() {

    },
    methods: {
        viewTable(table) {
            this.$refs.sqlBoard.query(`select * from ${table}`);
        }
    }
}
</script>
