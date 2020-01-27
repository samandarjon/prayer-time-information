package uz.pdp;

import java.sql.Time;

public class Test {
    public static void main(String[] args) {
        Time time = new Time(12, 10, 10);

        time.setTime(time.getTime() + 10 * 60 * 1000);
        System.out.println(time);
        Time time1 = new Time(time.getTime() + 10 * 60 * 1000);
        System.out.println(time1);
    }
}
