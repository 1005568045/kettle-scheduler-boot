package org.kettle.scheduler.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.kettle.scheduler.common.constants.Regex;
import org.kettle.scheduler.common.enums.HeaderEnum;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * IP工具
 *
 * <br>获取本地局域网有效IP地址
 * <br>获取客户端真实有效IP地址
 *
 * @author lyf
 */
@Slf4j
public class IpUtil {
    private static final String ANY_HOST = "0.0.0.0";
    private static final String LOCALHOST = "127.0.0.1";
    private static final Pattern IP_PATTERN = Regex.IP;
    private static volatile String LOCAL_ADDRESS = null;

    public IpUtil() {
    }

    /**
     * 获取本地有效ip地址
     */
    public static String getIp() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        } else {
            InetAddress localAddress = getFirstValidAddress();
            if (localAddress != null) {
                LOCAL_ADDRESS = localAddress.getHostAddress();
            }
            return LOCAL_ADDRESS;
        }
    }

    /**
     * 为本地ip地址拼接端口号
     */
    public static String concatLocalIpPort(int port) {
        return concatIpPort(getIp(), port);
    }

    /**
     * 为ip地址拼接端口号
     */
    public static String concatIpPort(String ip, int port) {
        return ip == null ? null : ip.concat(":").concat(String.valueOf(port));
    }

    /**
     * 解析ip地址,获取ip和端口号
     */
    public static Object[] parseIpPort(String address) {
        String[] array = address.split(":");
        String host = array[0];
        int port = Integer.parseInt(array[1]);
        return new Object[]{host, port};
    }

    /**
     * 获取客户端真实ip地址
     */
    public static String getClientIp(HttpServletRequest request) {
        for (HeaderEnum header : HeaderEnum.values()) {
            String ip = request.getHeader(header.getCode());
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                if (header == HeaderEnum.X_FORWARDED_FOR) {
                    int index = ip.indexOf(",");
                    return index != -1 ? ip.substring(0, index) : ip;
                } else {
                    return ip;
                }
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 验证ip地址有效性
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress() && !address.isLinkLocalAddress()) {
            String name = address.getHostAddress();
            return name != null && !ANY_HOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches();
        } else {
            return false;
        }
    }

    /**
     * 获取第一个有效的局域网分配的ip地址
     */
    private static InetAddress getFirstValidAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface network = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = network.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (isValidAddress(address)) {
                            return address;
                        }
                    }
                }
            }
        } catch (Throwable var7) {
            log.error("Failed to retrieving ip address, " + var7.getMessage(), var7);
        }

        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable var4) {
            log.error("Failed to retrieving ip address, " + var4.getMessage(), var4);
        }

        log.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return null;
    }
}
