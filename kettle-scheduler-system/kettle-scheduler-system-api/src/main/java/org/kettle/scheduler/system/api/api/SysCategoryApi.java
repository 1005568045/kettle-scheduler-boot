package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.system.api.request.CategoryReq;
import org.kettle.scheduler.system.api.response.CategoryRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理API
 *
 * @author lyf
 */
@Api(tags = "分类管理API")
@RequestMapping("/sys/category")
public interface SysCategoryApi {

    /**
     * 添加分类
     *
     * @param req {@link CategoryReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加分类")
    @PostMapping("/add")
    Result add(@RequestBody CategoryReq req);

    /**
     * 通过id删除分类
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除分类")
    @DeleteMapping("/delete")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除分类
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除分类")
    @DeleteMapping("/deleteBatch")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新分类
     *
     * @param req {@link CategoryReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新分类")
    @PutMapping("/update")
    Result update(@RequestBody CategoryReq req);

    /**
     * 根据条件查询分类列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询分类列表")
    @PostMapping("/findCategoryListByPage")
    Result<PageOut<CategoryRes>> findCategoryListByPage(@RequestBody QueryHelper<CategoryReq> req);

    /**
     * 查询分类明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询分类明细")
    @GetMapping("/getCategoryDetail")
    Result<CategoryRes> getCategoryDetail(@RequestParam("id") Integer id);

    /**
     * 查询分类列表
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "查询分类列表")
    @PostMapping("/findCategoryList")
    Result<List<CategoryRes>> findCategoryList();
}
