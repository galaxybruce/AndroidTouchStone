package com.galaxybruce.component.util.cache

import android.content.Context
import androidx.room.*
import com.galaxybruce.component.app.BaseApplication
import com.galaxybruce.component.proguard.IProguardKeeper

/**
 * @date 2021/11/25 13:47
 * @author bruce.zhang
 * @description 大数据存储db
 *
 * 遇到的坑：
 * 1. org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21版本要在1.4.1以上
 * 2. java环境变量路径要用android studio内嵌的/Applications/Android\ Studio.app/Contents/jre/jdk/Contents/Home
 *
 * modification history:
 */
@Database(version = 1, entities = [AppKVDbEntity::class])
abstract class AppKVCacheDb : RoomDatabase() {
    abstract fun appKVDbDao(): AppKVDbDao

    companion object {
        @Volatile
        private var INSTANCE: AppKVCacheDb? = null

        fun get(): AppKVCacheDb = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase().also { INSTANCE = it }
        }

        private fun buildDatabase(): AppKVCacheDb {
            val context: Context = BaseApplication.instance
            return Room.databaseBuilder(
                context.applicationContext,
                AppKVCacheDb::class.java,
                "kv_cache.db")
                .enableMultiInstanceInvalidation()
                .build()
        }
    }
}

@Entity(tableName = "kv_cache")
data class AppKVDbEntity(
    @PrimaryKey @ColumnInfo(defaultValue = "", name = "cacheKey") val cacheKey: String,
    @ColumnInfo(name = "cacheValue") val cacheValue: String?,
    @ColumnInfo(defaultValue = "", name = "cacheFileName") val cacheFileName: String?, // 区分业务
    @ColumnInfo(defaultValue = "", name = "cacheAccount") val cacheAccount: String? // 区分账号
) : IProguardKeeper

/**
 * 异步：
 * [Room和RxJava](https://blog.csdn.net/feather_wch/article/details/88762443#Insert_27)
 * [Android Room + RxJava 查询记录不存在的处理方法](https://blog.nex3z.com/2017/10/31/android-room-rxjava-%E6%9F%A5%E8%AF%A2%E8%AE%B0%E5%BD%95%E4%B8%8D%E5%AD%98%E5%9C%A8%E7%9A%84%E5%A4%84%E7%90%86%E6%96%B9%E6%B3%95/)
 */
@Dao
interface AppKVDbDao {
    @Query("SELECT * FROM kv_cache WHERE cacheAccount = :account")
    fun getAll(account: String): List<AppKVDbEntity>

    @Query("SELECT * FROM kv_cache WHERE cacheFileName = :fileName AND cacheAccount = :account")
    fun getAllByFileName(fileName: String, account: String): List<AppKVDbEntity>

    @Query("SELECT cacheValue FROM kv_cache WHERE cacheKey = :cacheKey AND cacheFileName = :fileName AND cacheAccount = :account")
    fun getValue(fileName: String, cacheKey: String, account: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setValue(cache: AppKVDbEntity): Long

    @Query("DELETE FROM kv_cache WHERE cacheKey = :cacheKey AND cacheFileName = :fileName AND cacheAccount = :account")
    fun remove(fileName: String, cacheKey: String, account: String): Int

    @Query("DELETE FROM kv_cache WHERE cacheFileName = :fileName AND cacheAccount = :account")
    fun removeByFileName(fileName: String, account: String): Int

    @Query("SELECT COUNT(cacheKey) FROM kv_cache WHERE cacheKey = :cacheKey AND cacheFileName = :fileName AND cacheAccount = :account")
    fun contains(fileName: String, cacheKey: String, account: String): Int

}
