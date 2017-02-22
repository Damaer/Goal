package cn.goal.goal;
// Create by LJF on 2017/02/21
import android.app.Activity;
import android.app.Service;
import android.os.Bundle;

import java.util.LinkedList;

import cn.qqtheme.framework.util.LogUtils;

public class QuickStartManager {
    //本类的实例
    private static QuickStartManager instance;
    //保存所有Activity
    private LinkedList<Activity> activities = new LinkedList<Activity>();
    //保存所有Service
    private LinkedList<Service> services = new LinkedList<Service>();

    public static QuickStartManager getInstance() {
        if (instance == null) {
            instance = new QuickStartManager();
        }
        return instance;
    }

    /**
     * 注册Activity以便集中“finish()”
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除Activity.
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 所有的Activity
     */
    public LinkedList<Activity> getActivities() {
        return activities;
    }

    /**
     * 最后加入的Activity
     */
    public Activity getLastActivity() {
        Activity activity = activities.getLast();
        LogUtils.debug(this, "last activity is " + activity.getClass().getName());
        return activity;
    }

    /**
     * 注册Service以便集中“stopSelf()”
     */
    public void addService(Service service) {
        services.add(service);
    }

    /**
     * Remove service.
     */
    public void removeService(Service service) {
        services.remove(service);
    }

    /**
     * 所有的Service
     */
    public LinkedList<Service> getServices() {
        return services;
    }

    /**
     * 退出软件
     */
    public void exitApp() {
        clearActivitiesAndServices();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);//normal exit application
    }

    /**
     * 当内存不足时，需要清除已打开的Activity及Service
     */
    public void clearActivitiesAndServices() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        for (Service service : services) {
            service.stopSelf();
        }
    }

}
