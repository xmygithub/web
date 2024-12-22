package cn.kmbeast.mapper;

import cn.kmbeast.pojo.dto.query.extend.MessageQueryDto;
import cn.kmbeast.pojo.entity.Message;
import cn.kmbeast.pojo.entity.Tags;
import cn.kmbeast.pojo.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息持久化接口
 */
@Mapper
public interface MessageMapper {

    /**
     * 批量新增  用户可能多个  用集合接受*/
    void batchSave(List<Message> messages);

    void update(@Param(value = "userId") Integer userId,
                @Param(value = "isRead") Boolean isRead);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<MessageVO> query(MessageQueryDto messageQueryDto);

    Integer queryCount(MessageQueryDto messageQueryDto);

}
