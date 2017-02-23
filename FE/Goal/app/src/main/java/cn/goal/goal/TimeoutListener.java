package cn.goal.goal;
// Created by LJF on 2017/2/22.

public interface TimeoutListener {
    void onTimePoint(String hour, String minute, String second);
    void onTimeout();
}
