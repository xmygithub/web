<template>
    <el-row style="background-color: #FFFFFF;padding: 10px 0;border-radius: 5px;">
        <el-row style="padding: 10px 0 20px;border-bottom: 1px solid #f1f1f1;margin: 0 10px;">
            <el-row>
                <span class="top-bar">消息类型</span>
                <el-select size="small" v-model="messageQueryDto.messageType" placeholder="请选择">
                    <el-option v-for="messageType in messageTypeList" :key="messageType.type"
                        :label="messageType.detail" :value="messageType.type">
                    </el-option>
                </el-select>
                <span class="top-bar">消息内容</span>
                <el-input size="small" style="width: 188px;" v-model="messageQueryDto.content" placeholder="输入处"
                    clearable @clear="handleFilterClear">
                </el-input>
                <span class="top-bar">读取状态</span>
                <el-switch style="user-select: none;margin-right: 20px;" v-model="messageQueryDto.isRead"
                    active-color="#13ce66" inactive-color="rgb(226, 226, 226)">
                </el-switch>
                <span class="top-bar">记录时间</span>
                <el-date-picker size="small" style="margin-left: 10px;width: 220px;" v-model="searchTime"
                    type="daterange" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间">
                </el-date-picker>
            </el-row>
            <el-row style="margin-top: 20px;">
                <el-button size="small" class="customer" style="background-color: rgb(43, 121, 203);border: none;"
                    type="primary" @click="handleFilter">立即查询</el-button>
                <el-button size="small" class="customer reset"
                    style="background-color: #f1f1f1;border: none;color: #909399;border: 1px solid #f1f1f1;" type="info"
                    @click="resetQueryCondition">条件重置</el-button>
                <el-button size="small" class="customer" style="background-color: rgb(43, 121, 203);border: none;"
                    type="primary" @click="allMessagePush">全站推送</el-button>
                <el-button size="small" class="customer"
                    :style="{ marginLeft: '10px', backgroundColor: selectedRows.length ? '#a7535a' : '#F1F1F1', border: 'none', color: selectedRows.length ? '#FFFFFF' : '#909399' }"
                    type="danger" @click="batchDelete()">批量删除</el-button>
            </el-row>
        </el-row>
        <el-row style="margin: 10px;">
            <el-table row-key="id" @selection-change="handleSelectionChange" :data="tableData" style="width: 100%">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column prop="name" width="98" label="读取状态">
                    <template slot-scope="scope">
                        <span>{{ scope.row.isRead ? '已读' : '未读' }}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="messageType" width="148" label="消息类型">
                    <template slot-scope="scope">
                        <span v-if="scope.row.messageType === 1">评论回复</span>
                        <span v-else-if="scope.row.messageType === 2">评论点赞</span>
                        <span v-else-if="scope.row.messageType === 3">指标提醒</span>
                        <span v-else>系统通知</span>
                    </template>
                </el-table-column>
                <el-table-column prop="senderName" width="108" label="发送者"></el-table-column>
                <el-table-column prop="receiverName" width="108" label="接收者"></el-table-column>
                <el-table-column prop="evaluationsContent" width="188" label="关联内容"></el-table-column>
                <el-table-column prop="content" width="258" label="消息体"></el-table-column>
                <el-table-column prop="createTime" width="168" label="发送时间"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <span class="text-button" @click="handleDelete(scope.row)">删除</span>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination style="margin: 20px 0;" @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="currentPage" :page-sizes="[5, 7]" :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="totalItems"></el-pagination>
        </el-row>
        <el-dialog :show-close="false" :visible.sync="dialogUserOperaion" width="26%">
            <div slot="title">
                <p class="dialog-title">{{ !isOperation ? '新增消息' : '编辑消息信息' }}</p>
            </div>
            <div style="padding:0 20px;">
                <!-- 图标 -->
                <el-row style="margin-top: 20px;">
                    <el-upload class="avatar-uploader" action="/api/personal-heath/v1.0/file/upload"
                        :show-file-list="false" :on-success="handleAvatarSuccess">
                        <img v-if="data.cover" :src="data.cover" style="height: 44px;width: 44px;">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-row>
                <!-- 配置名 -->
                <el-row style="padding: 0 20px 0 0;">
                    <p>
                        <span class="modelName">*配置名</span>
                    </p>
                    <input class="input-title" v-model="data.name"
                        style="border-radius: 5px;background-color: #f1f1f1;">
                </el-row>
                <!-- 单位 -->
                <el-row style="padding: 0 20px 0 0;">
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*单位</span>
                    </p>
                    <input class="input-title" v-model="data.unit"
                        style="border-radius: 5px;background-color: #f1f1f1;">
                </el-row>
                <!-- 符号 -->
                <el-row style="padding: 0 20px 0 0;">
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*符号</span>
                    </p>
                    <input class="input-title" v-model="data.symbol"
                        style="border-radius: 5px;background-color: #f1f1f1;">
                </el-row>
                <!-- 简介 -->
                <el-row style="padding: 0 20px 0 0;">
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*简介</span>
                    </p>
                    <el-input style="border-radius: 5px;background-color: #f1f1f1;" type="textarea"
                        :autosize="{ minRows: 2, maxRows: 3 }" placeholder="简介" v-model="data.detail">
                    </el-input>
                </el-row>
                <!-- 简介 -->
                <el-row style="padding: 0 20px 0 0;">
                    <p style="font-size: 12px;padding: 3px 0;">
                        <span class="modelName">*正常值范围</span>
                    </p>
                    <el-slider v-model="valuesRange" range show-stops :max="1000">
                    </el-slider>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" v-if="!isOperation" style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="addOperation">新增</el-button>
                <el-button size="small" v-else style="background-color: rgb(43, 121, 203);border: none;"
                    class="customer" type="info" @click="updateOperation">修改</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogUserOperaion = false">取消</el-button>
            </span>
        </el-dialog>
        <!-- 全站消息推送 -->
        <el-dialog :show-title="false" :show-close="false" :visible.sync="dialogMessageOperation" width="24%">
            <p style="padding: 20px 0  0 20px;">消息推送</p>
            <div style="padding:0 20px;">
                <el-row>
                    <span class="dialog-hover">消息内容</span>
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4 }" placeholder="消息内容"
                        v-model="messageContent">
                    </el-input>
                </el-row>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" style="background-color: rgb(43, 121, 203);border: none;" class="customer"
                    type="info" @click="messagePushOperation">确定推送</el-button>
                <el-button class="customer" size="small" style="background-color: rgb(241, 241, 241);border: none;"
                    @click="dialogMessageOperation = false">取消</el-button>
            </span>
        </el-dialog>
    </el-row>
</template>

<script>
export default {
    data() {
        return {
            data: {},
            filterText: '',
            currentPage: 1,
            pageSize: 7,
            totalItems: 0,
            dialogUserOperaion: false, // 开关
            isOperation: false, // 开关-标识新增或修改
            tableData: [],
            searchTime: [],
            selectedRows: [],
            status: null,
            messageQueryDto: {}, // 搜索条件
            messsageContent: '',
            tagsList: [],
            valuesRange: [10, 50],
            messageTypeList: [],
            messageQueryDto: {},
            dialogMessageOperation: false, // 开关变量，控制消息的对话框显示与否
            messageContent: ''
        };
    },
    watch: {
        dialogUserOperaion(v1, v2) {
            if (!v1) {
                this.isOperation = !this.isOperation;
            }
            if (!v1 && v2) {
                this.data = {};
            }
        },
    },
    created() {
        this.fetchFreshData();
        this.loadAllMessageType();
    },
    methods: {
        messagePushOperation() {
            const message = {
                content: this.messageContent
            }
            this.$axios.post('/message/systemInfoUsersSave', message).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.$swal.fire({
                        title: '消息推送',
                        text: '推送成功',
                        icon: 'success',
                        showConfirmButton: false,
                        timer: 1000,
                    });
                    this.dialogMessageOperation = false;
                    this.messageContent = '';
                }
            })
        },
        allMessagePush() {
            this.dialogMessageOperation = true;
        },
        loadAllMessageType() {
            this.$axios.get('/message/types').then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.messageTypeList = data.data;
                }
            })
        },
        handleAvatarSuccess(res, file) {
            if (res.code !== 200) {
                this.$message.error(`消息封面上传异常`);
                return;
            }
            this.$message.success(`消息封面上传成功`);
            this.data.cover = res.data;
        },
        // 多选框选中
        handleSelectionChange(selection) {
            this.selectedRows = selection;
        },
        // 批量删除数据
        async batchDelete() {
            if (!this.selectedRows.length) {
                this.$message(`未选中任何数据`);
                return;
            }
            const confirmed = await this.$swalConfirm({
                title: '删除消息数据',
                text: `删除后不可恢复，是否继续？`,
                icon: 'warning',
            });
            if (confirmed) {
                try {
                    let ids = this.selectedRows.map(entity => entity.id);
                    const response = await this.$axios.post(`/message/batchDelete`, ids);
                    if (response.data.code === 200) {
                        this.$swal.fire({
                            title: '删除提示',
                            text: response.data.msg,
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 2000,
                        });
                        this.fetchFreshData();
                        return;
                    }
                } catch (e) {
                    this.$swal.fire({
                        title: '错误提示',
                        text: e,
                        icon: 'error',
                        showConfirmButton: false,
                        timer: 2000,
                    });
                    console.error(`消息信息删除异常：`, e);
                }
            }
        },
        resetQueryCondition() {
            this.messageQueryDto = {};
            this.searchTime = [];
            this.fetchFreshData();
        },
        // 修改信息
        async updateOperation() {
            try {
                this.data.valueRange = this.valuesRange.join(',');
                const response = await this.$axios.put('/message/update', this.data);
                this.$swal.fire({
                    title: '消息信息修改',
                    text: response.data.msg,
                    icon: response.data.code === 200 ? 'success' : 'error',
                    showConfirmButton: false,
                    timer: 1000,
                });
                if (response.data.code === 200) {
                    this.closeDialog();
                    this.fetchFreshData();
                    this.clearFormData();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('提交失败，请稍后再试！');
            }
        },
        // 信息新增
        async addOperation() {
            try {
                // [20,252] ---> 20,252
                this.data.valueRange = this.valuesRange.join(',');
                const response = await this.$axios.post('/message/save', this.data);
                this.$message[response.data.code === 200 ? 'success' : 'error'](response.data.msg);
                if (response.data.code === 200) {
                    this.closeDialog();
                    this.fetchFreshData();
                    this.clearFormData();
                }
            } catch (error) {
                console.error('提交表单时出错:', error);
                this.$message.error('提交失败，请稍后再试！');
            }
        },
        closeDialog() {
            this.dialogUserOperaion = false;
        },
        clearFormData() {
            this.data = {};
        },
        async fetchFreshData() {
            try {
                this.tableData = [];
                let startTime = null;
                let endTime = null;
                if (this.searchTime != null && this.searchTime.length === 2) {
                    const [startDate, endDate] = await Promise.all(this.searchTime.map(date => date.toISOString()));
                    startTime = `${startDate.split('T')[0]}T00:00:00`;
                    endTime = `${endDate.split('T')[0]}T23:59:59`;
                }
                // 请求参数
                const params = {
                    current: this.currentPage,
                    size: this.pageSize,
                    startTime: startTime,
                    endTime: endTime,
                    ...this.messageQueryDto
                };
                const response = await this.$axios.post('/message/query', params);
                const { data } = response;
                this.tableData = data.data;
                this.totalItems = data.total;
            } catch (error) {
                console.error('查询消息信息异常:', error);
            }
        },
        add() {
            this.dialogUserOperaion = true;
        },
        handleFilter() {
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleFilterClear() {
            this.filterText = '';
            this.handleFilter();
        },
        handleSizeChange(val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchFreshData();
        },
        handleCurrentChange(val) {
            this.currentPage = val;
            this.fetchFreshData();
        },
        handleEdit(row) {
            this.dialogUserOperaion = true;
            this.isOperation = true;
            if (row.valueRange !== null) {
                this.valuesRange = row.valueRange.split(',');
            }
            this.data = { ...row }
        },
        handleDelete(row) {
            this.selectedRows.push(row);
            this.batchDelete();
        }
    },
};
</script>
<style scoped lang="scss">
.status-success,
.status-error {
    display: inline-block;
    padding: 1px 5px;
    border-radius: 2px;
}

.status-success {
    background-color: rgb(232, 250, 158);
    color: #6d0ea1;
}

.status-error {
    background-color: rgb(227, 171, 147);
    color: #6d0ea1;
}

.tag-tip {
    display: inline-block;
    padding: 5px 10px;
    border-radius: 5px;
    background-color: rgb(245, 245, 245);
    color: rgb(104, 118, 130);
}

.input-def {
    height: 40px;
    line-height: 40px;
    outline: none;
    border: none;
    font-size: 20px;
    color: rgb(102, 102, 102);
    font-weight: 900;
    width: 100%;
}

.dialog-footer {
    /* 使按钮水平居中 */
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 如果需要调整按钮之间的间距 */
.customer {
    margin: 0 8px;
    /* 根据需要调整间距 */
}
</style>