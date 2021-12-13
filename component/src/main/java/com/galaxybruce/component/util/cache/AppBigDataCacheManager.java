package com.galaxybruce.component.util.cache;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IAuthAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.galaxybruce.component.util.AppConstants.EMPTY_JSON;
import static com.galaxybruce.component.util.AppConstants.EMPTY_STR;


/**
 * @date 2021/5/14 17:41
 * @author bruce.zhang
 * @description 大数据存储
 *
 * 该类每个方法都提供了同步方法xxx和异步方法xxxAsync
 *
 * 注意：大对象不适合存在在SharedPreferences中，会导致内存很大。强烈建议使用该类存储。
 *
 * 存储优化:
 * 1. 文件存储使用：AppFileCacheManager
 * 2. 所有用到对象存储的业务使用：AppBigDataCacheManager
 * 3. 字段类型的小数据继续使用AppPreferencesUtil，不要直接使用AppSharedPreferencesCompat 和 SharedPreferences
 *
 * 参考文章：
 * 1. [请不要滥用SharedPreference](https://cloud.tencent.com/developer/article/1329495)
 * 2. [[Google] 再见 SharedPreferences 拥抱 Jetpack DataStore](https://juejin.cn/post/6881442312560803853)
 * 3. [这是一篇你应该了解的Android数据存储优化](https://zhuanlan.zhihu.com/p/352437849)
 * <p>
 * modification history:
 */
public class AppBigDataCacheManager {

    /**
     * 保存对象
     *
     * @param fileName 业务名称，用来分类存储，可以为null。 
     * @param object
     * @throws IOException
     */
    public static boolean saveCacheObject(String fileName, String key, Object object,
                                          boolean switchAccount) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        String strCache = null;
        try {
            strCache = JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return saveCacheString(fileName, key, strCache, switchAccount);
    }

    public static Observable<Boolean> saveCacheObjectAsyncObservable(String fileName, String key,
                                                                Object object, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(saveCacheObject(fileName, key, object, switchAccount));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public static void saveCacheObjectAsync(String fileName, String key,
                                                           Object object, boolean switchAccount) {
        saveCacheObjectAsyncObservable(fileName, key, object, switchAccount)
                .subscribe();
    }

    /**
     * 保存字符串
     *
     * @param fileName 业务名称，用来分类存储，可以为null。
     * @param data
     * @throws IOException
     */
    public static boolean saveCacheString(String fileName, String key, String data,
                                          boolean switchAccount) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.putString(fileName, key, data, account);
    }

    public static Observable<Boolean> saveCacheStringAsyncObservable(String fileName, String key,
                                                              String data, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(saveCacheString(fileName, key, data, switchAccount));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public static void saveCacheStringAsync(String fileName, String key,
                                                           String data, boolean switchAccount) {
        saveCacheStringAsyncObservable(fileName, key, data, switchAccount)
                .subscribe();
    }

    /**
     * 读取对象
     *
     * @param fileName 业务名称，用来分类存储，可以为null。
     * @return
     * @throws IOException
     */
    public static String loadCacheString(String fileName, String key,
                                         boolean switchAccount) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.getString(fileName, key, null, account);
    }

    public static Observable<String> loadCacheStringAsync(String fileName, String key, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                String result = loadCacheString(fileName, key, switchAccount);
                subscriber.onNext(result != null ? result : EMPTY_STR);
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 读取对象
     *
     * @param fileName 业务名称，用来分类存储，可以为null。
     * @return
     * @throws IOException
     */
    public static <T> T loadCacheObject(String fileName, String key, Class<T> clazz,
                                        boolean switchAccount) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String account = createAccount(switchAccount);
        String strData = AppKVDbWrapper.INSTANCE.getString(fileName, key, EMPTY_STR, account);
        if(!TextUtils.isEmpty(strData)) {
            try {
                return JSON.parseObject(strData, clazz);
            } catch (Exception e) {
                remove(fileName, key, switchAccount);
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param fileName
     * @param key
     * @param clazz
     * @param switchAccount
     * @param <T>
     * @return  由于Rxjava不允许返回null，所以用{@link com.galaxybruce.component.util.AppConstants#EMPTY_JSON}替代
     * 调用方需要校验这个值！！！
     */
    public static <T> Observable<T> loadCacheObjectAsync(String fileName, String key,
                                                             Class<T> clazz, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                T result = loadCacheObject(fileName, key, clazz, switchAccount);
                subscriber.onNext(result != null ? result : JSON.parseObject(EMPTY_JSON, clazz));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 读取某个业务的所有缓存
     *
     * @param fileName 业务名称，用来分类存储，可以为null。
     * @return
     * @throws IOException
     */
    public static List<AppKVDbEntity> loadAllCacheByFileName(String fileName, boolean switchAccount) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.getAllByFileName(fileName, account);
    }

    public static Observable<List<AppKVDbEntity>> loadAllCacheByFileNameAsync(String fileName, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<List<AppKVDbEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppKVDbEntity>> subscriber) throws Exception {
                List<AppKVDbEntity> list = loadAllCacheByFileName(fileName, switchAccount);
                subscriber.onNext(list != null ? list : new ArrayList<AppKVDbEntity>(1));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 读取所有缓存
     *
     * @return
     * @throws IOException
     */
    public static List<AppKVDbEntity> loadAllCache(boolean switchAccount) {
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.getAll(account);
    }

    public static Observable<List<AppKVDbEntity>> loadAllCacheAsync(boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<List<AppKVDbEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppKVDbEntity>> subscriber) throws Exception {
                List<AppKVDbEntity> list = loadAllCache(switchAccount);
                subscriber.onNext(list != null ? list : new ArrayList<AppKVDbEntity>(1));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 判断缓存是否存在
     *
     * @param fileName 业务名称，用来分类存储，可以为null。
     * @return
     */
    public static boolean isCacheExist(String fileName, String key, boolean switchAccount) {
        if(TextUtils.isEmpty(key)) {
            return false;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.contains(fileName, key, account);
    }

    public static Observable<Boolean> isCacheExistAsync(String fileName, String key,
                                                  boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(isCacheExist(fileName, key, switchAccount));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static boolean remove(String fileName, String key, boolean switchAccount) {
        if(TextUtils.isEmpty(key)) {
            return false;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.remove(fileName, key, account);
    }

    public static Observable<Boolean> removeAsyncObservable(String fileName, String key,
                                                         boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(remove(fileName, key, switchAccount));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void removeAsync(String fileName, String key,
                                                  boolean switchAccount) {
        removeAsyncObservable(fileName, key, switchAccount)
                .subscribe();
    }

    public static boolean removeByFileName(String fileName, boolean switchAccount) {
        if(TextUtils.isEmpty(fileName)) {
            return false;
        }
        String account = createAccount(switchAccount);
        return AppKVDbWrapper.INSTANCE.removeByFileName(fileName, account);
    }

    public static Observable<Boolean> removeByFileNameAsyncObservable(String fileName, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(removeByFileName(fileName, switchAccount));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void removeByFileNameAsync(String fileName, boolean switchAccount) {
        removeByFileNameAsyncObservable(fileName, switchAccount)
                .subscribe();
    }

    private static String createAccount(boolean switchAccount) {
        if(switchAccount) {
            IAuthAccount authAccount = AppInternal.getInstance().getAuthAccount();
            return authAccount != null ? authAccount.getUid() : EMPTY_STR;
        }
        return EMPTY_STR;
    }

}
