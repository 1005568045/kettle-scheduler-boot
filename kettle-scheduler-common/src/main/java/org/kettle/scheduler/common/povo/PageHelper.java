package org.kettle.scheduler.common.povo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 分页助手
 *
 * @author lyf
 */
@Data
public class PageHelper {
    /**
     * 第几页
     */
    private int number = 0;

    /**
     * 每页多少条数据
     */
    private int size = 10;

    /**
     * 排序字段和方向(ASC, DESC)
     */
    private SortedMap<String, Sort.Direction> sort = new TreeMap<>();

    /**
     * 自定义set方法, 方便前端传入的页码和数据库页码对应
     */
    public void setNumber(int number) {
        this.number = number >= 1 ? number - 1 : 0;
    }

    /**
     * 获取排序字段集, 给jpa的pageable使用
     */
    @JsonIgnore
    public Sort getSorts() {
        return Sort.by(sort.entrySet().stream().map(e -> new Sort.Order(e.getValue(), e.getKey())).collect(Collectors.toList()));
    }

    /**
     * 返回分页参数, 设置一个默认排序，如果没有
     */
    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(number, size, getSorts());
    }
}
