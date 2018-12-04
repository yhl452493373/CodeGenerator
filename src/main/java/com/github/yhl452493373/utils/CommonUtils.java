package com.github.yhl452493373.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类，常用的方法写到这里
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CommonUtils {
    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 生成min到max之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 生成的随机数
     */
    public static Integer random(Integer min, Integer max) {
        return random(min, max, null);
    }

    /**
     * 生成min到max之间的随机数
     *
     * @param min         最小值
     * @param max         最大值
     * @param excludeList 要排除的值
     * @return 生成的随机数
     */
    public static Integer random(Integer min, Integer max, List<Integer> excludeList) {
        Random random = new Random();
        Integer randomNumber = random.nextInt(max) % (max - min + 1) + min;
        if (excludeList != null && excludeList.contains(randomNumber)) {
            return random(min, max, excludeList);
        }
        return randomNumber;
    }

    /**
     * 生成一个指定位数的盐字符串，仅包含字母
     *
     * @param size 盐长度
     * @return 生成的盐字符串
     */
    public static String salt(Integer size) {
        List<Integer> excludeList = Arrays.asList(91, 92, 93, 94, 95, 96);
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < size; i++) {
            salt.append((char) (int) random(65, 122, excludeList));
        }
        return salt.toString();
    }

    /**
     * 将明文密码进行md5加密
     *
     * @param inputPassword 输入的明文密码
     * @param salt          加密时用的盐
     * @param hashNumber    加密次数
     * @return 加密后的密码
     */
    public static String hashPassword(String inputPassword, String salt, Integer hashNumber) {
        return new Md5Hash(inputPassword, salt, hashNumber).toString();
    }

    /**
     * MD5加密
     *
     * @param message 要进行MD5加密的字符串
     * @return 加密结果为32位字符串
     */
    public static String getMD5(String message) {
        MessageDigest messageDigest;
        StringBuilder md5StrBuff = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(message.getBytes(StandardCharsets.UTF_8));

            byte[] byteArray = messageDigest.digest();
            for (byte aByteArray : byteArray) {
                if (Integer.toHexString(0xFF & aByteArray).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & aByteArray));
                else md5StrBuff.append(Integer.toHexString(0xFF & aByteArray));
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return md5StrBuff.toString().toUpperCase();// 字母大写
    }

    /**
     * 验证密码是否正确
     *
     * @param inputPassword 输入的明文密码
     * @param dbPassword    数据库中的加密后的密码
     * @param salt          数据库中的盐
     * @param hasNumber     数据库中的加密次数
     * @return 密码是否正确
     */
    public static boolean validatePassword(String inputPassword, String dbPassword, String salt, Integer hasNumber) {
        return new Md5Hash(inputPassword, salt, hasNumber).toString().equals(dbPassword);
    }

    /**
     * 生成Hibernate风格的32位UUID
     *
     * @return 生成的uuid
     */
    public static String uuid() {
        return UUIDGenerator.generate().toString();
    }

    public static <T> String convertToIdString(Iterable<T> list) {
        List<String> idList = convertToIdList(list);
        return String.join(",", idList);
    }

    /**
     * 将对象列表转化为对象ID列表。列表对象的ID字段需要为字符串
     *
     * @param list 对象
     * @param <T>  对象类型
     * @return id列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<String> convertToIdList(Iterable<T> list) {
        List<String> idList = new ArrayList<>();
        if (list == null) return idList;
        for (T t : list) {
            Class clazz = t.getClass();
            String id = (String) executeMethod(t, clazz, "getId", null, null);
            idList.add(id);
        }
        return idList;
    }

    /**
     * 将对象列表转化为对象某字段值列表。列表对象的字段需要为字符串
     *
     * @param list       对象
     * @param <T>        对象类型
     * @param methodName 字段获取方法
     * @return 字段值列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<String> convertToFieldList(Iterable<T> list, String methodName) {
        List<String> idList = new ArrayList<>();
        if (list == null || !list.iterator().hasNext()) {
            return new ArrayList<>();
        }
        for (T t : list) {
            Class clazz = t.getClass();
            String id = (String) executeMethod(t, clazz, methodName, null, null);
            idList.add(id);
        }
        return idList;
    }

    /**
     * 执行某个类的某个方法
     *
     * @param t              类的实例
     * @param clazz          类
     * @param methodName     方法
     * @param parameterTypes 获取方法名时的条件:参数类型,可以为null
     * @param args           执行方法时的参数,可以为null
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public static <T> Object executeMethod(T t, Class clazz, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method;
        Object result = null;
        try {
            if (parameterTypes == null || parameterTypes.length == 0)
                method = clazz.getMethod(methodName);
            else
                method = clazz.getMethod(methodName, parameterTypes);
            if (args == null || args.length == 0)
                result = method.invoke(t);
            else
                result = method.invoke(t, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Class supperClazz = clazz.getSuperclass();
            if (supperClazz != null)
                executeMethod(t, supperClazz, methodName, parameterTypes, args);
            else
                e.printStackTrace();
        }
        return result;
    }

    /**
     * 是否是ajax请求
     */
    public static boolean isAjaxRequest(ServletRequest request) {
        HttpServletRequest servletRequest = WebUtils.toHttp(request);
        return (servletRequest.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(servletRequest.getHeader("X-Requested-With")));
    }

    /**
     * create date:2010-5-22下午03:32:31 描述：将源字符串中的阿拉伯数字格式化为汉字
     */
    public static char formatDigit(char sign) {
        if (sign == '0') {
            sign = '〇';
        }
        if (sign == '1') {
            sign = '一';
        }
        if (sign == '2') {
            sign = '二';
        }
        if (sign == '3') {
            sign = '三';
        }
        if (sign == '4') {
            sign = '四';
        }
        if (sign == '5') {
            sign = '五';
        }
        if (sign == '6') {
            sign = '六';
        }
        if (sign == '7') {
            sign = '七';
        }
        if (sign == '8') {
            sign = '八';
        }
        if (sign == '9') {
            sign = '九';
        }
        return sign;
    }

    /**
     * 分隔多个id字符串为id列表
     *
     * @param ids 多个id字符串
     * @return id列表
     */
    public static List<String> splitIds(String ids) {
        if (StringUtils.isEmpty(ids)) return new ArrayList<>();
        return splitIds(ids, ",");
    }

    /**
     * 分隔多个id字符串为id列表
     *
     * @param ids      多个id字符串
     * @param splitter 分隔符
     * @return id列表
     */
    public static List<String> splitIds(String ids, String splitter) {
        if (StringUtils.isEmpty(ids)) return new ArrayList<>();
        String[] idArray = ids.split(splitter);
        return new ArrayList<>(Arrays.asList(idArray));
    }

    /**
     * 将路径对应的图片转为base64字符串
     *
     * @param imgPath 图片路径
     */
    public static String imageToBase64(String imgPath) {
        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgPath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 日期转时间表达式
     *
     * @param timeString 日期字符串
     * @return 转换结果(cron表达式字符串或者null)
     */
    public static String convertDateToCron(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String[] cronArray = new String[]{"*", "*", "*", "*", "*", "?", "*"};
        cronArray[0] = String.valueOf(calendar.get(Calendar.SECOND));
        cronArray[1] = String.valueOf(calendar.get(Calendar.MINUTE));
        cronArray[2] = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        cronArray[3] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        cronArray[4] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        cronArray[6] = String.valueOf(calendar.get(Calendar.YEAR));
        return String.join(" ", cronArray);
    }

    /**
     * 比较传入的日期是否大于当前时间
     *
     * @param timeString 日期字符串
     * @return true-有效,false-无效
     */
    public static Boolean compareTimeIsValid(String timeString) {
        Calendar currentCalendar = Calendar.getInstance();
        Calendar nextCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        nextCalendar.setTime(date);
        int result = nextCalendar.compareTo(currentCalendar);
        return result > 0;
    }

    /**
     * 根据时间戳计算从时间戳开始偏移X天的日期区间
     *
     * @param timestamp 日期时间戳
     * @param offsetDay 需要偏移的天数，向前偏移用负数，向后偏移用正数
     * @return 日期区间，start-开始区间，为往前偏移X的当天或往后偏移的时间戳当天的0时0分0秒，end-结束区间，为日期时间戳当天加1或往后偏移X的日期+1日的0时0分0秒，查询时应该用小于结束时间
     */
    public static Map<String, Date> getDateSection(Timestamp timestamp, Integer offsetDay) {
        Map<String, Date> sectionMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        CalendarUtils.INSTANCE.setTimeToDayStart(calendar);
        if (offsetDay == 0) return getDateSection(timestamp);
        if (offsetDay > 0) {
            sectionMap.put("start", calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, offsetDay + 1);
            sectionMap.put("end", calendar.getTime());
        } else {
            Calendar endCalendar = (Calendar) calendar.clone();
            calendar.add(Calendar.DAY_OF_MONTH, offsetDay);
            sectionMap.put("start", calendar.getTime());
            endCalendar.add(Calendar.DAY_OF_MONTH, 1);
            sectionMap.put("end", endCalendar.getTime());
        }

        return sectionMap;
    }

    /**
     * 根据时间戳计算时间戳当天日期格式区间
     *
     * @param timestamp 日期时间戳
     * @return 日期区间，start-开始区间，为当天的0时0分0秒，end-结束区间，为第二天的0时0分0秒，查询时应该用小于结束时间
     */
    public static Map<String, Date> getDateSection(Timestamp timestamp) {
        Map<String, Date> sectionMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        sectionMap.put("start", calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        sectionMap.put("end", calendar.getTime());
        return sectionMap;
    }

    /**
     * 根据时间戳计算从开始时间偏移X月的月份区间
     *
     * @param timestamp   开始时间
     * @param offsetMonth 需要偏移的月份
     * @return 月份期间, start-开始区间,为当前时间向前或向后推x月后的月份的第一天,end-结束区间,为当前时间向前或向后推x月后的月份的最后一天
     */
    public static Map<String, Date> getMonthSection(Timestamp timestamp, Integer offsetMonth) {
        Map<String, Date> sectionMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        CalendarUtils.INSTANCE.setDateToMonthStart(calendar);
        if (offsetMonth == 0) {
            sectionMap.put("start", calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            sectionMap.put("end", calendar.getTime());
        } else if (offsetMonth > 0) {
            sectionMap.put("start", calendar.getTime());
            calendar.add(Calendar.MONTH, offsetMonth + 1);
            sectionMap.put("end", calendar.getTime());
        } else {
            Calendar endCalendar = (Calendar) calendar.clone();
            endCalendar.add(Calendar.MONTH, 1);
            sectionMap.put("end", endCalendar.getTime());
            calendar.add(Calendar.MONTH, offsetMonth);
            sectionMap.put("start", calendar.getTime());
        }

        return sectionMap;
    }

    /**
     * 根据开始日,结束日期计算同比天数前的日期区间
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 时间区间{start:开始日期,end:结束日期}，使用时需要start<=x<end
     */
    public static Map<String, String> getPrevDateSection(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startCalendar = CalendarUtils.INSTANCE.parseToCalendar(start, dateFormat, datetimeFormat);
        Calendar endCalendar = CalendarUtils.INSTANCE.parseToCalendar(end, dateFormat, datetimeFormat);
        int offset = CalendarUtils.INSTANCE.calculateDaysBetween(startCalendar, endCalendar);
        startCalendar.add(Calendar.DAY_OF_MONTH, offset);
        endCalendar.add(Calendar.DAY_OF_MONTH, offset);
        Map<String, String> temp = new HashMap<>();
        temp.put("start", datetimeFormat.format(startCalendar.getTime()));
        temp.put("end", datetimeFormat.format(endCalendar.getTime()));
        return temp;
    }

    /**
     * 根据开始日,结束日期计算开始日期前一个月到结束日期之间的日期区间.如果结束日期不在开始日期月份中,则结束日期为改月最后一天往后推一天
     *
     * @param start 开始日期
     * @param end   结束日期
     * @return 时间区间{start:开始日期,end:结束日期}，使用时需要start<=x<end
     */
    public static Map<String, Date> getPrevMonthDateSection(Date start, Date end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        endCalendar.setTime(end);

        //将日期设置到开始日期的月份的1日的0时0分0秒
        startCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) - 1);
        CalendarUtils.INSTANCE.setDateToMonthStart(startCalendar);

        //若结束日期和开始日期不在同一月,则将结束日期设置为上一月最后一日
        if (endCalendar.get(Calendar.MONTH) != startCalendar.get(Calendar.MONTH)) {
            endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH));
            endCalendar.set(Calendar.DAY_OF_MONTH, startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        endCalendar.add(Calendar.DAY_OF_MONTH, 1);

        Map<String, Date> temp = new HashMap<>();
        temp.put("start", startCalendar.getTime());
        temp.put("end", endCalendar.getTime());
        return temp;
    }

    /**
     * 获取字符串中连续的整数部分
     *
     * @param str 字符串
     * @return 整数, 没有则为null
     */
    public static Integer getInteger(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        boolean match = matcher.find();
        if (match) return Integer.valueOf(matcher.group());
        return null;
    }

    /**
     * 内部类，用于生成Hibernate风格的UUID
     */
    static class UUIDGenerator {
        private static final int JVM = (int) (System.currentTimeMillis() >>> 8);
        private final static String sep = "";
        private static int IP;
        private static short counter = (short) 0;

        static {
            int ipadd;
            try {
                ipadd = IptoInt(InetAddress.getLocalHost().getAddress());
            } catch (Exception e) {
                ipadd = 0;
            }
            IP = ipadd;
        }

        private static int IptoInt(byte[] bytes) {
            int result = 0;
            for (int i = 0; i < 4; i++) {
                result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
            }
            return result;
        }

        public static Serializable generate() {
            return format(getIP()) + sep + format(getJVM()) + sep + format(getHiTime()) + sep + format(getLoTime()) + sep + format(getCount());
        }

        private static String format(int intval) {
            String formatted = Integer.toHexString(intval);
            StringBuilder buf = new StringBuilder("00000000");
            buf.replace(8 - formatted.length(), 8, formatted);
            return buf.toString();
        }

        /**
         * Unique in a local network
         */
        private static int getIP() {
            return IP;
        }

        /**
         * Unique across JVMs on this machine (unless they load this class
         * in the same quater second - very unlikely)
         */
        private static int getJVM() {
            return JVM;
        }

        private static String format(short shortval) {
            String formatted = Integer.toHexString(shortval);
            StringBuilder buf = new StringBuilder("0000");
            buf.replace(4 - formatted.length(), 4, formatted);
            return buf.toString();
        }

        /**
         * Unique down to millisecond
         */
        private static short getHiTime() {
            return (short) (System.currentTimeMillis() >>> 32);
        }

        private static int getLoTime() {
            return (int) System.currentTimeMillis();
        }

        /**
         * Unique in a millisecond for this JVM instance (unless there
         * are > Short.MAX_VALUE instances created in a millisecond)
         */
        private static short getCount() {
            synchronized (UUIDGenerator.class) {
                if (counter < 0) counter = 0;
                return counter++;
            }
        }
    }
}
