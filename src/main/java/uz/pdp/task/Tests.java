package uz.pdp.task;

import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import uz.pdp.database.IDatabaseAction;
import uz.pdp.model.Users;

public class Tests {
    public static void main(String[] args) {
        ScheduledConfiguration configuration = new ScheduledConfiguration();
        configuration.configureTasks(new ScheduledTaskRegistrar());
        IDatabaseAction action = null;
        action.add(new Users());

    }
}
