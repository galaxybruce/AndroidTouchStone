package com.galaxybruce.component.ui.jetpack

/**
 * @date 2020/8/18  1:54 PM
 * @author
 * @description
 * 
 *  bbsrecyclerView   数据配套使用 databinding 中 CustomBindAdapter updateBBSRecyclerViewData 使用
 *  
 *  在 viewmodel
 *      public MutableLiveData<JPListDataModel> list = new MutableLiveData<>();

            {
                list.setValue(new JPListDataModel(null, false));
            }
 *
 *
 *      <BBSRecyclerView2
            android:id="@+id/bbs_recyclerView"
            android:layout_width="0dp"
            android:layout_height="0dp"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toBottomOf="@id/fl_title"
            bbsRecyclerViewListData="@{vm.listData}"
            />
 *
 *
 * <p>
 * modification history:
 */
data class JPListDataModel(val list: List<Any>?, val isAvailable: Boolean = true) {

    var isError: Boolean = false

    constructor(list: List<Any>?, isAvailable: Boolean = true, isError: Boolean = false)
            : this(list, isAvailable) {
        this.isError = isError
    }

}