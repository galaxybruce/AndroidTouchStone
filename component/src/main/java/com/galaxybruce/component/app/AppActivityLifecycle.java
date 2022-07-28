package com.galaxybruce.component.app;

import android.app.Activity;
import android.os.Bundle;

import com.galaxybruce.component.util.AppSimpleActivityLifecycleCallbacks;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class AppActivityLifecycle extends AppSimpleActivityLifecycleCallbacks {

    private final List<Activity> activities = new ArrayList<>();

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activities.remove(activity);
    }

    public void finishActivities() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public Activity getTopActivity() {
        return activities != null && activities.size() > 0 ? activities.get(activities.size() - 1) : null;
    }
}
