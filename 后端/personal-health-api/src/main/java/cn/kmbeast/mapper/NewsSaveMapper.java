package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.NewsSaveQueryDto;
import cn.kmbeast.pojo.dto.query.extend.TagsQueryDto;
import cn.kmbeast.pojo.entity.NewsSave;
import cn.kmbeast.pojo.entity.Tags;
import cn.kmbeast.pojo.vo.NewsSaveVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 健康资讯收藏持久化接口
 */
@Mapper
public interface NewsSaveMapper {

    // 收藏跟取消收藏，在后台分别代表什么？
    void save(NewsSave newsSave);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<NewsSaveVO> query(NewsSaveQueryDto newsSaveQueryDto);

    Integer queryCount(NewsSaveQueryDto newsSaveQueryDto);

}
