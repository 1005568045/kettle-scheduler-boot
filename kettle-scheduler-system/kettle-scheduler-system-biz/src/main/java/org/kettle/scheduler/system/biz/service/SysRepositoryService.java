package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.povo.TreeDTO;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.core.dto.RepositoryDTO;
import org.kettle.scheduler.core.repository.RepositoryUtil;
import org.kettle.scheduler.system.api.request.RepositoryReq;
import org.kettle.scheduler.system.api.response.RepositoryRes;
import org.kettle.scheduler.system.biz.entity.Repository;
import org.kettle.scheduler.system.biz.repository.RepositoryRepository;
import org.pentaho.di.repository.AbstractRepository;
import org.pentaho.di.repository.RepositoryObjectType;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源库管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysRepositoryService {
    private final RepositoryRepository repRepository;

    public SysRepositoryService(RepositoryRepository repRepository) {
        this.repRepository = repRepository;
    }

    public void add(RepositoryReq req) {
        Repository rep = BeanUtil.copyProperties(req, Repository.class);
        repRepository.save(rep);
    }

    public void delete(Integer id) {
        repRepository.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        List<Repository> repositories = repRepository.findAllById(ids);
        repRepository.deleteInBatch(repositories);
    }

    public void update(RepositoryReq req) {
        Optional<Repository> optional = repRepository.findById(req.getId());
        if (optional.isPresent()) {
            Repository rep = optional.get();
            BeanUtil.copyProperties(req, rep);
            repRepository.save(rep);
        }
    }

    public PageOut<RepositoryRes> findRepListByPage(RepositoryReq query, PageHelper page) {
        // 排序
        Sort sort = page.getSorts().isEmpty() ? Sort.by(Sort.Direction.DESC, "addTime") : page.getSorts();
        // 查询
		Page<Repository> pageList = null;
		if (query != null) {
			Repository rep = BeanUtil.copyProperties(query, Repository.class);
			Example<Repository> example = Example.of(rep, ExampleMatcher.matchingAll().withIgnoreCase());
			pageList = repRepository.findAll(example, PageRequest.of(page.getNumber(), page.getSize(), sort));
		} else {
			pageList = repRepository.findAll(PageRequest.of(page.getNumber(), page.getSize(), sort));
		}
        // 封装数据
        List<RepositoryRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, RepositoryRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements());
    }

    public RepositoryRes getRepositoryDetail(Integer id) {
        Optional<Repository> optional = repRepository.findById(id);
        return optional.map(repository -> BeanUtil.copyProperties(repository, RepositoryRes.class)).orElse(null);
    }

    public List<RepositoryRes> findRepList() {
        List<Repository> list = repRepository.findAll();
        return list.stream().map(rep -> BeanUtil.copyProperties(rep, RepositoryRes.class)).collect(Collectors.toList());
    }

    public List<TreeDTO<String>> findRepTreeById(Integer id, RepositoryObjectType objectType) {
        Optional<Repository> optional = repRepository.findById(id);
        if (optional.isPresent()) {
            Repository rep = optional.get();
            RepositoryDTO repDto = BeanUtil.copyProperties(rep, RepositoryDTO.class);
            // 连接资源库
            AbstractRepository repository = RepositoryUtil.connection(repDto);
            // 遍历获取资源库信息
            return RepositoryUtil.getRepositoryTreeList(repository, "/", objectType);
        } else {
            return null;
        }
    }

}
