package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.povo.TreeDTO;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.system.api.api.SysRepositoryApi;
import org.kettle.scheduler.system.api.request.RepositoryReq;
import org.kettle.scheduler.system.api.response.RepositoryRes;
import org.kettle.scheduler.system.biz.service.SysRepositoryService;
import org.pentaho.di.repository.RepositoryObjectType;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资源库管理
 *
 * @author lyf
 */
@RestController
public class SysRepositoryApiController implements SysRepositoryApi {
    private final SysRepositoryService repositoryService;

    public SysRepositoryApiController(SysRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * 添加资源库
     *
     * @param req {@link RepositoryReq}
     * @return {@link Result}
     */
    @Override
    public Result add(RepositoryReq req) {
        req.setRepBasePath(FileUtil.replaceSeparator(req.getRepBasePath()));
        repositoryService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除资源库
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        repositoryService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除资源库
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        repositoryService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新资源库
     *
     * @param req {@link RepositoryReq}
     * @return {@link Result}
     */
    @Override
    public Result update(RepositoryReq req) {
        repositoryService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询资源库列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<RepositoryRes>> findRepListByPage(QueryHelper<RepositoryReq> req) {
        return Result.ok(repositoryService.findRepListByPage(req.getQuery(), req.getPage()));
    }

    /**
     * 查询资源库明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<RepositoryRes> getRepositoryDetail(Integer id) {
        return Result.ok(repositoryService.getRepositoryDetail(id));
    }

    /**
     * 资源库列表
     *
     * @return {@link Result}
     */
    @Override
    public Result<List<RepositoryRes>> findRepList() {
        return Result.ok(repositoryService.findRepList());
    }

    /**
     * 根据资源库id查询资源库中job作业内容树
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<List<TreeDTO<String>>> findJobRepTreeById(Integer id) {
        return Result.ok(repositoryService.findRepTreeById(id, RepositoryObjectType.JOB));
    }

    /**
     * 根据资源库id查询资源库中trans转换内容树
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<List<TreeDTO<String>>> findTransRepTreeById(Integer id) {
        return Result.ok(repositoryService.findRepTreeById(id, RepositoryObjectType.TRANSFORMATION));
    }
}
