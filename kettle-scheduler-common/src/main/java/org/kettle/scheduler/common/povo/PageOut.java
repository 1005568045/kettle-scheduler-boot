package org.kettle.scheduler.common.povo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 分页助手
 *
 * @author lyf
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageOut<T> extends PageHelper {
    /**
     * 总的数据量
     */
    private long totalElements;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 查询的当前页内容
     */
    private List<T> content;

    public PageOut() {
    }

    public PageOut(List<T> content) {
        this.content = content;
    }

    public PageOut(List<T> content, int number, int size, long totalElements, int totalPages) {
        this.content = content;
        this.setNumber(number);
        this.setSize(size);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    /**
     * 重写页码的set方法, 使得返回给页面的页码符合正常认知
     */
    @Override
    public void setNumber(int number) {
        super.setNumber(number + 2);
    }
}
