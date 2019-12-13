package org.kettle.scheduler.system.biz.controller;

import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.groups.Insert;
import org.kettle.scheduler.common.groups.Update;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.QueryHelper;
import org.kettle.scheduler.common.povo.Result;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.common.utils.ValidatorUtil;
import org.kettle.scheduler.core.enums.RepTypeEnum;
import org.kettle.scheduler.system.api.api.SysTransApi;
import org.kettle.scheduler.system.api.enums.RunTypeEnum;
import org.kettle.scheduler.system.api.request.TransReq;
import org.kettle.scheduler.system.api.response.TransRes;
import org.kettle.scheduler.system.biz.constant.KettleConfig;
import org.kettle.scheduler.system.biz.entity.Trans;
import org.kettle.scheduler.system.biz.service.SysTransService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 转换管理API
 *
 * @author lyf
 */
@RestController
public class SysTransApiController implements SysTransApi {

    private final SysTransService transService;

    public SysTransApiController(SysTransService transService) {
        this.transService = transService;
    }

	private void validatedParam(TransReq req) {
		switch (RunTypeEnum.getEnum(req.getTransType())) {
			case FILE:
				String result1 = ValidatorUtil.validateWithString(req, TransReq.File.class);
				if (!StringUtil.isEmpty(result1)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result1);
				}
				break;
			case REP:
				String result2 = ValidatorUtil.validateWithString(req, TransReq.Rep.class);
				if (!StringUtil.isEmpty(result2)) {
					throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, result2);
				}
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + RepTypeEnum.getEnum(req.getTransType()));
		}
	}

    /**
     * 添加转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @Override
    public Result add(@Validated({Insert.class, Default.class}) TransReq req, MultipartFile transFile) {
		// 参数验证
		validatedParam(req);
		// 保存上传文件
		if (RunTypeEnum.FILE.getCode().equals(req.getTransType())) {
			if (transFile == null || transFile.isEmpty()) {
				throw new MyMessageException(GlobalStatusEnum.ERROR_PARAM, "上传文件不能为空");
			}
			req.setTransPath(FileUtil.uploadFile(transFile, KettleConfig.uploadPath));
		}
        transService.add(req);
        return Result.ok();
    }

    /**
     * 通过id删除转换
     *
     * @param id 要删除的数据的id
     * @return {@link Result}
     */
    @Override
    public Result delete(Integer id) {
        transService.delete(id);
        return Result.ok();
    }

    /**
     * 批量删除转换
     *
     * @param ids 要删除数据的{@link List}集
     * @return {@link Result}
     */
    @Override
    public Result deleteBatch(List<Integer> ids) {
        transService.deleteBatch(ids);
        return Result.ok();
    }

    /**
     * 更新转换
     *
     * @param req {@link TransReq}
     * @return {@link Result}
     */
    @Override
    public Result update(@Validated({Update.class, Default.class}) TransReq req) {
        transService.update(req);
        return Result.ok();
    }

    /**
     * 根据条件查询转换列表
     *
     * @param req {@link QueryHelper}
     * @return {@link Result}
     */
    @Override
    public Result<PageOut<TransRes>> findTransListByPage(QueryHelper<TransReq> req) {
        return Result.ok(transService.findTransListByPage(req.getQuery(), req.getPage().getPageable()));
    }

    /**
     * 查询转换明细
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result<TransRes> getTransDetail(Integer id) {
        return Result.ok(transService.getTransDetail(id));
    }

    /**
     * 全部启动
     *
     * @return {@link Result}
     */
    @Override
    public Result startAllTrans() {
        transService.startAllTrans();
        return Result.ok();
    }

    /**
     * 单个启动
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result startTrans(Integer id) {
        transService.startTrans(id);
        return Result.ok();
    }

    /**
     * 全部停止
     *
     * @return {@link Result}
     */
    @Override
    public Result stopAllTrans() {
        transService.stopAllTrans();
        return Result.ok();
    }

    /**
     * 单个停止
     *
     * @param id 根据id查询
     * @return {@link Result}
     */
    @Override
    public Result stopTrans(Integer id) {
        transService.stopTrans(id);
        return Result.ok();
    }

	/**
	 * 验证名称是否存在
	 *
	 * @param transName 转换名
	 * @return 只能返回true或false
	 */
	@Override
	public String transNameExist(String transName) {
		if (StringUtil.isEmpty(transName)) {
			return "true";
		} else {
			Trans trans = transService.getByTransName(transName);
			if (trans != null) {
				return "false";
			} else {
				return "true";
			}
		}
	}
}
