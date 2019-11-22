package org.kettle.scheduler.common.id;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.kettle.scheduler.common.utils.StringUtil;

import java.io.Serializable;
import java.util.Properties;

/**
 * 雪花算法生成策略
 *
 * @author lyf
 */
public class SnowFlakeIdGenerateStrategy implements Configurable, IdentifierGenerator {
    private String idPrefix;
    private SnowFlakeId idGenerate = new SnowFlakeId(0, 0);

    /**
     * 从GenericGenerator的parameters中获取前缀参数,例如: {@code @GenericGenerator(parameters = {@Parameter(name="idPrefix", value="USER"))}
     */
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        this.idPrefix = properties.getProperty("idPrefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        // 线程安全
        synchronized (SnowFlakeIdGenerateStrategy.class) {
            long idNo = idGenerate.nextId();
            if (StringUtil.hasText(idPrefix)) {
                return idPrefix + "_" + idNo;
            } else {
                return idNo;
            }
        }
    }
}
