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

/**
 * 文件操作工具, 同时继承common-io包的FileUtils
 *
 * @author lyf
 */
@Slf4j
public class FileUtil extends FileUtils {

	public static String separator = "/";

	/**
	 * 根据不同的encoding写入文件到本地
	 * @param inputByte 输入的字节
	 * @param outputFile 输出的文件
	 * @param overwrite 是否覆写文件
	 */
	public static void writeFile(byte[] inputByte, File outputFile, boolean overwrite) {
		// 不覆写就判断文件是否存在
		if (!overwrite && outputFile.exists()) {
			throw new MyMessageException("文件已存在");
		}
		try {
			// 输出文件不存在就创建
			touch(outputFile);
			// 开始写入文件
			writeByteArrayToFile(outputFile, inputByte);
		} catch (IOException e) {
			String msg = "写入本地文件失败";
			log.error(msg, e);
			throw new MyMessageException(msg);
		}
	}

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

		File destFile = new File(new File(addSeparator(savePath)).getAbsolutePath() + separator + newFilename);

		try {
			writeFile(file.getBytes(), destFile, true);
		} catch (IOException e) {
			throw new MyMessageException("读取上传文件流失败");
		}

		return replaceSeparator(destFile.getAbsolutePath());
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

		File destFile = new File(new File(addSeparator(savePath)).getAbsolutePath() + separator + originalFilename);

		try {
			writeFile(file.getBytes(), destFile, true);
		} catch (IOException e) {
			throw new MyMessageException("读取上传文件流失败");
		}

		return replaceSeparator(destFile.getAbsolutePath());
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
     * @param header 响应头部标识 eg: attachment, inline等
     */
    public static void fileOutOperation(HttpServletResponse response, String filePath, String header) {
        response.reset();
        try {
            File file = new File(filePath);
			@Cleanup
			FileInputStream fileInputStream = new FileInputStream(file);
			@Cleanup
			OutputStream outputStream = response.getOutputStream();
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
        return StringUtil.isEmpty(str) ? "" : str.replaceAll("\\\\", separator);
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
		String fileFullName = s.substring(s.lastIndexOf(separator)+1);
		int endIndex = fileFullName.lastIndexOf(".");
		return fileFullName.substring(0, endIndex<0 ? fileFullName.length() : endIndex);
	}

	/**
	 * 获取文件名（带有后缀）
	 * @param filePath 文件路径
	 * @return {@link String} 文件名
	 */
	public static String getFileFullName(String filePath) {
		String s = replaceSeparator(filePath);
		return s.substring(s.lastIndexOf(separator)+1);
	}

	/**
	 * 删除文件
	 * @param filePath 文件路径
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			try {
				forceDelete(file);
			} catch (IOException e) {
				String msg = "文件删除失败";
				log.error(msg, e);
				throw new MyMessageException(msg);
			}
		}
	}

	/**
	 * 删除目录
	 * @param dirPath 目录路径
	 */
	public static void deleteDir(String dirPath) {
		File file = new File(dirPath);
		if (file.exists() && file.isDirectory()) {
			try {
				deleteDirectory(file);
			} catch (IOException e) {
				String msg = "目录删除失败";
				log.error(msg, e);
				throw new MyMessageException(msg);
			}
		}
	}

	/**
	 * 获取当前文件的父路径
	 * @param filePath 文件路径
	 * @return {@link String} 父级路径
	 */
	public static String getParentPath(String filePath) {
		String s = replaceSeparator(filePath);
		return s.substring(0, s.lastIndexOf(separator));
	}
}
