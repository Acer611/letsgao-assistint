package com.umessage.letsgo.assistant.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件下载工具类
 * Created by gaofei on 2017/1/5.
 */
public class FileDownloadUtil {

    public static HttpServletResponse download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void downloadLocal(HttpServletResponse response) throws FileNotFoundException {
        // 下载本地文件
        String fileName = "Operator.doc".toString(); // 文件的默认保存名
        // 读到流中
        InputStream inStream = new FileInputStream("c:/Operator.doc");// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File  downloadNet(String headUrl) throws MalformedURLException {
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;
        File file = null;
        URL url = new URL(headUrl);
        //获取系统临时文件路径
        String folder =System.getProperty("java.io.tmpdir");
        String filename = UuidUtil.getUuidStr();
        String filePath = folder + System.getProperty("file.separator") + filename;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(filePath);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            file = new File(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  file;
    }

 /*   public static void main(String[] args) throws Exception{
        String headUrl = "http://wx.qlogo.cn/mmopen/ibX2S5qapW9XqpQlINP36hqbCbib7NsBrYScmVDKpgUzAm6qSk9vsCnXG2jcmxHjKummAw2kAdvUSuZicAwAgstW5aBuSAW9W1p/0";
        String targetPath ="F://abcd.jpg";
        downloadNet(headUrl,targetPath);
    }*/
}
