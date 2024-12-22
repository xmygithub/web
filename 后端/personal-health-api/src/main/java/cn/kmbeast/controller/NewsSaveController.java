package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.NewsSaveQueryDto;
import cn.kmbeast.pojo.entity.NewsSave;
import cn.kmbeast.pojo.vo.NewsSaveVO;
import cn.kmbeast.service.NewsSaveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资讯收藏的 Controller
 */
@RestController
@RequestMapping(value = "/news-save")
public class NewsSaveController {

    @Resource
    private NewsSaveService newsSaveService;

    /**
     * 资讯收藏新增
     *
     * @param newsSave 新增数据
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody NewsSave newsSave) {
        return newsSaveService.save(newsSave);
    }

    /**
     * 资讯收藏删除
     *
     * @param ids 要删除的资讯收藏ID列表
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        return newsSaveService.batchDelete(ids);
    }

    /**
     * 资讯收藏查询
     *
     * @param newsSaveQueryDto 查询参数
     * @return Result<List < NewsSaveVO>> 通用响应
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<NewsSaveVO>> query(@RequestBody NewsSaveQueryDto newsSaveQueryDto) {
        return newsSaveService.query(newsSaveQueryDto);
    }

}
