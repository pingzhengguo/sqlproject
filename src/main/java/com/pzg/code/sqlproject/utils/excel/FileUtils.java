package com.pzg.code.sqlproject.utils.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述：文件相关工具类
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {


    /**
     * 描述：对文件名称再编码
     * 详设编号：
     *
     * @param fileName
     * @param agent
     * @return java.lang.String
     * @throws
     * @modified By wangliangchu
     */
    public static String encodeDownloadFileName(String fileName, String agent) throws IOException {
        //火狐浏览器
        if (agent.contains("Firefox")) {
            fileName = "=?UTF-8?B?"
                    + new BASE64Encoder().encode(fileName.getBytes("utf-8"))
                    + "?=";
            fileName = fileName.replaceAll("\r\n", "");
        } else {
            //IE及其他浏览器
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", " ");
        }

        return fileName;
    }

    /**
     * 描述：设置导出Excel文件的响应头
     *
     * @param request
     * @param response
     * @param fileName
     * @return void
     * @throws
     */
    public static void setExcelDownloadResponseHeader(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String fileName) {
        //获取客户端浏览器类型
        String agent = request.getHeader("User-Agent");

        try {
            //对文件名称再次编码
            String encodedFileName = encodeDownloadFileName(fileName, agent);
            // 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType("application/x-download;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;fileName=" + encodedFileName);
        } catch (IOException e) {
            log.error("FileUtils.setExcelDownloadResponseHeader() has error!{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 描述：设置zip下载响应头
     *
     * @param request
     * @param response
     * @param fileName
     */
    public static void setZipDownloadResponseHeader(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    String fileName) throws IOException {

        //获取客户端浏览器类型
        String agent = request.getHeader("User-Agent");

        try {
            //对文件名称再次编码
            String encodedFileName = encodeDownloadFileName(fileName, agent);

            // 表示不能用浏览器直接打开
            response.setHeader("Connection", "close");
            // 告诉客户端允许断点续传多线程连接下载
            response.setHeader("Accept-Ranges", "bytes");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + encodedFileName);
        } catch (IOException e) {
            log.error("FileUtils.setZipDownloadResponseHeader() has error!{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 描述：给多个文件打包成zip文件
     *
     * @param files   不含有文件夹
     * @param zipFile
     */
    public static void zipFiles(File[] files, File zipFile) {

        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {

            for (File file : files) {
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                try (FileInputStream fin = new FileInputStream(file)) {
                    IOUtils.copy(fin, zipOut);
                }
                //静默删除文件
                deleteQuietly(file);
                zipOut.closeEntry();
            }
            zipOut.flush();
        } catch (Exception e) {
            log.error("FileUtils.zipFiles() has error!{}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 描述：文件打包成zip写到流中
     *
     * @param dir
     * @param os
     */
    public static void writeZipToOutputStream(File dir, OutputStream os) {

        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(os))) {

            File[] files = dir.listFiles((filename) -> filename.isFile() && filename.getName().endsWith(".xlsx"));

            if (files != null) {
                for (File file : files) {
                    zipOut.putNextEntry(new ZipEntry(file.getName()));
                    try (FileInputStream fin = new FileInputStream(file)) {
                        IOUtils.copy(fin, zipOut);
                    }
                    zipOut.closeEntry();
                }
                zipOut.flush();
            }

        } catch (Exception e) {
            log.error("FileUtils.writeZipToOutputStream() has error!{}", e);
            throw new RuntimeException(e);
        } finally {
            //静默删除文件夹
            deleteQuietly(dir);
        }
    }

    /**
     * 描述：将excel文件写入响应
     *
     * @param excelFile
     * @param os
     * @return void
     * @throws
     */
    public static void writeExcelToOutputStream(File excelFile, OutputStream os) {

        try (InputStream is = new FileInputStream(excelFile)) {
            IOUtils.copy(is, os);
        } catch (Exception e) {
            log.error("FileUtils.writeExcelToOutputStream() has error!{}", e);
        }
    }

    /**
     * 描述：导出一个空的excel
     *
     * @param excelName
     * @param sheetName
     * @param request
     * @param response
     * @throws
     */
    public static void exportEmptyExcel(String excelName, String sheetName, HttpServletRequest request, HttpServletResponse response) {
        ExportParams exportParams = new ExportParams(null, sheetName, ExcelType.XSSF);
        exportParams.setStyle(MyExcelStyle.class);
        Workbook wb = ExcelExportUtil.exportExcel(exportParams, Object.class, Lists.newArrayListWithExpectedSize(0));
        setExcelDownloadResponseHeader(request, response, excelName);
        try (OutputStream os = response.getOutputStream()) {
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            log.error("FileUtils.exportEmptyExcel() has error!{}", e);
        } finally {
            if (null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    log.error("FileUtils.exportEmptyExcel() has error!{}", e);
                }
            }
        }
    }

    /**
     * 描述：获取唯一id
     *
     * @param
     * @return java.lang.String
     * @throws
     */
    public static String genUniqueName() {
        return UUID.randomUUID().toString().toLowerCase();
    }

}

