package org.kettle.scheduler.common.id;

import org.springframework.stereotype.Component;
import org.kettle.scheduler.common.utils.StringUtil;

/**
 * 雪花算法id生成器-Spring组件
 *
 * @author lyf
 */
@Component
public class SnowFlakeIdGenerate {
    private SnowFlakeId snowFlakeId = new SnowFlakeId(0, 0);

    /**
     * 获取字符串id
     */
    public String getId() {
        return String.valueOf(snowFlakeId.nextId());
    }

    /**
     * 获取有前缀的id
     */
    public String getId(String idPrefix) {
        if (StringUtil.hasText(idPrefix)) {
            return idPrefix + "_" + getId();
        } else {
            return getId();
        }
    }

    /**
     * 获取数字id
     */
    public Long getLongId() {
        return snowFlakeId.nextId();
    }
}
