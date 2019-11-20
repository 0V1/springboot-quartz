package com.gupaoedu.demo.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

/**
 * @Author: qingshan
 * @Date: 2018/9/19 15:19
 */
@Component
public class MyTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    // Trigger 被触发，Job 上的 execute() 方法将要被执行时
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        String triggerName = trigger.getKey().getName();
        System.out.println("Method 11111 " + triggerName + " was fired");
    }

    // 在 Trigger 触发后，Job 将要被执行时由 Scheduler 调用这个方法
    // 返回true时，这个任务不会被触发
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        String triggerName = trigger.getKey().getName();
        System.out.println("Method 222222 " + triggerName + " was not vetoed");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        String triggerName = trigger.getKey().getName();
        System.out.println("Method 333333 " + triggerName + " misfired");
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        String triggerName = trigger.getKey().getName();
        System.out.println("Method 444444 " + triggerName + " is complete");
        System.out.println("------------");
    }
}
