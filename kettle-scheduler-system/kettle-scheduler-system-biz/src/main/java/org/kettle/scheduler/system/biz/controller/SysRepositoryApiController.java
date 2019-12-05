package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.groups.Insert;
import org.kettle.scheduler.common.groups.Update;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.povo.TreeDTO;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.common.utils.ValidatorUtil;
import org.kettle.scheduler.core.enums.RepTypeEnum;
import org.kettle.scheduler.system.api.api.SysRepositoryApi;
import org.kettle.scheduler.system.api.request.RepositoryReq;
import org.kettle.scheduler.system.api.response.RepositoryRes;
import org.kettle.scheduler.system.biz.service.SysRepositoryService;
import org.pentaho.di.repository.RepositoryObjectType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.groups.Default;
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
    public Result add(@Validated({Insert.class, Default.class}) RepositoryReq req) {
		// 根据选项进行参数验证
		validatedRep(req);
		// 添加资源库
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
    public Result update(@Validated({Update.class, Default.class}) RepositoryReq req) {
		// 根据选项进行参数验证
		validatedRep(req);
		// 修改
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

	/**
	 * 测试资源库链接
	 *
	 * @param req {@link RepositoryReq}
	 * @return {@link Result}
	 */
	@Override
	public Result testConnection(@Validated({Default.class}) RepositoryReq req) {
		// 根据选项进行参数验证
		validatedRep(req);
		// 测试链接
		repositoryService.testConnection(req);
		return Result.ok();
	}

	private void validatedRep(RepositoryReq req) {
		switch (RepTypeEnum.getEnum(req.getRepType())) {
			case FILE:
				String result1 = ValidatorUtil.validateWithString(req, RepositoryReq.FileRep.class);
				if (!StringUtil.isEmpty(result1)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result1);
				}
				break;
			case DB:
				String result2 = ValidatorUtil.validateWithString(req, RepositoryReq.DatabaseRep.class);
				if (!StringUtil.isEmpty(result2)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result2);
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + RepTypeEnum.getEnum(req.getRepType()));
		}
	}
}
