package android.text;


/**
 * @author bruce.zhang
 * @date 2021/12/24 10:59
 * @description (亲 ， 我是做什么的)
 * <p>
 * modification history:
 */
public class TextUtils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
