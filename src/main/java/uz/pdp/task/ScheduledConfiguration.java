package uz.pdp.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import uz.pdp.database.DataBaseConnection;
import uz.pdp.model.Times;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Configuration
@EnableScheduling
public class ScheduledConfiguration implements SchedulingConfigurer {

    private TaskScheduler taskScheduler;
    private static List<Times> times;
    private static boolean isEnter = false;
    private ScheduledFuture<?> job1;
    private ScheduledFuture<?> job2;
    private static int a = 3;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(6);
        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");
        threadPoolTaskScheduler.initialize();
        job1(threadPoolTaskScheduler);
        job2(threadPoolTaskScheduler);
        this.taskScheduler = threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
        times = new ArrayList<>();

    }

    private void job1(TaskScheduler scheduler) {
        job1 = scheduler.schedule(() -> {
                    times = DataBaseConnection.getCurrentTimes();
                    if (true) {
                        isEnter = false;
                        job2.cancel(true);
                        System.out.println("Task2 canceled");
                        a = 0;
                    } else if (isEnter) {
                        a += 3;
                        isEnter = false;
                    }
                    if (job2.isCancelled()) {
                        a++;
                        if (a == 4) {
                            System.out.println("Task2 is running.");
                            job2(taskScheduler);
                        }
                    }

                    System.out.println("task1 " + new Date());
                }, triggerContext -> {
                    String cronExp = "0/5 * * * * ?";
                    return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
                }

        );
        job1.isDone();
    }

    private void job2(TaskScheduler scheduler) {
        job2 = scheduler.schedule(() -> {
                    isEnter = true;
                    System.out.println("task2 " + new Date());
                }, triggerContext -> {
                    String cronExp = "0/" + a + " * * * * ?";
                    return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
                }
        );
//        job2.cancel(job2.isDone())
    }

}