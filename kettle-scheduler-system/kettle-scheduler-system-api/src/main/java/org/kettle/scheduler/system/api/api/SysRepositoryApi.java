package org.kettle.scheduler.system.api.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.povo.TreeDTO;
import org.kettle.scheduler.system.api.request.RepositoryReq;
import org.kettle.scheduler.system.api.response.RepositoryRes;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源库管理API
 *
 * @author lyf
 */
@Api(tags = "资源库管理API")
@RequestMapping("/sys/repository")
public interface SysRepositoryApi {

    /**
     * 添加资源库
     *
     * @param req {@link RepositoryReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "添加资源库")
    @PostMapping("/add.do")
    Result add(@RequestBody RepositoryReq req);

    /**
     * 通过id删除资源库
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @ApiOperation(value = "通过id删除资源库")
    @DeleteMapping("/delete.do")
    Result delete(@RequestParam("id") Integer id);

    /**
     * 批量删除资源库
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @ApiOperation(value = "批量删除资源库")
    @DeleteMapping("/deleteBatch.do")
    Result deleteBatch(@RequestBody List<Integer> ids);

    /**
     * 更新资源库
     *
     * @param req {@link RepositoryReq}
     * @return {@link Result}
     */
    @ApiOperation(value = "更新资源库")
    @PutMapping("/update.do")
    Result update(@RequestBody RepositoryReq req);

    /**
     * 根据条件查询资源库列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @ApiOperation(value = "根据条件查询资源库列表")
    @PostMapping("/findRepListByPage.do")
    Result<PageOut<RepositoryRes>> findRepListByPage(@RequestBody QueryHelper<RepositoryReq> req);

    /**
     * 查询资源库明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "查询资源库明细")
    @GetMapping("/getRepositoryDetail.do")
    Result<RepositoryRes> getRepositoryDetail(@RequestParam("id") Integer id);

    /**
     * 资源库列表
     *
     * @return {@link Result}
     */
    @ApiOperation(value = "资源库列表")
    @GetMapping("/findRepList.do")
    Result<List<RepositoryRes>> findRepList();

    /**
     * 根据资源库id查询资源库中job作业内容树
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "根据资源库id查询资源库中job作业内容树")
    @GetMapping("/findJobRepTreeById.do")
    Result<List<TreeDTO<String>>> findJobRepTreeById(@RequestParam("id") Integer id);

    /**
     * 根据资源库id查询资源库中trans转换内容树
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @ApiOperation(value = "根据资源库id查询资源库中trans转换内容树")
    @GetMapping("/findTransRepTreeById.do")
    Result<List<TreeDTO<String>>> findTransRepTreeById(@RequestParam("id") Integer id);

	/**
	 * 测试资源库链接
	 *
	 * @param req {@link RepositoryReq}
	 * @return {@link Result}
	 */
	@ApiOperation(value = "测试资源库链接")
	@PostMapping("/testConnection.do")
	Result testConnection(@RequestBody RepositoryReq req);
}
