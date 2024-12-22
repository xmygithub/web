<!-- 健康咨询 点击后显示-->

<template>
    <el-row>
        <!-- 未收藏 -->
        <span class="save-btn" @click="saveNews">
            <i class="el-icon-star-on" :style="{ color: !saveFlag ? '#333' : '#ee3f4d' }"></i>
            <span :style="{ color: !saveFlag ? '#333' : '#ee3f4d' }">{{ !saveFlag ? '立即收藏' : '取消收藏' }}</span>
        </span>
        <el-col :span="18">
            <div style="padding-right: 50px;box-sizing: border-box;">
                <div>
                    <!-- 标题名 -->
                    <h1>{{ newsInfo.name }}</h1>
                    <div style="font-size: 12px;">
                        <!-- 标签名 -->
                        <span class="news-tags">{{ newsInfo.tagName }}</span>
                        <!-- 什么时候发布 -->
                        <span style="margin-left: 10px;">{{ parseTime(newsInfo.createTime) }}</span>
                    </div>
                    <!-- 文本内容 -->
                    <div v-html="newsInfo.content"></div>
                </div>

                <div>
                    <!-- 评论 -->
                    <Evaluations :contentId="newsInfo.id" contentType="NEWS" />
                </div>
            </div>
        </el-col>
        <!-- 右侧咨询推荐 -->
        <el-col :span="6">
            <h3 style="padding: 10px 30px;">资讯推荐</h3>
            <el-col @click.native="newsItemClick(news)" :span="24" :key="index" v-for="(news, index) in newsTopList">
                <div style="padding: 25px 30px;box-sizing: border-box;">
                    <img :src="news.cover" :alt="news.name" style="width: 100%;height: 118px;border-radius: 5px;">
                    <!-- 标题名 -->
                    <h3 class="news-title">{{ news.name }}</h3>
                    <div style="font-size: 12px;">
                        <!-- 标签名 -->
                        <span class="news-tags">{{ news.tagName }}</span>
                        <!--什么时间发布 -->
                        <span style="margin-left: 10px;">{{ parseTime(news.createTime) }}</span>
                    </div>
                </div>
            </el-col>
        </el-col>
    </el-row>
</template>
<script>
import { timeAgo } from "@/utils/data"
import Evaluations from "@/components/Evaluations.vue";
export default {
    components: { Evaluations },
    name: "NewsDetail",
    data() {
        return {
            newsInfo: {},
            newsTopList: [],
            saveFlag: false,
            newsSaveList: []
        }
    },
    created() {
        // 第一件事：拿到存起来的资讯数据
        this.getStorageInfo();
        this.loadAllTopNews();
    },
    watch: {
        newsInfo(v1, v2) {
            this.loadNewsSaveStatus();
        },
    },
    methods: {
        // 加载内容的时候，是不是要监听判断：用户是不是收藏了这篇文章？
        // 这就是判断用户跟这篇文章的收藏关系
        loadNewsSaveStatus() {
            // 传用户ID以及当前文章ID
            const userInfo = sessionStorage.getItem('userInfo');//传过来的是JSON字符串
            const userInfoEntity = JSON.parse(userInfo);
            const newsSaveQueryDto = {
                userId: userInfoEntity.id,
                newsId: this.newsInfo.id
            }
            this.$axios.post('/news-save/query', newsSaveQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    const responseData = data.data;
                    // 请求成功
                    // 1. 置状态
                    this.saveFlag = responseData.length !== 0;
                    // 2. 把值拿出来
                    this.newsSaveList = responseData;
                }
            });
        },
        // 用户要收藏这一篇文章
        saveNews() {
            // 收藏---> 对应新增
            if (!this.saveFlag) {
                const userInfo = sessionStorage.getItem('userInfo');
                const userInfoEntity = JSON.parse(userInfo);
                const newsSave = {
                    userId: userInfoEntity.id,
                    newsId: this.newsInfo.id
                }
                this.$axios.post('/news-save/save', newsSave).then(response => {
                    const { data } = response;
                    if (data.code === 200) {
                        // 请求成功
                        this.saveFlag = !this.saveFlag;
                        this.$swal.fire({
                            title: '资讯收藏',
                            text: '资讯收藏成功',
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 1000,
                        });
                    }
                });
            } else {
                const saveEntity = this.newsSaveList[0];
                const ids = [];
                ids.push(saveEntity.id);
                this.$axios.post('/news-save/batchDelete', ids).then(response => {
                    const { data } = response;
                    if (data.code === 200) {
                        // 请求成功
                        this.saveFlag = !this.saveFlag;
                        this.$swal.fire({
                            title: '资讯取消收藏',
                            text: '资讯取消收藏成功',
                            icon: 'success',
                            showConfirmButton: false,
                            timer: 1000,
                        });
                    }
                });
            }

        },
        newsItemClick(news) {
            this.newsInfo = news;
        },
        // 转换时间
        parseTime(time) {
            return timeAgo(time);
        },
        getStorageInfo() {
            const newInfo = sessionStorage.getItem('newsInfo');
            this.newsInfo = JSON.parse(newInfo);
        },
        // 查询推荐资讯
        loadAllTopNews() {
            const newQueryDto = { isTop: true };
            this.$axios.post('/news/query', newQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.newsTopList = data.data;
                }
            })
        },
    }
};
</script>

<style scoped lang="scss">
.news-tags {
    display: inline-block;
    padding: 2px 5px;
    background-color: rgb(222, 243, 251);
    color: #1d3cc4;
    border-radius: 3px;
}

.save-btn {
    position: fixed;
    top: 150px;
    left: 100px;

    i {
        display: block;
        text-align: center;
        font-size: 30px;
    }

    span {
        margin: 8px 0;
        display: inline-block;
        font-size: 12px;
    }
}

.save-btn:hover {
    color: #4f4f50;
}
</style>
