package com.pay.tg.utils;

import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {


    //对图片进行处理 - 提高识别度
    public static BufferedImage convertImage(BufferedImage image) throws Exception {
        //按指定宽高创建一个图像副本
        //image = ImageHelper.getSubImage(image, 0, 0, image.getWidth(), image.getHeight());
        //图像转换成灰度的简单方法 - 黑白处理
        image = ImageHelper.convertImageToGrayscale(image);
        //图像缩放 - 放大n倍图像
        image = ImageHelper.getScaledInstance(image, image.getWidth() * 3, image.getHeight() * 3);
        return image;
    }


    /**
     * 远程图片转BufferedImage
     *
     * @param destUrl 远程图片地址
     * @return
     */
    public static BufferedImage getBufferedImageDestUrl(String destUrl) {
        HttpURLConnection conn = null;
        BufferedImage image = null;
        try {
            URL url = new URL(destUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                image = ImageIO.read(conn.getInputStream());
                return image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return image;
    }

    /**
     * 读取网络中的图片
     *
     * @param file
     * @param destUrl
     * @return
     */
    public static File getUrlFile(File file, String destUrl) {
        try {
            URL url = new URL(destUrl);
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
