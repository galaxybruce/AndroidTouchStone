package com.galaxybruce.component.util.cache;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.galaxybruce.component.interal.AppInternal;
import com.galaxybruce.component.interal.IAuthAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
 * @description 文件缓存
 *
 * 存储优化:
 * 1. 文件存储使用：AppFileCacheManager
 * 2. 所有用到对象存储的业务使用：AppBigDataCacheManager
 * 3. 字段类型的小数据继续使用AppPreferencesUtil，不要直接使用AppSharedPreferencesCompat 和 SharedPreferences
 *
 * <p>
 * modification history:
 */
public class AppFileCacheManager {

    /**
     * 保存对象
     *
     * @param fileName
     * @param object
     * @throws IOException
     */
    public static boolean saveCacheObject(Context context, String fileName, Object object,
                                          boolean switchAccount) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        String strCache = null;
        try {
            strCache = JSON.toJSONString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return saveCacheString(context, fileName, strCache, switchAccount);
    }

    public static Observable<Boolean> saveCacheObjectAsyncObservable(Context context, String fileName,
                                                                Object object, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(saveCacheObject(context, fileName, object, switchAccount));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public static void saveCacheObjectAsync(Context context, String fileName,
                                            Object object, boolean switchAccount) {
        saveCacheObjectAsyncObservable(context, fileName, object, switchAccount)
                .subscribe();
    }

    /**
     * 保存字符串
     *
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static boolean saveCacheString(Context context, String fileName, String data, boolean switchAccount) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        String finalFileName = createKey(fileName, switchAccount);
        if (data == null) {
            data = "";
        }

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(finalFileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public static Observable<Boolean> saveCacheStringAsyncObservable(Context context, String fileName,
                                                              String data, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(saveCacheString(context, fileName, data, switchAccount));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public static void saveCacheStringAsync(Context context, String fileName,
                                                           String data, boolean switchAccount) {
        saveCacheStringAsyncObservable(context, fileName, data, switchAccount)
                .subscribe();
    }

    /**
     * 读取对象
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String loadCacheString(Context context, String fileName, String defValue, boolean switchAccount) {
        if (TextUtils.isEmpty(fileName)) {
            return defValue;
        }
        String finalFileName = createKey(fileName, switchAccount);
        if (!isFileCacheExist(context, finalFileName, false)) {
            return defValue;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(finalFileName);
            ois = new ObjectInputStream(fis);
            return (String) ois.readObject();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File file = context.getFileStreamPath(finalFileName);
                if(file != null) {
                    file.delete();
                }
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return defValue;
    }

    public static Observable<String> loadCacheStringAsync(Context context, String fileName,
                                                        String defValue, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> subscriber) throws Exception {
                String result = loadCacheString(context, fileName, defValue, switchAccount);
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
     * @param fileName
     * @return
     * @throws IOException
     */
    public static <T> T loadCacheObject(Context context, String fileName, Class<T> clazz,
                                        boolean switchAccount) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        String finalFileName = createKey(fileName, switchAccount);
        if (!isFileCacheExist(context, finalFileName, false)) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(finalFileName);
            ois = new ObjectInputStream(fis);
            String strData = (String) ois.readObject();

            if(!TextUtils.isEmpty(strData)) {
                try {
                    return JSON.parseObject(strData, clazz);
                } catch (Exception e) {
                    remove(context, fileName, switchAccount);
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File file = context.getFileStreamPath(finalFileName);
                if(file != null) {
                    file.delete();
                }
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * @param fileName
     * @param clazz
     * @param switchAccount
     * @param <T>
     * @return  由于Rxjava不允许返回null，所以用{@link com.galaxybruce.component.util.AppConstants#EMPTY_JSON}替代
     * 调用方需要校验这个值！！！
     */
    public static <T> Observable<T> loadCacheObjectAsync(Context context, String fileName,
                                                             Class<T> clazz, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
                T result = loadCacheObject(context, fileName, clazz, switchAccount);
                subscriber.onNext(result != null ? result : JSON.parseObject(EMPTY_JSON, clazz));
                subscriber.onComplete();
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 判断缓存是否存在
     *
     * @param fileName
     * @return
     */
    private static boolean isFileCacheExist(Context context, String fileName, boolean switchAccount) {
        if(TextUtils.isEmpty(fileName)) {
            return false;
        }
        if (context == null) {
            return false;
        }
        String finalFileName = createKey(fileName, switchAccount);
        boolean exist = false;
        File data = context.getFileStreamPath(finalFileName);
        if (data != null && data.exists()) {
            exist = true;
        }
        return exist;
    }

    public static Observable<Boolean> isFileCacheExistAsync(Context context, String fileName,
                                                        boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(isFileCacheExist(context, fileName, switchAccount));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static boolean remove(Context context, String fileName, boolean switchAccount) {
        if(TextUtils.isEmpty(fileName)) {
            return false;
        }
        String finalFileName = createKey(fileName, switchAccount);
        if (isFileCacheExist(context, finalFileName, false)) {
            return context.deleteFile(finalFileName);
        }
        return false;
    }

    public static Observable<Boolean> removeAsyncObservable(Context context, String fileName, boolean switchAccount) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> subscriber) throws Exception {
                subscriber.onNext(remove(context, fileName, switchAccount));
                subscriber.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void removeAsync(Context context, String fileName,  boolean switchAccount) {
        removeAsyncObservable(context, fileName, switchAccount)
                .subscribe();
    }

    private static String createKey(String key, boolean switchAccount) {
        if(switchAccount) {
            IAuthAccount authAccount = AppInternal.getInstance().getAuthAccount();
            String uid = authAccount != null ? authAccount.getUid() : "";
            key = key + "_" + uid;
        }
        return key;
    }

}
