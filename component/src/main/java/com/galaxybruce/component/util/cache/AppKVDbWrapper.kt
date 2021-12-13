package com.galaxybruce.component.util.cache

import com.galaxybruce.component.util.AppConstants.EMPTY_STR


/**
 * @date 2021/11/25 14:59
 * @author bruce.zhang
 * @description 大数据k-v缓存，对DB的包装。主要是对参数走校验以及对返回结果处理
 *
 * modification history:
 */
internal object AppKVDbWrapper {

    fun putString(fileName: String?, key: String, value: String?, account: String?): Boolean {
        return key.takeIf { it.isNotBlank() }?.let {
            AppKVCacheDb.get().appKVDbDao().setValue(
                AppKVDbEntity(key, value, fileName ?: EMPTY_STR, account ?: EMPTY_STR)) > 0
        } ?: false
    }

    fun getString(fileName: String?, key: String, defValue: String? = null, account: String?): String? {
        return AppKVCacheDb.get().appKVDbDao().getValue(
            fileName ?: EMPTY_STR, key, account ?: EMPTY_STR) ?: defValue
    }

    fun getAll(account: String?): List<AppKVDbEntity>? {
        return AppKVCacheDb.get().appKVDbDao().getAll(account ?: EMPTY_STR)
    }

    fun getAllByFileName(fileName: String?, account: String?): List<AppKVDbEntity>? {
        return fileName.takeIf { !it.isNullOrBlank() }?.let {
            AppKVCacheDb.get().appKVDbDao().getAllByFileName(
                fileName ?: EMPTY_STR, account ?: EMPTY_STR)
        }
    }

    fun remove(fileName: String?, key: String, account: String?): Boolean {
        return key.takeIf { it.isNotBlank() }?.let {
            AppKVCacheDb.get().appKVDbDao().remove(
                fileName ?: EMPTY_STR, key, account ?: EMPTY_STR) > 0
        } ?: false
    }

    fun removeByFileName(fileName: String?, account: String?): Boolean {
        return fileName.takeIf { !it.isNullOrBlank() }?.let {
            AppKVCacheDb.get().appKVDbDao().removeByFileName(
                fileName ?: EMPTY_STR, account ?: EMPTY_STR) > 0
        } ?: false
    }

    fun contains(fileName: String?, key: String, account: String?): Boolean {
        return key.takeIf { it.isNotBlank() }?.let {
            AppKVCacheDb.get().appKVDbDao().contains(
                fileName ?: EMPTY_STR, key, account ?: EMPTY_STR) > 0
        } ?: false
    }

}