package com.github.yhl452493373.bean;

import com.github.yhl452493373.shiro.ShiroCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class CaptchaProperties {
    public static Logger logger = LoggerFactory.getLogger(ShiroCaptcha.class);
    /**
     * 默认验证码的cookie名称
     */
    public static String DEFAULT_COOKIE_NAME = "captcha";
    /**
     * 默认验证码的cookie的缓存名称
     */
    public static String DEFAULT_CACHE_NAME = "captcha-cache";
    /**
     * 默认验证码的cooke的有效时间
     */
    public static Integer DEFAULT_COOKIE_MAX_AGE = -1;
    /**
     * 默认验证码的背景色
     */
    public static Color DEFAULT_CAPTCHA_BACKGROUND_COLOR = Color.WHITE;
    /**
     * 默认验证码位数
     */
    public static Integer DEFAULT_CAPTCHA_SIZE = 4;
    /**
     * 默认验证码干扰线数量
     */
    public static Integer DEFAULT_CAPTCHA_LINE_COUNT = 10;
    /**
     * 默认验证码干扰点数量
     */
    public static Integer DEFAULT_CAPTCHA_POINT_COUNT = 10;
    /**
     * 默认验证码图片宽度
     */
    public static Integer DEFAULT_CAPTCHA_WIDTH = 80;
    /**
     * 默认验证码图片高度
     */
    public static Integer DEFAULT_CAPTCHA_HEIGHT = 40;
    /**
     * 默认验证码图片字号
     */
    public static Integer DEFAULT_CAPTCHA_FONT_SIZE = 20;
    /**
     * 默认验证图片是否有边框
     */
    public static Boolean DEFAULT_CAPTCHA_HAS_BORDER = false;
    /**
     * 默认验证图片是否为彩色
     */
    public static Boolean DEFAULT_CAPTCHA_IS_COLORFUL = true;

    private String cacheName = DEFAULT_CACHE_NAME;
    private String cookieName = DEFAULT_COOKIE_NAME;
    private Integer cookieMaxAge = DEFAULT_COOKIE_MAX_AGE;
    private Color captchaBackgroundColor = DEFAULT_CAPTCHA_BACKGROUND_COLOR;
    private Integer captchaSize = DEFAULT_CAPTCHA_SIZE;
    private Integer captchaLineCount = DEFAULT_CAPTCHA_LINE_COUNT;
    private Integer captchaPointCount = DEFAULT_CAPTCHA_POINT_COUNT;
    private Integer captchaWidth = DEFAULT_CAPTCHA_WIDTH;
    private Integer captchaHeight = DEFAULT_CAPTCHA_HEIGHT;
    private Integer captchaFontSize = DEFAULT_CAPTCHA_FONT_SIZE;
    private Boolean captchaHasBorder = DEFAULT_CAPTCHA_HAS_BORDER;
    private Boolean captchaIsColorful = DEFAULT_CAPTCHA_IS_COLORFUL;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public Color getCaptchaBackgroundColor() {
        return captchaBackgroundColor;
    }

    public void setCaptchaBackgroundColor(Color captchaBackgroundColor) {
        this.captchaBackgroundColor = captchaBackgroundColor;
    }

    public Integer getCaptchaSize() {
        return captchaSize;
    }

    public void setCaptchaSize(Integer captchaSize) {
        this.captchaSize = captchaSize;
    }

    public Integer getCaptchaLineCount() {
        return captchaLineCount;
    }

    public void setCaptchaLineCount(Integer captchaLineCount) {
        this.captchaLineCount = captchaLineCount;
    }

    public Integer getCaptchaPointCount() {
        return captchaPointCount;
    }

    public void setCaptchaPointCount(Integer captchaPointCount) {
        this.captchaPointCount = captchaPointCount;
    }

    public Integer getCaptchaWidth() {
        return captchaWidth;
    }

    public void setCaptchaWidth(Integer captchaWidth) {
        this.captchaWidth = captchaWidth;
    }

    public Integer getCaptchaHeight() {
        return captchaHeight;
    }

    public void setCaptchaHeight(Integer captchaHeight) {
        this.captchaHeight = captchaHeight;
    }

    public Integer getCaptchaFontSize() {
        return captchaFontSize;
    }

    public void setCaptchaFontSize(Integer captchaFontSize) {
        this.captchaFontSize = captchaFontSize;
    }

    public Boolean getCaptchaHasBorder() {
        return captchaHasBorder;
    }

    public void setCaptchaHasBorder(Boolean captchaHasBorder) {
        this.captchaHasBorder = captchaHasBorder;
    }

    public Boolean getCaptchaIsColorful() {
        return captchaIsColorful;
    }

    public void setCaptchaIsColorful(Boolean captchaIsColorful) {
        this.captchaIsColorful = captchaIsColorful;
    }
}
