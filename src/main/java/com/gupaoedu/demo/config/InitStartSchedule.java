package com.gupaoedu.demo.config;

import com.gupaoedu.demo.listener.MyJobListener;
import com.gupaoedu.demo.listener.MySchedulerListener;
import com.gupaoedu.demo.listener.MyTriggerListener;
import com.gupaoedu.demo.task.TestTask1;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 这个类用于启动SpringBoot时，加载作业。run方法会自动执行。
 *
 * 另外可以使用 ApplicationRunner
 *
 */
@Slf4j
@Component
public class InitStartSchedule implements CommandLineRunner {

	@Autowired
	private MyJobFactory myJobFactory;

	@Autowired
	private MyJobListener myJobListener;

	@Autowired
	private MyTriggerListener myTriggerListener;

	@Autowired
	private MySchedulerListener mySchedulerListener;


	@Override
	public void run(String... args) throws Exception {
		/**
		 * 用于程序启动时加载定时任务，并执行已启动的定时任务(只会执行一次，在程序启动完执行)
		 */

		// 通过SchedulerFactory获取一个调度器实例
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler scheduler = sf.getScheduler();
		// 如果不设置JobFactory，Service注入到Job会报空指针
		scheduler.setJobFactory(myJobFactory);
//		scheduler.getListenerManager().addJobListener(myJobListener);
//		scheduler.getListenerManager().addSchedulerListener(mySchedulerListener);
//		scheduler.getListenerManager().addTriggerListener(myTriggerListener);
		// 启动调度器
		scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(TestTask1.class).withIdentity(TestTask1.class.getName(), "0").build();
        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/20 * * * * ?");
        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(TestTask1.class.getName(), "0")
                .withSchedule(scheduleBuilder).startNow().build();
        // 任务不存在的时候才添加
        if( !scheduler.checkExists(jobDetail.getKey()) ){
            try {
                scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException e) {
                log.info("\n创建定时任务失败"+e);
                throw new Exception("创建定时任务失败");
            }
        }

	}

}
