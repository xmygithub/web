package cn.kmbeast.service.impl;

import cn.kmbeast.mapper.NewsSaveMapper;
import cn.kmbeast.pojo.api.ApiResult;
import cn.kmbeast.pojo.api.PageResult;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NewsSaveQueryDto;
import cn.kmbeast.pojo.entity.NewsSave;
import cn.kmbeast.pojo.vo.NewsSaveVO;
import cn.kmbeast.service.NewsSaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 健康资讯收藏业务逻辑实现
 */
@Service
public class NewsSaveServiceImpl implements NewsSaveService {

    @Resource
    private NewsSaveMapper newsSaveMapper;

    /**
     * 健康资讯收藏新增
     *
     * @param newsSave 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(NewsSave newsSave) {
        newsSave.setCreateTime(LocalDateTime.now());
        newsSaveMapper.save(newsSave);
        return ApiResult.success();
    }

    /**
     * 健康资讯收藏删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Long> ids) {
        newsSaveMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 健康资讯收藏查询
     *
     * @param newsSaveQueryDto 查询参数
     * @return Result<List < NewsSaveVO>>
     */
    @Override
    public Result<List<NewsSaveVO>> query(NewsSaveQueryDto newsSaveQueryDto) {
        List<NewsSaveVO> tagsList = newsSaveMapper.query(newsSaveQueryDto);
        Integer totalCount = newsSaveMapper.queryCount(newsSaveQueryDto);
        return PageResult.success(tagsList, totalCount);
    }


}
