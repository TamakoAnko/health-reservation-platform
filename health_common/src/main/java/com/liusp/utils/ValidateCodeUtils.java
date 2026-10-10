package com.liusp.utils;

import java.util.Random;

/**
 * 验证码生成工具类
 */
public class ValidateCodeUtils {
    /**
     * 随机生成验证码
     * @param digits 验证码位数
     * @return 指定位数的随机数字验证码
     */
    public static Integer generateValidateCode(int digits) {
        Integer code = 0;
        if (digits == 4) {
            code = new Random().nextInt(9999);
        } else if (digits == 6) {
            code = new Random().nextInt(999999);
        } else {
            code = new Random().nextInt(99999999);
        }
        return code;
    }
}
