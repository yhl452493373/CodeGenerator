package com.h3w.shiro;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.h3w.bean.CaptchaProperties;
import com.h3w.utils.CaptchaUtils;
import com.h3w.utils.CommonUtils;
import com.h3w.utils.CookieUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.MapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro调用生成验证码、比较输入的验证码的工具类
 */
@SuppressWarnings("unused")
public class ShiroCaptcha implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String cacheName;
    private String cookieName;
    private Integer cookieMaxAge;
    private Color captchaBackgroundColor;
    private Integer captchaSize;
    private Integer captchaLineCount;
    private Integer captchaPointCount;
    private Integer captchaWidth;
    private Integer captchaHeight;
    private Integer captchaFontSize;
    private Boolean captchaHasBorder;
    private Boolean captchaIsColorful;

    private Cache<String, String> captchaCache;

    public ShiroCaptcha() {
        this.cacheName = CaptchaProperties.DEFAULT_CACHE_NAME;
        this.cookieName = CaptchaProperties.DEFAULT_COOKIE_NAME;
        this.cookieMaxAge = CaptchaProperties.DEFAULT_COOKIE_MAX_AGE;
        this.captchaBackgroundColor = CaptchaProperties.DEFAULT_CAPTCHA_BACKGROUND_COLOR;
        this.captchaSize = CaptchaProperties.DEFAULT_CAPTCHA_SIZE;
        this.captchaLineCount = CaptchaProperties.DEFAULT_CAPTCHA_LINE_COUNT;
        this.captchaPointCount = CaptchaProperties.DEFAULT_CAPTCHA_POINT_COUNT;
        this.captchaWidth = CaptchaProperties.DEFAULT_CAPTCHA_WIDTH;
        this.captchaHeight = CaptchaProperties.DEFAULT_CAPTCHA_HEIGHT;
        this.captchaFontSize = CaptchaProperties.DEFAULT_CAPTCHA_FONT_SIZE;
        this.captchaHasBorder = CaptchaProperties.DEFAULT_CAPTCHA_HAS_BORDER;
        this.captchaIsColorful = CaptchaProperties.DEFAULT_CAPTCHA_IS_COLORFUL;
        captchaCache = new MapCache<>(cacheName, new HashMap<>());
    }

    public ShiroCaptcha(CaptchaProperties captchaProperties) {
        this.cacheName = captchaProperties.getCacheName();
        this.cookieName = captchaProperties.getCookieName();
        this.cookieMaxAge = captchaProperties.getCookieMaxAge();
        this.captchaBackgroundColor = captchaProperties.getCaptchaBackgroundColor();
        this.captchaSize = captchaProperties.getCaptchaSize();
        this.captchaLineCount = captchaProperties.getCaptchaLineCount();
        this.captchaPointCount = captchaProperties.getCaptchaPointCount();
        this.captchaWidth = captchaProperties.getCaptchaWidth();
        this.captchaHeight = captchaProperties.getCaptchaHeight();
        this.captchaFontSize = captchaProperties.getCaptchaFontSize();
        this.captchaHasBorder = captchaProperties.getCaptchaHasBorder();
        this.captchaIsColorful = captchaProperties.getCaptchaIsColorful();
        captchaCache = new MapCache<>(cacheName, new HashMap<>());
    }

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(cacheName, "cacheName must not be empty!");
        Assert.hasText(cookieName, "cookieName must not be empty!");
        Assert.notNull(cookieMaxAge, "cookieMaxAge must not be null!");
        Assert.notNull(captchaBackgroundColor, "captchaBackgroundColor must not be null!");
        Assert.notNull(captchaSize, "captchaSize must not be null!");
        Assert.notNull(captchaLineCount, "captchaLineCount must not be null!");
        Assert.notNull(captchaPointCount, "captchaPointCount must not be null!");
        Assert.notNull(captchaWidth, "captchaWidth must not be null!");
        Assert.notNull(captchaHeight, "captchaHeight must not be null!");
        Assert.notNull(captchaFontSize, "captchaFontSize must not be null!");
        Assert.notNull(captchaHasBorder, "captchaHasBorder must not be null!");
        Assert.notNull(captchaIsColorful, "captchaIsColorful must not be null!");
    }

    /**
     * 生成验证码兵返回到客户端
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    public void generate(HttpServletRequest request, HttpServletResponse response) {
        //首先检查cookie的uuid是否存在
        String cookieValue = CookieUtils.getCookieValue(request, cookieName);
        boolean hasCookie = true;
        if (StringUtils.isEmpty(cookieValue)) {
            hasCookie = false;
            cookieValue = CommonUtils.uuid();
        }
        //生成验证码，包括图像和字符串
        Map<String, Object> captchaMap = CaptchaUtils.getCaptchaImage(captchaBackgroundColor, captchaSize, captchaWidth, captchaHeight, captchaFontSize, captchaLineCount, captchaPointCount, captchaHasBorder, captchaIsColorful, CaptchaUtils.ComplexLevel.HARD);
        //不存在cookie时设置cookie
        if (!hasCookie) {
            CookieUtils.setCookie(request, response, cookieName, cookieValue, cookieMaxAge);
        }
        CaptchaUtils.writeToResponse(response, (BufferedImage) captchaMap.get(CaptchaUtils.CAPTCHA_IMAGE_STREAM));
        captchaCache.put(cookieValue, (String) captchaMap.get(CaptchaUtils.CAPTCHA_STRING));
    }

    /**
     * 仅能验证一次，验证后立即删除
     *
     * @param request          HttpServletRequest
     * @param response         HttpServletResponse
     * @param userInputCaptcha 用户输入的验证码
     * @return 验证通过返回 true, 否则返回 false
     */
    public boolean validate(HttpServletRequest request, HttpServletResponse response, String userInputCaptcha) {
        if (logger.isDebugEnabled()) {
            logger.info("validate captcha userInputCaptcha is " + userInputCaptcha);
        }
        String cookieValue = CookieUtils.getCookieValue(request, cookieName);
        if (StringUtils.isEmpty(cookieValue)) {
            return false;
        }
        String captchaCode = captchaCache.get(cookieValue);
        if (StringUtils.isEmpty(captchaCode)) {
            return false;
        }
        // 转成大写，重要
        userInputCaptcha = userInputCaptcha.toUpperCase();
        boolean result = userInputCaptcha.equals(captchaCode.toUpperCase());
        if (result) {
            captchaCache.remove(cookieValue);
            CookieUtils.deleteCookie(request, response, cookieName);
        }
        return result;
    }
}
