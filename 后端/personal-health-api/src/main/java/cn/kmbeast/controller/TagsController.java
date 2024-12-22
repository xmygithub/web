package cn.kmbeast.controller;

import cn.kmbeast.aop.Pager;
import cn.kmbeast.pojo.api.Result;
import cn.kmbeast.pojo.dto.query.extend.TagsQueryDto;
import cn.kmbeast.pojo.entity.Tags;
import cn.kmbeast.service.TagsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签的 Controller
 */
@RestController //处理HTTP请求
@RequestMapping(value = "/tags")
public class TagsController {

    @Resource
    private TagsService tagsService;

    /**
     * 标签新增
     *
     * @param tags 新增数据
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody Tags tags) {
        return tagsService.save(tags);
    }
//    用于处理HTTP POST请求，并将请求体中的JSON数据保存到数据库中
//    @RequestBody将HTTP请求体中的数据绑定到Controller方法的参数上
//    @RequestBody注解告诉Spring MVC，这个方法的参数应该从HTTP请求体中获取，
//    并且将请求体中的JSON数据反序列化为Tags对象。Tags是与数据库表结构对应的Java实体类。
    /**
     * 这个方法提供了一个RESTful API端点/save，允许客户端通过HTTP POST请求发送JSON格式
     * 的标签数据到服务器。服务器接收到请求后，将请求体中的JSON数据映射到Tags对象，
     * 然后调用服务层的方法保存数据。最终，方法返回一个Result对象，表示操作的结果。*/

    /**
     * 标签删除
     * @param ids 要删除的标签ID列表
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        return tagsService.batchDelete(ids);
    }

    /**
     * 标签修改
     *
     * @param tags 参数
     * @return Result<Void> 响应
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody Tags tags) {
        return tagsService.update(tags);
    }

    /**
     * 标签查询
     *
     * @param tagsQueryDto 查询参数
     * @return Result<List < Tags>> 通用响应
     */
    @Pager//自动处理分页请求
    @PostMapping(value = "/query")
    public Result<List<Tags>> query(@RequestBody TagsQueryDto tagsQueryDto) {
        return tagsService.query(tagsQueryDto);
    }

}
