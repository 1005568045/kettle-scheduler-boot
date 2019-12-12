package org.kettle.scheduler.common.utils;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kettle.scheduler.common.exceptions.MyMessageException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * 文件操作工具, 同时继承common-io包的FileUtils
 *
 * @author lyf
 */
@Slf4j
public class FileUtil extends FileUtils {

	public static String separator = "/";

	/**
	 * 上传文件（重命名原文件）
	 * @param file 上传的文件流
	 * @param savePath 需要保存的路径
	 * @return {@link String} 保存后的文件路径
	 */
	public static String uploadFileRename(MultipartFile file, String savePath) {
		// 原文件名称
		String originalFilename = file.getOriginalFilename();
		if (StringUtils.isBlank(originalFilename)) {
			throw new MyMessageException("获取源文件名失败");
		}
		// 新文件名
		String newFilename = getFileName(originalFilename)
				.concat("-")
				.concat(String.valueOf(System.currentTimeMillis()))
				.concat(getFileSuffix(originalFilename));

		// 在字符串末尾添加分隔符
		String uploadPath = addSeparator(savePath);

		// 判断目录是否存在
		File dirFile = new File(uploadPath);
		if(dirFile.exists() || dirFile.mkdirs()){
			// 输出到文件
			File destFile = new File(dirFile.getAbsolutePath() + separator + newFilename);
			try {
				file.transferTo(destFile);
			} catch (IOException e) {
				String exMsg = MessageFormat.format("文件上传失败: msg = {0}", e.getMessage());
				throw new MyMessageException(exMsg);
			}
			return replaceSeparator(destFile.getAbsolutePath());
		}else {
			throw new MyMessageException("保存路径不存在");
		}
	}

	/**
	 * 上传文件（不重命名）
	 * @param file 上传的文件流
	 * @param savePath 需要保存的路径
	 * @return {@link String} 保存后的文件路径
	 */
	public static String uploadFile(MultipartFile file, String savePath) {
		// 原文件名称
		String originalFilename = file.getOriginalFilename();
		if (StringUtils.isBlank(originalFilename)) {
			throw new MyMessageException("获取源文件名失败");
		}

		// 在字符串末尾添加分隔符
		String uploadPath = addSeparator(savePath);

		// 判断目录是否存在
		File dirFile = new File(uploadPath);
		if(dirFile.exists() || dirFile.mkdirs()){
			// 判断文件是否存在
			File destFile = new File(dirFile.getAbsolutePath() + separator + originalFilename);
			if (destFile.exists()) {
				throw new MyMessageException("文件已存在");
			}
			// 输出到文件
			try {
				file.transferTo(destFile);
			} catch (IOException e) {
				String exMsg = MessageFormat.format("文件上传失败: msg = {0}", e.getMessage());
				throw new MyMessageException(exMsg);
			}
			return replaceSeparator(destFile.getAbsolutePath());
		}else {
			throw new MyMessageException("保存路径不存在");
		}
	}

    /**
     * 根据路径下载文档
     * @param response 响应
     * @param filePath 文档路径
     */
    public static void downloadFile(HttpServletResponse response, String filePath) {
		fileOutOperation(response, filePath, "attachment");
    }

    /**
     * 根据路径预览文档
     * @param response 响应
     * @param filePath 文档路径
     */
    public static void viewFile(HttpServletResponse response, String filePath) {
		fileOutOperation(response, filePath, "inline");
    }

    /**
     * 根据响应头部标识操作文档
     * @param response 响应
     * @param filePath 文档路径
     * @param header 响应头部标识
     */
    public static void fileOutOperation(HttpServletResponse response, String filePath, String header) {
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

	/**
	 * 在字符串末尾添加分隔符
	 * @param str 字符串
	 * @return {@link String}
	 */
	public static String addSeparator(String str) {
		String s = replaceSeparator(str);
		if(!s.endsWith(separator)) {
			s += separator;
		}
		return s;
	}

	/**
	 * 获取文件名后缀
	 * @param filePath 文件路径
	 * @return {@link String} 后缀
	 */
	public static String getFileSuffix(String filePath) {
		String s = replaceSeparator(filePath);
		return s.substring(s.lastIndexOf("."));
	}

	/**
	 * 获取文件名
	 * @param filePath 文件路径
	 * @return {@link String} 文件名
	 */
	public static String getFileName(String filePath) {
		String s = replaceSeparator(filePath);
		String fileFullName = s.substring(s.lastIndexOf("/")+1);
		return fileFullName.substring(0, fileFullName.lastIndexOf("."));
	}

	/**
	 * 获取文件名（带有后缀）
	 * @param filePath 文件路径
	 * @return {@link String} 文件名
	 */
	public static String getFileFullName(String filePath) {
		String s = replaceSeparator(filePath);
		return s.substring(s.lastIndexOf("/")+1);
	}
}
