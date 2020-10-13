package com.example.demo.utils.file.word;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@Slf4j
public class WordDonload {

    /**
     * 在工具类中的实现方法
     */
    public static void exportWord(HttpServletRequest request, HttpServletResponse response, String fileName, String content) throws Exception {
        try {
            //这里是必须要设置编码的，不然导出中文就会乱码。
            byte b[] = content.getBytes("utf-8");
            //将字节数组包装到流中
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            //生成word
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            directory.createDocument(fileName, bais);
            //输出文件
            response.setCharacterEncoding("utf-8");
            //设置word格式
            response.setContentType("application/msword");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".docx");
            OutputStream ostream = response.getOutputStream();
            poifs.writeFilesystem(ostream);
            bais.close();
            ostream.close();
        } catch (Exception e) {
            //异常处理
            log.error("文件导出错误", e);
        }
    }

}
