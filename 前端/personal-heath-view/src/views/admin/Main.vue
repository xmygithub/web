<template>
    <div style="padding: 10px;">
        <el-row>
            <el-col :span="16">
                <div style="padding: 10px 20px;box-sizing: border-box;">
                    <LineChart height="370px" tag="用户存量数据" @on-selected="userDatesSelected" :values="userValues"
                         :date="userDates" />
                </div>
                <div style="padding: 10px 20px;box-sizing: border-box;">
                    <LineChart height="370px" tag="用户健康记录数据" @on-selected="modelDatesSelected" :values="modelValues"
                         :date="modelDates" />
                </div>
            </el-col>
            <el-col :span="8"> 
                <div style="padding: 10px 20px;box-sizing: border-box;">
                    <PieChart fontColor="#fff" bag="#2f90b9" tag="系统基础数据" :values="pieValues" :types="pieTypes" />
                </div>
                <div style="padding: 10px 20px;box-sizing: border-box;">
                    <h3>最新消息</h3>
                    <div style="margin-bottom: 10px;" v-for="(message, index) in messageList" :key="index">
                        <div>
                            <span class="status-success" v-if="message.messageType === 1">评论回复</span>
                            <span class="status-success" v-else-if="message.messageType === 2">评论点赞</span>
                            <span class="status-success" v-else-if="message.messageType === 3">指标提醒</span>
                            <span class="status-success" v-else>系统通知</span>
                        </div>
                        <!-- 内容 -->
                        <div style="margin: 5px 0;">
                            <span style="font-size: 14px;">{{ message.content }}</span>
                        </div>
                        <!-- 时间 -->
                        <div>
                            <span style="font-size: 14px;">{{ message.createTime }}</span>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>
<script>
import LineChart from "@/components/LineChart"
import PieChart from "@/components/PieChart"
export default {
    components: { LineChart, PieChart },
    data() {
        return {
            userValues: [],
            userDates: [],
            modelDates: [],
            modelValues: [],
            pieValues: [],
            pieTypes: [],
            messageList: []
        }
    },
    created() {
        // 默认查7天
        this.userDatesSelected(7);
        // 默认查7天
        this.modelDatesSelected(7);
        this.loadPieCharts();
        this.loadMessage();
    },
    methods: {
        // 加载消息数据
        loadMessage() {
            const messageQueryDto = {
                current: 1,
                size: 4
            }
            this.$axios.post(`/message/query`, messageQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.messageList = data.data;
                }
            })
        },
        loadPieCharts() {
            this.$axios.get(`/views/staticControls`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.pieValues = data.data.map(entity => entity.count);
                    this.pieTypes = data.data.map(entity => entity.name);
                }
            })
        },
        modelDatesSelected(time) {
            this.$axios.get(`/user-health/daysQuery/${time}`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.modelValues = data.data.map(entity => entity.count);
                    this.modelDates = data.data.map(entity => entity.name);
                }
            })
        },
        userDatesSelected(time) {
            this.$axios.get(`/user/daysQuery/${time}`).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.userValues = data.data.map(entity => entity.count);
                    this.userDates = data.data.map(entity => entity.name);
                }
            })
        },
    },
};
</script>
<style scoped lang="scss">
.status-success {
    display: inline-block;
    padding: 1px 5px;
    border-radius: 2px;
    background-color: rgb(201, 237, 249);
    color: rgb(111, 106, 196);
    font-size: 12px;
}

.status-error {
    display: inline-block;
    padding: 1px 5px;
    border-radius: 2px;
    background-color: rgb(233, 226, 134);
    color: rgb(131, 138, 142);
    color: rgb(111, 106, 196);
    font-size: 12px;
}
</style>