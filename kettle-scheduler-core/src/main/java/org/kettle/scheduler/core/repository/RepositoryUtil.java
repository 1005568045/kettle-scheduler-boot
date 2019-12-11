package org.kettle.scheduler.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.kettle.scheduler.common.enums.GlobalStatusEnum;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.kettle.scheduler.common.povo.TreeDTO;
import org.kettle.scheduler.common.utils.CollectionUtil;
import org.kettle.scheduler.common.utils.FileUtil;
import org.kettle.scheduler.common.utils.StringUtil;
import org.kettle.scheduler.core.dto.RepositoryDTO;
import org.kettle.scheduler.core.enums.RepTypeEnum;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.*;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * kettle资源库工具类
 *
 * @author lyf
 */
@Slf4j
public class RepositoryUtil {
    /**
     * 资源库缓存
     */
    private static final Map<Integer, AbstractRepository> DATABASE_REP = new ConcurrentHashMap<>();

    /**
     * 连接到数据库资源库
     *
     * @param dbRep 连接参数
     */
    private static AbstractRepository databaseRepository(RepositoryDTO dbRep) {
        // 检查资源库是否存在
        if (dbRep.getId() !=null && DATABASE_REP.containsKey(dbRep.getId())) {
			return DATABASE_REP.get(dbRep.getId());
        }
        // 获取数据连接元
        DatabaseMeta dataMeta = new DatabaseMeta(dbRep.getDbName(),dbRep.getDbType(),dbRep.getDbAccess(),dbRep.getDbHost(),dbRep.getDbName(),dbRep.getDbPort(),dbRep.getDbUsername(),dbRep.getDbPassword());
        // 数据库资源库元
        KettleDatabaseRepositoryMeta drm = new KettleDatabaseRepositoryMeta();
        drm.setConnection(dataMeta);
        drm.setName(dbRep.getRepName());
        // 初始化并连接到数据库资源库
        KettleDatabaseRepository rep = new KettleDatabaseRepository();
        rep.init(drm);
        // 开始连接资源库
        try {
            rep.connect(dbRep.getRepUsername(), dbRep.getRepPassword());
        } catch (KettleException e) {
            String msg = "连接数据库资源库失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
        // 缓存资源库信息
		if (dbRep.getId() != null) {
			DATABASE_REP.put(dbRep.getId(), rep);
		}
		return rep;
    }

    /**
     * 连接到文件资源库
     *
     * @param fileRep 连接参数
     */
    private static AbstractRepository fileRepository(RepositoryDTO fileRep) {
        // 检查资源库是否存在
        if (fileRep.getId() !=null && DATABASE_REP.containsKey(fileRep.getId())) {
            return DATABASE_REP.get(fileRep.getId());
        }
		// 判断文件是否存在
		String baseDir = FileUtil.replaceSeparator(fileRep.getRepBasePath());
		if (StringUtil.isEmpty(baseDir) || !new File(baseDir).exists()) {
			throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "文件资源库不存在");
		}
		// 文件资源库元数据
		KettleFileRepositoryMeta frm = new KettleFileRepositoryMeta();
		frm.setBaseDirectory(baseDir);
        frm.setName(fileRep.getRepName());
        // 初始化资源库
        KettleFileRepository rep = new KettleFileRepository();
        rep.init(frm);
        // 开始连接资源库
        try {
            rep.connect(fileRep.getRepUsername(), fileRep.getRepPassword());
        } catch (KettleException e) {
            String msg = "连接文件资源库失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
        // 缓存资源库信息
		if (fileRep.getId() != null) {
			DATABASE_REP.put(fileRep.getId(), rep);
		}
		return rep;
    }

    /**
     * 连接资源库
     * @param rep 连接参数
     */
    public static AbstractRepository connection(@NotNull RepositoryDTO rep) {
        // 资源库存在直接返回
        if (isExist(rep.getId())) {
            return getRepository(rep.getId());
        }
        // 不存在就创建资源库
		AbstractRepository repository = null;
		RepTypeEnum repTypeEnum = RepTypeEnum.getEnum(rep.getRepType());
		if (repTypeEnum != null) {
			switch (repTypeEnum) {
				case FILE:
					repository = fileRepository(rep);
					break;
				case DB:
					repository = databaseRepository(rep);
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + rep.getRepType());
			}
		}
        // 返回资源库
        return repository;
    }

    /**
     * 批量连接资源库
     * @param list 连接参数列表
     */
    public static void connectionBatch(List<RepositoryDTO> list) {
        list.forEach(RepositoryUtil::connection);
    }

    /**
     * 断开指定资源库
     * @param repId 资源库ID
     */
    public static void disconnection(Integer repId) {
        // 检查资源库是否存在
        if (DATABASE_REP.containsKey(repId)) {
            AbstractRepository repository = DATABASE_REP.get(repId);
            // 断开连接
            repository.disconnect();
            repository.clearSharedObjectCache();
            // 清除缓存
            DATABASE_REP.remove(repId);
        }
    }

    /**
     * 断开所有资源库
     */
    public static void disconnectionAll() {
        if (CollectionUtil.isNotEmpty(DATABASE_REP)) {
            // 断开连接
            DATABASE_REP.values().forEach(repository -> {
                repository.disconnect();
                repository.clearSharedObjectCache();
            });
            // 清除缓存
            DATABASE_REP.clear();
        }
    }

    /**
     * 判断资源库是否连接成功
     * @param repId 资源库ID
     * @return {@link Boolean}
     */
    public static boolean isExist(Integer repId) {
        return repId != null && DATABASE_REP.containsKey(repId);
    }

    /**
     * 通过资源库ID获取资源库
     * @param repId 资源库ID
     * @return {@link AbstractRepository}
     */
    public static AbstractRepository getRepository(Integer repId) {
        return DATABASE_REP.getOrDefault(repId, null);
    }

    /**
     * 通过资源库名称获取文件资源库
     * @param repId 资源库ID
     * @return {@link KettleFileRepository}
     */
    public static KettleFileRepository getFileRepository(Integer repId) {
        if (!DATABASE_REP.containsKey(repId)) {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "资源库不存在");
        }

        if (DATABASE_REP.get(repId) instanceof KettleFileRepository) {
            return (KettleFileRepository) DATABASE_REP.get(repId);
        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "资源库类型不匹配, 不是有效文件资源库");
        }
    }

    /**
     * 通过资源库名称获取数据库资源库
     * @param repId 资源库ID
     * @return {@link KettleDatabaseRepository}
     */
    public static KettleDatabaseRepository getDatabaseRepository(Integer repId) {
        if (!DATABASE_REP.containsKey(repId)) {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "资源库不存在");
        }

        if (DATABASE_REP.get(repId) instanceof KettleDatabaseRepository) {
            return (KettleDatabaseRepository) DATABASE_REP.get(repId);
        } else {
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, "资源库类型不匹配, 不是有效数据库资源库");
        }
    }

    /**
     * 遍历获取资源库信息
     * @param repository 资源库
     * @param dirPath 当前目录路径
     * @param objectType 查询指定脚本类型
     * @return {@link List}
     */
    public static List<TreeDTO<String>> getRepositoryTreeList(Repository repository, String dirPath, RepositoryObjectType objectType) {
        List<TreeDTO<String>> treeList = new ArrayList<>();
        try {
            // 获取当前的路径信息
            RepositoryDirectoryInterface rdi = repository.loadRepositoryDirectoryTree().findDirectory(dirPath);
            // 获取Directory信息
            List<TreeDTO<String>> dirTree = getDirectoryTree(rdi);
            // 获取Job和Transformation的信息
            List<TreeDTO<String>> elementTree = getElementTree(repository, rdi, objectType);
            // 设置子级数据
            dirTree.forEach(dt -> {
                dt.setChildren(getRepositoryTreeList(repository, dt.getExtra(), objectType));
            });
            // 合并数据
            treeList.addAll(dirTree);
            treeList.addAll(elementTree);
        } catch (KettleException e) {
            String msg = "遍历资源库信息失败";
            log.error(msg, e);
            throw new MyMessageException(GlobalStatusEnum.KETTLE_ERROR, msg);
        }
        return treeList;
    }

    /**
     * 获取当前目录下的子目录信息
     * @param rdi 当前目录
     * @return {@link List}
     */
    private static List<TreeDTO<String>> getDirectoryTree(RepositoryDirectoryInterface rdi) {
        List<TreeDTO<String>> treeList = new ArrayList<>();
        // 获取子目录的数量
        int subDirSize = rdi.getNrSubdirectories();
        if (subDirSize > 0) {
            for (int i = 0; i < subDirSize; i++) {
                RepositoryDirectory subDir = rdi.getSubdirectory(i);
                TreeDTO<String> tree = new TreeDTO<>();
                tree.setId(subDir.getObjectId().getId());
                tree.setText(subDir.getName());
                tree.setLeaf(false);
                tree.setExpand(true);
                tree.setExtra(subDir.getPath());
                treeList.add(tree);
            }
        }
        return treeList;
    }

    /**
     * 获取当前目录下的转换和作业
     * @param repository 资源库
     * @param rdi 当前目录
     * @param objectType 查询指定脚本类型
     * @return {@link List}
     * @throws KettleException 异常
     */
    private static List<TreeDTO<String>> getElementTree(Repository repository, RepositoryDirectoryInterface rdi, RepositoryObjectType objectType) throws KettleException {
        List<TreeDTO<String>> treeList = new ArrayList<>();
        List<RepositoryElementMetaInterface> list = repository.getJobAndTransformationObjects(rdi.getObjectId(), false);
        if (null != list) {
			String rdiPath = rdi.getPath();
			list.forEach(element -> {
                if (objectType == null || objectType.equals(element.getObjectType())) {
                    TreeDTO<String> tree = new TreeDTO<>();
                    tree.setId(element.getObjectType().getTypeDescription() + "@" + rdi.getObjectId().getId() + "@" + element.getObjectId().getId());
                    tree.setText(element.getName());
					tree.setIcon("jstree-file");
                    tree.setLeaf(true);
                    tree.setExpand(false);

                    if (rdiPath.endsWith("/")) {
						tree.setExtra(rdiPath.concat(element.getName()));
					} else {
						tree.setExtra(rdiPath.concat("/").concat(element.getName()));
					}
                    treeList.add(tree);
                }
            });
        }
        return treeList;
    }
}
