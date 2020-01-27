package uz.pdp.task;

public class ScheduleMain {
    public static void main(String[] args) {
        ISchedule schedule = new testImp();

        schedule.test("HE");


    }


}

class testImp implements ISchedule {
    @Override
    public void test(String say) {
        System.out.println(say);
    }
}
