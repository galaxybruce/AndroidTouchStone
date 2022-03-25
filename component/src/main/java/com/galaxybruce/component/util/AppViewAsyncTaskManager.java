package com.galaxybruce.component.util;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 列表中异步处理和view相关的任务
 * created by bruce.zhang
 */
public class AppViewAsyncTaskManager<T> {

    public interface ViewAsyncTaskManagerCallback<T> {

        void onLoadingStart(String key, View view);

        T run(String key, View view);

        void onLoadingComplete(String key, View view, T result);
    }

    private final Map<String, Reference<T>> keysForCaches = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, String> keysForViews = Collections.synchronizedMap(new HashMap<>());
    private final Map<Integer, AsyncTask<Void, Void, T>> keysForTasks = Collections.synchronizedMap(new HashMap<>());        //每个view绑定的task
    private Executor executorService = null;

    private boolean isViewWasReused(View view, String key) {
        String currentCacheKey = getLoadingUriForView(view);
        return currentCacheKey != null && !key.equals(currentCacheKey);
    }

    String getLoadingUriForView(View view) {
        return this.keysForViews.get(view.hashCode());
    }

    void prepareDisplayTaskFor(View view, String key) {
        this.keysForViews.put(view.hashCode(), key);
    }

    void cancelDisplayTaskFor(View view) {
        this.keysForViews.remove(view.hashCode());
    }

    public void cancelTask() {
        this.keysForCaches.clear();
        this.keysForViews.clear();
        Collection<AsyncTask<Void, Void, T>> tasks = keysForTasks.values();
		for (AsyncTask<Void, Void, T> task : tasks) {
			if (task != null && !task.isCancelled()) {
				task.cancel(true);
			}
		}
        keysForTasks.clear();

        if (executorService != null) {
            if (executorService instanceof ExecutorService) {
                ((ExecutorService) executorService).shutdown();
            }
            executorService = null;
        }
    }

    public AsyncTask<Void, Void, T> handle(final View view, final String key,
										   final ViewAsyncTaskManagerCallback<T> runnable) {
        return handle(view, key, null , runnable);
    }

    public AsyncTask<Void, Void, T> handle(final View view, final String key,
										   Executor executorService, final ViewAsyncTaskManagerCallback<T> runnable) {
        if (TextUtils.isEmpty(key)) {
            cancelDisplayTaskFor(view);
            runnable.onLoadingStart(key, view);
            runnable.onLoadingComplete(key, view, null);
            return null;
        }

        //取消之前在这个vew上的task
        AsyncTask<Void, Void, T> preTask = keysForTasks.get(view.hashCode());
        if (preTask != null && !preTask.isCancelled()) {
            preTask.cancel(true);
        }

        final Reference<View> viewRef = new WeakReference<>(view);
        prepareDisplayTaskFor(viewRef.get(), key);

        runnable.onLoadingStart(key, view);

        Reference<T> bitmapRef = keysForCaches.get(key);
        if (bitmapRef != null && bitmapRef.get() != null) {
            runnable.onLoadingComplete(key, view, bitmapRef.get());
            return null;
        }

        if (executorService != null) {
            this.executorService = executorService;
        } else {
            executorService = AsyncTask.THREAD_POOL_EXECUTOR;
        }

        AsyncTask<Void, Void, T> asyncTask = new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                if (!isCancelled()) {
                    return runnable.run(key, viewRef.get());
                } else {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(T result) {
                super.onPostExecute(result);

                keysForCaches.put(key, new WeakReference(result));

                if (viewRef.get() == null) {

                } else if (isViewWasReused(viewRef.get(), key)) {

                } else {
                    cancelDisplayTaskFor(viewRef.get());
                    if (!isCancelled()) {
                        runnable.onLoadingComplete(key, viewRef.get(), result);
                    }
                }
            }

            @Override
            protected void onCancelled(T t) {
                super.onCancelled(t);
                cancelDisplayTaskFor(viewRef.get());
            }
        }.executeOnExecutor(executorService);

        keysForTasks.put(view.hashCode(), asyncTask);
        return asyncTask;
    }
}
