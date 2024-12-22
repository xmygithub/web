<template>
    <div>
        <!-- 首页上端 左侧轮播图 右侧推荐咨询  中间标签分类  下方对应的标签-->
        <el-row>
            <!-- 轮播图 -->
            <el-col :span="7">
                <Banner :data="newsTopList" @on-click="onBannerClick" />
            </el-col>
            <!-- 推荐标签 -->
            <el-col :span="17">
                <el-col @click.native="newsItemClick(news)" :span="6" :key="index" v-for="(news, index) in newsTopList">
                    <div style="padding: 0 10px;">
                        <img :src="news.cover" :alt="news.name" style="width: 100%;height: 118px;border-radius: 5px;">
                        <h3 class="news-title">{{ news.name }}</h3>
                        <div style="font-size: 12px;">
                            <span class="news-tags">{{ news.tagName }}</span>
                            <span style="margin-left: 10px;">{{ parseTime(news.createTime) }}</span>
                        </div>
                    </div>
                </el-col>
            </el-col>
        </el-row>

        <!-- 标签分类 -->
        <el-row>
            <!-- 点击后调用 tagOnClick方法 -->
            <TagLine :dataList="tagsList" @on-click="tagOnClick" />
        </el-row>
        <el-row>
            <el-col @click.native="newsItemClick(news)" :span="4" :key="index" v-for="(news, index) in newsList">
                <div style="padding: 0 10px 10px 0;box-sizing: border-box;">
                    <img :src="news.cover" :alt="news.name" style="width: 100%;height: 118px;border-radius: 5px;">
                    <h3 class="news-title">{{ news.name }}</h3>
                    <div style="font-size: 12px;">
                        <!-- 对应标签名 -->
                        <span class="news-tags">{{ news.tagName }}</span>
                        <span style="margin-left: 10px;">{{ parseTime(news.createTime) }}</span>
                        <!-- 创建时间 -->
                    </div>
                </div>
            </el-col>
        </el-row>

        
    </div>
</template>
<script>
import TagLine from "@/components/TagLine"
import Banner from "@/components/Banner"
import { timeAgo } from "@/utils/data"
export default {
    components: { TagLine, Banner },
    data() {
        return {
            tagsList: [], // 标签列表
            newsList: [], // 健康资讯列表
            newsTopList: [], // 推荐的健康资讯数据列表
            newQueryDto: { tagId: null },
        }
    },
    created() {
        this.loadAllTags();
        this.loadAllNews();
        this.loadAllTopNews();
    },
    methods: {
        // 轮播图点击事件回传
        onBannerClick(banner) {
            sessionStorage.setItem('newsInfo', JSON.stringify(banner));
            this.$router.push('/news-detail');
        },
        // 健康资讯列表的项点击事件
        newsItemClick(news) {
            sessionStorage.setItem('newsInfo', JSON.stringify(news));
            this.$router.push('/news-detail');
        },
        // 转换时间  将时间装换成  几天前这些 如3天前
        parseTime(time) {
            return timeAgo(time);
        },
        tagOnClick(tags) {
            this.newQueryDto.tagId = tags.id;
            this.loadAllNews();
        },
        // 查询全部的标签记录
        loadAllTags() {
            this.$axios.post('/tags/query', {}).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.tagsList = data.data;
                }
            })
        },
        // 查标签下的资讯信息
        loadAllTopNews() {
            const newQueryDto = { isTop: true };
            this.$axios.post('/news/query', newQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.newsTopList = data.data;
                }
            })
        },
        // 查标签下的资讯信息
        loadAllNews() {
            this.$axios.post('/news/query', this.newQueryDto).then(response => {
                const { data } = response;
                if (data.code === 200) {
                    this.newsList = data.data;
                }
            })
        }
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

.news-title {
    overflow: hidden;
    /* 显示省略符号来代表被修剪的文本。 */
    text-overflow: ellipsis;
    /* 文本不换行 */
    white-space: nowrap;
}
</style>