package org.kettle.scheduler.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP请求头枚举
 *
 * @author lyf
 */
@Getter
@AllArgsConstructor
public enum HeaderEnum {
    /**
     * X-Forwarded-For
     */
    X_FORWARDED_FOR("X-Forwarded-For"),
    /**
     * Proxy-Client-IP
     */
    PROXY_CLIENT_IP("Proxy-Client-IP"),
    /**
     * WL-Proxy-Client-IP
     */
    WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
    /**
     * HTTP_X_FORWARDED_FOR
     */
    HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
    /**
     * HTTP_X_FORWARDED
     */
    HTTP_X_FORWARDED("HTTP_X_FORWARDED"),
    /**
     * HTTP_X_CLUSTER_CLIENT_IP
     */
    HTTP_X_CLUSTER_CLIENT_IP("HTTP_X_CLUSTER_CLIENT_IP"),
    /**
     * HTTP_CLIENT_IP
     */
    HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
    /**
     * HTTP_FORWARDED_FOR
     */
    HTTP_FORWARDED_FOR("HTTP_FORWARDED_FOR"),
    /**
     * HTTP_FORWARDED
     */
    HTTP_FORWARDED("HTTP_FORWARDED"),
    /**
     * HTTP_VIA
     */
    HTTP_VIA("HTTP_VIA"),
    /**
     * REMOTE_ADDR
     */
    REMOTE_ADDR("REMOTE_ADDR"),
    /**
     * X-Real-IP
     */
    X_REAL_IP("X-Real-IP");

    private String code;
}
