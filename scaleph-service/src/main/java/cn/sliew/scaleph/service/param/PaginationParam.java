package cn.sliew.scaleph.service.param;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gleiyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页参数")
public class PaginationParam implements Serializable {
    private static final long serialVersionUID = -860020632404225667L;

    @ApiModelProperty(value = "页码", example = "1")
    private Long current = 1L;

    @ApiModelProperty(value = "页面大小", example = "10")
    private Long pageSize = 10L;

    @ApiModelProperty(value = "排序", example = "[{direction: \"ASC\"" + "field: \"fieldName\"}]")
    private List<SortArg> sorter;

    public List<OrderItem> buildSortItems() {
        List<OrderItem> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sorter)) {
            for (SortArg arg : sorter) {
                if (StrUtil.isAllNotBlank(arg.getField())) {
                    if ("desc".equalsIgnoreCase(arg.getDirection())) {
                        list.add(OrderItem.desc(StrUtil.toUnderlineCase(arg.getField())));
                    } else if ("asc".equalsIgnoreCase(arg.getDirection())) {
                        list.add(OrderItem.asc(StrUtil.toUnderlineCase(arg.getField())));
                    } else {
                        list.add(OrderItem.asc(StrUtil.toUnderlineCase(arg.getField())));
                    }
                }
            }
        }
        return list;
    }
}

