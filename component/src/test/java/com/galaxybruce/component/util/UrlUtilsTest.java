package com.galaxybruce.component.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author bruce.zhang
 * @date 2021/12/24 10:39
 * @description (亲 ， 我是做什么的)
 * <p>
 * modification history:
 */
public class UrlUtilsTest {

    @Test
    public void getUriParamValue1() {
        String url = "https://www.google.com?a=b";
        String value = AppURLUtil.getUriParamValue(url, "a");
        assertEquals("b", value);
    }

}