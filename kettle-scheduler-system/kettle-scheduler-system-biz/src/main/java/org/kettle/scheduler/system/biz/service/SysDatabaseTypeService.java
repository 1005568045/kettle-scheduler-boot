package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.response.DatabaseTypeRes;
import org.kettle.scheduler.system.biz.entity.DatabaseType;
import org.kettle.scheduler.system.biz.repository.DatabaseTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库类型服务层
 *
 * @author lyf
 */
@Service
public class SysDatabaseTypeService {
    private final DatabaseTypeRepository databaseTypeRepository;

    public SysDatabaseTypeService(DatabaseTypeRepository databaseTypeRepository) {
        this.databaseTypeRepository = databaseTypeRepository;
    }

    public List<DatabaseTypeRes> findDbTypeList() {
        List<DatabaseType> list = databaseTypeRepository.findAll();
        return list.stream().map(db -> BeanUtil.copyProperties(db, DatabaseTypeRes.class)).collect(Collectors.toList());
    }
}
