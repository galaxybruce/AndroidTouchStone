package com.galaxybruce.touchstone.task;

import com.effective.android.anchors.TaskCreator;
import com.effective.android.anchors.TaskFactory;

/**
 * @date 2020-01-10 15:09
 * @author bruce.zhang
 * @description 初始化对象factory，这里的任务是手动初始化，建议采用注解的形式@TaskAnchor
 * <p>
 * modification history:
 */
public class InitTaskFactory extends TaskFactory {

    public static final String PROJECT                          = "PROJECT";
    public static final String TASK_NETWORK                     = "InitNetworkTask";
    public static final String TASK_NECESSARY_MAIN_PROCESS      = "InitNecessaryTask_Main_Process";
    public static final String TASK_NECESSARY_ALL_PROCESS       = "InitNecessaryTask_All_Process";
    public static final String TASK_ASYNC_GROUP                 = "InitAsyncGroupTask";



    public InitTaskFactory(TaskCreator creator) {
        super(taskName -> {
            switch (taskName) {
                case TASK_NETWORK:
                    return new InitNetworkTask();
                case TASK_NECESSARY_MAIN_PROCESS:
                    return new InitNecessaryTask_Main_Process();
                case TASK_NECESSARY_ALL_PROCESS:
                    return new InitNecessaryTask_All_Process();
                case TASK_ASYNC_GROUP:
                    return new InitAsyncGroupTask();
                default:
                    if(creator != null) {
                        return creator.createTask(taskName);
                    }

            }
            return null;
        });
    }
}
