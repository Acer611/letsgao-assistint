package com.umessage.letsgo.assistant.common.utils;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by ZhaoYidong on 2017/1/4.
 */
public class NewImageUtils {

    /**
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param file
     *            源文件(图片)
     * @param waterFile
     *            水印文件(图片)
     * @param x
     *            距离右下角的X偏移量
     * @param y
     *            距离右下角的Y偏移量
     * @param alpha
     *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */
     public static File watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = waterImg.getWidth();// 获取层图的宽度
        int waterImgHeight = waterImg.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
         try {
             ImageIO.write(buffImg, "jpg", file);
         } catch (IOException e1) {
             e1.printStackTrace();
         }
        return file;
    }

    /**
      * 输出水印图片
      *
      * @param buffImg
     *            图像加水印之后的BufferedImage对象
      * @param savePath
      *            图像加水印之后的保存路径
     */
    public static void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
             ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
         } catch (IOException e1) {
             e1.printStackTrace();
         }
    }



    /**
     * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
     * mergeImage方法不做判断，自己判断。
     *
     * @param img1
     *            待合并的第一张图
     * @param img2
     *            带合并的第二张图
     * @return 返回合并后的BufferedImage对象
     * @throws IOException
     */
    private static BufferedImage mergeImage(BufferedImage img1, BufferedImage img2)
            throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();
        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);
        // 生成新图片
        BufferedImage DestImage = null;
        DestImage = new BufferedImage(w1,h1,BufferedImage.TYPE_INT_RGB);

        DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
        // 加载水印图片文件
        Graphics2D resizedG = DestImage.createGraphics();
        resizedG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,(float) 1));
        resizedG.drawImage(img2, w1 / 2 - w2 / 2, h1 / 2 - h2 / 2, null);
        resizedG.dispose();
        // 画图
        return DestImage;
    }


    /**
     * 给图片添加水印、可设置水印图片旋转角度
     *
     * @param logoText
     *            水印文字
     * @param file
     *            文件
     * @param x
     *            水平位置(与左相比)
     * @param y
     *            垂直位置(与顶相比)
     * @param clarity
     *            透明度(小于1的数)越接近0越透明
     */
    public static void waterMarkByText(String logoText, File file, Integer x, Integer y, Integer fontSize,
                                       Float clarity) {
        // 主图片的路径
        InputStream is = null;
        OutputStream os = null;
        try {
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font("宋体", Font.CENTER_BASELINE, fontSize));
            g.setColor(Color.white);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, clarity));

            int width_1 = fontSize * getLength(logoText);
            int height_1 = fontSize;
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if(x < 0){
                x = widthDiff / 2;
            }else if(x > widthDiff){
                x = widthDiff;
            }
            if(y < 0){
                y = heightDiff / 2;
            }else if(y > heightDiff){
                y = heightDiff;
            }
            g.drawString(logoText, x, y + height_1);
            g.dispose();
            ImageIO.write(bufferedImage, "jpg", file);
            System.out.println("添加水印文字完成!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }




    /**
     * 图片压缩主方法
     * @param file 文件
     *
     * @param w
     *            目标宽
     * @param h
     *            目标高
     * @param JPEGcompression
     *            压缩质量/百分比
     * @author zhouqz
     */
    public static File ImgCompress(File file,
                                     int w, int h, float JPEGcompression) {
            try {
                BufferedImage bufferedImage =  ImageIO.read(file);

                int new_w = w;
                int new_h = h;

                BufferedImage image_to_save = new BufferedImage(new_w, new_h,
                        bufferedImage.getType());
                image_to_save.getGraphics().drawImage(
                        bufferedImage.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,
                        0, null);
                FileOutputStream fos = new FileOutputStream(file); // 输出到文件流

                // 新的方法
                int dpi = 300;//分辨率
                saveAsJPEG(dpi, image_to_save, JPEGcompression, fos);
                //关闭输出流
                fos.close();
                //返回压缩后的图片地址
            } catch (IOException ex) {

            }


        return file;

    }

    /**
     * 以JPEG编码保存图片
     *
     * @param dpi
     *            分辨率
     * @param image_to_save
     *            要处理的图像图片
     * @param JPEGcompression
     *            压缩比
     * @param fos
     *            文件输出流
     * @throws IOException
     */
    public static void saveAsJPEG(Integer dpi, BufferedImage image_to_save,
                                  float JPEGcompression, FileOutputStream fos) throws IOException {

        // useful documentation at
        // http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
        // useful example program at
        // http://johnbokma.com/java/obtaining-image-metadata.html to output
        // JPEG data

        // old jpeg class
        // com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder =
        // com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
        // com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam =
        // jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);

        // Image writer
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO
                .getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                new ImageTypeSpecifier(image_to_save), null);

        // if(dpi != null && !dpi.equals("")){
        //
        // //old metadata
        // //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
        // //jpegEncodeParam.setXDensity(dpi);
        // //jpegEncodeParam.setYDensity(dpi);
        //
        // //new metadata
        // Element tree = (Element)
        // imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
        // Element jfif =
        // (Element)tree.getElementsByTagName("app0JFIF").item(0);
        // jfif.setAttribute("Xdensity", Integer.toString(dpi) );
        // jfif.setAttribute("Ydensity", Integer.toString(dpi));
        //
        // }

        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

            // old compression
            // jpegEncodeParam.setQuality(JPEGcompression,false);

            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
                    .getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        // old write and clean
        // jpegEncoder.encode(image_to_save, jpegEncodeParam);

        // new Write and clean up
        imageWriter.write(imageMetaData,
                new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

    }
}
