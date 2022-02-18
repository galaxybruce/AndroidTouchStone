package com.galaxybruce.base.util.debug;


import com.galaxybruce.component.ui.jetpack.JPBaseRequest;
import com.galaxybruce.component.ui.jetpack.JPBaseViewModel;
import com.galaxybruce.component.ui.jetpack.JPListDataModel;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class AppDebugLogDialogViewModel extends JPBaseViewModel {

    public final MutableLiveData<Boolean> notifyCurrentListChanged = new MutableLiveData<>();
    public final MutableLiveData<JPListDataModel> listData = new MutableLiveData<>();

    @Nullable
    @Override
    public List<JPBaseRequest> getRequests() {
        return new ArrayList<>(1);
    }


    {
        listData.setValue(new JPListDataModel(new ArrayList<>(), false));
    }
}
