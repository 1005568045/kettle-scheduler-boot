package org.kettle.scheduler.system.biz.service;

import org.kettle.scheduler.common.povo.PageHelper;
import org.kettle.scheduler.common.povo.PageOut;
import org.kettle.scheduler.common.utils.BeanUtil;
import org.kettle.scheduler.system.api.request.CategoryReq;
import org.kettle.scheduler.system.api.response.CategoryRes;
import org.kettle.scheduler.system.biz.entity.Category;
import org.kettle.scheduler.system.biz.repository.CategoryRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 分类管理业务逻辑层
 *
 * @author lyf
 */
@Service
public class SysCategoryService {
    private final CategoryRepository categoryRepository;

    public SysCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void add(CategoryReq req) {
        Category category = BeanUtil.copyProperties(req, Category.class);
        categoryRepository.save(category);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        categoryRepository.deleteInBatch(categories);
    }

    public void update(CategoryReq req) {
        Optional<Category> optional = categoryRepository.findById(req.getId());
        if (optional.isPresent()) {
            Category category = optional.get();
            BeanUtil.copyProperties(req, category);
            categoryRepository.save(category);
        }
    }

    public PageOut<CategoryRes> findCategoryListByPage(CategoryReq query, PageHelper page) {
        // 排序
        Sort sort = page.getSorts().isEmpty() ? Sort.by(Sort.Direction.DESC, "addTime") : page.getSorts();
        // 查询
        Category category = BeanUtil.copyProperties(query, Category.class);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreCase();
        Example<Category> example = Example.of(category, matcher);
        Page<Category> pageList = categoryRepository.findAll(example, PageRequest.of(page.getNumber(), page.getSize(), sort));
        // 封装数据
        List<CategoryRes> collect = pageList.get().map(t -> BeanUtil.copyProperties(t, CategoryRes.class)).collect(Collectors.toList());
        return new PageOut<>(collect, pageList.getNumber(), pageList.getSize(), pageList.getTotalElements(), pageList.getTotalPages());
    }

    public CategoryRes getCategoryDetail(Integer id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.map(category -> BeanUtil.copyProperties(category, CategoryRes.class)).orElse(null);
    }

    public List<CategoryRes> findCategoryList() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(rep -> BeanUtil.copyProperties(rep, CategoryRes.class)).collect(Collectors.toList());
    }
}
