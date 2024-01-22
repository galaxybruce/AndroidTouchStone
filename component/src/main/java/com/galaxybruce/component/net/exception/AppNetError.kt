package com.galaxybruce.component.net.exception

/**
 * @date 2023/6/21 13:53
 * @author bruce.zhang
 * @description 网络请求异常Code
 *
 * modification history:
 */
enum class AppNetError(val code: String, val msg: String) {

    /**
     * 未知错误
     */
    ERROR_UNKNOWN("-1000", "请求失败，请稍后再试"),
    /**
     * 解析数据失败
     */
    ERROR_PARSE("-1001", "错误的数据格式"),
    /**
     * 网络错误
     */
    ERROR_BAD_NETWORK("-1002", "网络异常，请检查网络状态"),
    /**
     * 连接错误
     */
    ERROR_CONNECT("-1003", "网络连接异常，请检查网络状态"),
    /**
     * 证书出错
     */
    ERROR_SSL("-1004", "证书出错，请稍后再试"),
    /**
     * 连接超时
     */
    ERROR_CONNECT_TIMEOUT("-1005", "网络连接超时，请检查网络状态，请稍后重试");
}