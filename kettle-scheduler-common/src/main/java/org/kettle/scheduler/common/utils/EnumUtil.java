package org.kettle.scheduler.common.utils;

import org.kettle.scheduler.common.enums.base.BaseEnum;
import org.kettle.scheduler.common.povo.EnumInfoDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 枚举工具
 *
 * @author lyf
 */
public class EnumUtil {

    /**
     * 获取枚举列表信息
     *
     * @param enums 枚举数组
     * @return {@link List}
     */
    public static List<EnumInfoDTO> getEnumInfo(BaseEnum[] enums) {
        return Arrays.stream(enums).map(e -> {
            EnumInfoDTO dto = new EnumInfoDTO();
            dto.setCode(e.getCode().toString());
            dto.setValue(e.getDesc());
            return dto;
        }).collect(Collectors.toList());
    }
}
