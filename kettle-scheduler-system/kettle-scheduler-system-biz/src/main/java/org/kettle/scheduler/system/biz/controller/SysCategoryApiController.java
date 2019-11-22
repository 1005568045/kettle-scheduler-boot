package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.api.SysCategoryApi;
import org.kettle.scheduler.system.api.request.CategoryReq;
import org.kettle.scheduler.system.api.response.CategoryRes;
import org.kettle.scheduler.system.biz.service.SysCategoryService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类管理API
 *
 * @author lyf
 */
@RestController
public class SysCategoryApiController implements SysCategoryApi {

    private final SysCategoryService categoryService;

    public SysCategoryApiController(SysCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 添加分类
     *
     * @param req {@link CategoryReq}
     * @return {@link Result}
     */
    @Override
    public Result add(CategoryReq req) {
        categoryService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除分类
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        categoryService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除分类
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        categoryService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新分类
     *
     * @param req {@link CategoryReq}
     * @return {@link Result}
     */
    @Override
    public Result update(CategoryReq req) {
        categoryService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询分类列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<CategoryRes>> findCategoryListByPage(QueryHelper<CategoryReq> req) {
        return Result.ok(categoryService.findCategoryListByPage(req.getQuery(), req.getPage()));
    }

    /**
     * 查询分类明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<CategoryRes> getCategoryDetail(Integer id) {
        return Result.ok(categoryService.getCategoryDetail(id));
    }

    /**
     * 查询分类列表
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<CategoryRes>> findCategoryList() {
        return Result.ok(categoryService.findCategoryList());
    }
}
