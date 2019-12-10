package org.kettle.scheduler.common.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kettle.scheduler.common.exceptions.MyMessageException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 文件操作工具, 同时继承common-io包的FileUtils
 *
 * @author lyf
 */
@Slf4j
public class FileUtil extends FileUtils {

    /**
     * 根据路径下载文档
     * @param response 响应
     * @param filePath 文档路径
     */
    public static void downloadFile(HttpServletResponse response, String filePath) {
        fileOperation(response, filePath, "attachment");
    }

    /**
     * 根据路径预览文档
     * @param response 响应
     * @param filePath 文档路径
     */
    public static void viewFile(HttpServletResponse response, String filePath) {
        fileOperation(response, filePath, "inline");
    }

    /**
     * 根据响应头部标识操作文档
     * @param response 响应
     * @param filePath 文档路径
     * @param header 响应头部标识
     */
    public static void fileOperation(HttpServletResponse response, String filePath, String header) {
        response.reset();
        try {
            File file = new File(filePath);
            @Cleanup FileInputStream fileInputStream = new FileInputStream(file);
            @Cleanup OutputStream outputStream = response.getOutputStream();
            // 下载attachment,下载时文件名必须带有后缀名,不然下载后的文件不正确,例如：合同.pdf,头像.png
            response.setHeader("Content-Disposition", header + "; filename="+ URLEncoder.encode(file.getName(), "UTF-8"));
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
            outputStream.flush();
        } catch (IOException e) {
            String msg = "操作文件失败";
            log.error(msg, e);
            throw new MyMessageException(msg);
        }
    }

    /**
     * 替换反斜杠为正斜杠
     * @param str 要替换的字符串
     * @return {@link String}
     */
    public static String replaceSeparator(String str) {
        return StringUtil.isEmpty(str) ? "" : str.replaceAll("\\\\", "/");
    }
}
