package cn.kmbeast.pojo.dto.query.extend;

import cn.kmbeast.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsQueryDto extends QueryDto {


    private String name;//咨询名
    private Integer tagId;//标签ID
    private Boolean isTop;//是否推荐

}
