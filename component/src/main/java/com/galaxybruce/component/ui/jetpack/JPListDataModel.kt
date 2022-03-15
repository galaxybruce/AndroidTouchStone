package com.galaxybruce.component.ui.jetpack

/**
 * @date 2020/8/18  1:54 PM
 * @author
 * @description
 * 
 *  bbsrecyclerView   数据配套使用 databinding 中 CustomBindAdapter updateAppRecyclerViewData 使用
 *  
 *  在 viewmodel
 *      public MutableLiveData<JPListDataModel> list = new MutableLiveData<>();

            {
                list.setValue(new JPListDataModel(null, false));
            }
 *
 *
 *      <AppRecyclerView2
            android:id="@+id/app_recyclerView"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/fl_title"
            appRecyclerViewListData="@{vm.listData}"
            />
 *
 *
 * <p>
 * modification history:
 */
data class JPListDataModel<T>(val list: List<T>? = null) {

    var isError: Boolean = false
    var isAvailable: Boolean = true

    constructor(list: List<T>?,
                isAvailable: Boolean = true,
                isError: Boolean = false)
            : this(list) {
        this.isError = isError
        this.isAvailable = isAvailable
    }
}