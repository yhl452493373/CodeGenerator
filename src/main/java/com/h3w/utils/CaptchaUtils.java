package com.h3w.utils;

import com.baomidou.mybatisplus.core.toolkit.IOUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 生成验证码的工具类
 *
 * @author 252878950@qq.com
 */
public class CaptchaUtils {

    public static final String CAPTCHA_IMAGE_STREAM = "captchaImageStream";
    public static final String CAPTCHA_STRING = "captchaString";
    private static final char[] SIMPLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final char[] MEDIUM = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Z', 'Y', 'Z'};
    private static final char[] HARD = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Z', 'Y', 'Z'};
    private static Random random = new Random();

    /**
     * 验证码生产类
     * 简单的验证码由仅数字组成
     * 中等复杂的验证码由字母（包括大小写）和数字组成，有干扰点
     * 最复杂的验证码由由字母（包括大小写）和数字组成，有干扰点和干扰线
     * 可自定义干扰点和干扰线的数量
     *
     * @param size         验证码位数
     * @param width        验证码图片宽度
     * @param height       验证码图片高度
     * @param fontSize     字体大小
     * @param lineCount    干扰线条数 仅对复杂验证码模式有效
     * @param pointCount   干扰点 对复杂验证码和中等复杂度验证码有效
     * @param colours      是否彩色字体
     * @param border       是否绘制边框
     * @param complexLevel 复杂度枚举类型，如传入ComplexLevel.SIMPLE
     * @return Map 键值对
     * Map.get(CaptchaUtils.CAPTCHA_IMAGE_STREAM)是BufferImage图像信息，可以通过ImageIO.write((BufferImage)Map.get(CaptchaUtils.CAPTCHA_IMAGE_STREAM),"JPEG",HttpResponse.getOutputStream())写到客户端
     * Map.get(CaptchaUtils.CAPTCHA_STRING)是验证码字符串
     */
    public static Map<String, Object> getCaptchaImage(Color backgroundColor, int size, int width, int height, int fontSize, int lineCount, int pointCount, boolean border, boolean colours, ComplexLevel complexLevel) {
        Map<String, Object> map = new HashMap<>();
        //验证码字符串
        StringBuilder stringBuilder = new StringBuilder();
        //从字符数组中去字符的随机位置
        int position;
        int tempFontSize = width / size;

        tempFontSize = tempFontSize > fontSize ? fontSize : tempFontSize;

        //图像数据
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取一张图像
        Graphics graphics = bufferedImage.getGraphics();
        //设置背景色
        graphics.setColor(backgroundColor);
        //绘制一个矩形
        graphics.fillRect(0, 0, width, height);
        //设置字体
        graphics.setFont(new Font("Arial", Font.BOLD, tempFontSize));
        //设置字体颜色
        graphics.setColor(Color.BLACK);
        //绘制一个边框
        if (border) graphics.drawRect(0, 0, width - 1, height - 1);
        switch (complexLevel) {
            case SIMPLE:
                for (int i = 0; i < size; i++) {
                    //设置随机颜色
                    if (colours)
                        graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    position = random.nextInt(SIMPLE.length);
                    //写字符到图片合适的位置
                    graphics.drawString(SIMPLE[position] + "", random.nextInt(tempFontSize / 2) + (i * tempFontSize  + random.nextInt(tempFontSize / 4)), height / 2 + random.nextInt(height / 2));
                    stringBuilder.append(SIMPLE[position]);
                }
                break;
            case MEDIUM:
                //绘制干扰点
                for (int k = 0; k < pointCount; k++) {
                    graphics.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
                }
                for (int i = 0; i < size; i++) {
                    //设置随机颜色
                    if (colours)
                        graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    position = random.nextInt(MEDIUM.length);
                    //写字符到图片合适的位置
                    graphics.drawString(MEDIUM[position] + "", random.nextInt(tempFontSize / 2) + (i * tempFontSize  + random.nextInt(tempFontSize / 4)), height / 2 + random.nextInt(height / 2));
                    stringBuilder.append(MEDIUM[position]);
                }
                break;
            case HARD:
                //绘制干扰线
                for (int j = 0; j < lineCount; j++) {
                    graphics.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
                }
                //绘制干扰点
                for (int k = 0; k < pointCount; k++) {
                    graphics.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
                }
                for (int i = 0; i < size; i++) {
                    //设置随机颜色
                    if (colours)
                        graphics.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    position = random.nextInt(HARD.length);
                    //写字符到图片合适的位置
                    graphics.drawString(HARD[position] + "", random.nextInt(tempFontSize / 2) + (i * tempFontSize  + random.nextInt(tempFontSize / 4)), height / 2 + random.nextInt(height / 2));
                    stringBuilder.append(HARD[position]);
                }
                break;
            default:
                break;
        }
        //释放Graphics对象
        graphics.dispose();

        map.put(CAPTCHA_IMAGE_STREAM, bufferedImage);
        map.put(CAPTCHA_STRING, stringBuilder.toString());
        return map;
    }

    public static void writeToResponse(HttpServletResponse response, BufferedImage captchaImage) {
        response.setHeader("Pragma", "no-redis");
        response.setHeader("Cache-Control", "no-redis");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            ImageIO.write(captchaImage, "PNG", servletOutputStream);
            servletOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(servletOutputStream);
        }
    }

    /**
     * 验证码复杂等级
     * ComplexLevel.SIMPLE 简单，由0-9的数字组成
     * ComplexLevel.MEDIUM 中等，由a-zA-Z字母组成
     * ComplexLevel.HARD 复杂，由0-9a-zA-Z数字和字母组成
     */
    public enum ComplexLevel {
        SIMPLE, MEDIUM, HARD
    }

    /**
     * 测试代码
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = getCaptchaImage(Color.CYAN, 4, 150, 50, 35, 50, 500, true, true, ComplexLevel.MEDIUM);
            System.out.println("验证码" + map.get(CaptchaUtils.CAPTCHA_STRING));
            new File("D:\\").mkdir();
            ImageIO.write((BufferedImage) map.get(CaptchaUtils.CAPTCHA_IMAGE_STREAM), "jpg", new File("D:\\test\\" + System.currentTimeMillis() + ".jpg"));
            Thread.sleep(500L);
        }
    }
}