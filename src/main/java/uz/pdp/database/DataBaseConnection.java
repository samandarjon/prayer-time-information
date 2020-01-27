package uz.pdp.database;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.model.*;

import javax.persistence.*;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataBaseConnection {

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("Test_db");
    private static EntityManager manager = factory.createEntityManager();
    private static EntityTransaction transaction = manager.getTransaction();

    public static void add(Message message) {
        transaction.begin();
        Integer user = message.getFrom().getId();
        Query query = manager.createQuery("select  u from Users u where u.user_id=" + user);
        List<Users> hasUser = query.getResultList();
        if (hasUser.size() == 0) {
            System.out.println(hasUser.size());

//          Create user
            Users users = new Users();
            users.setName(message.getFrom().getFirstName() + " " + message.getFrom().getLastName());
            users.setUsername(message.getFrom().getUserName());
            users.setChat_id(message.getChatId());
            users.setUser_id(message.getFrom().getId());

//          Create profile of User
            Profile profile = new Profile();
            profile.setUser(users);
            profile.setStep(0);

//          Add To Database

            manager.persist(users);
            manager.persist(profile);

            System.out.println("User Successfully Added");
        }
        transaction.commit();
    }

    public static Profile getProfile(Integer id) {
        transaction.begin();
        Query query = manager.createQuery("select  p from Profile  p where p.user.user_id =" + id);
        List<Profile> profiles = query.getResultList();
        transaction.commit();
        if (profiles.size() != 0) {
            return profiles.get(0);
        }
        return null;


    }

    public static void updateCountryOfProfile(CallbackQuery callbackQuery) {
        Integer user_id = callbackQuery.getFrom().getId();
        String data = callbackQuery.getData();
        transaction.begin();
        Query query = manager.createQuery("select  p from Profile  p where p.user.user_id = " + user_id);
        Profile profile = (Profile) query.getResultList().get(0);
        Country country = manager.find(Country.class, data);
        profile.setCountry(country);
        manager.persist(profile);
        transaction.commit();

    }

    public static List<Country> getCountries() {
        transaction.begin();
        Query query = manager.createQuery("select c from Country c");
        List<Country> countries = query.getResultList();
        transaction.commit();
        return countries;
    }

    public static void getTimes() {
        Document document = null;
        try {
            document = Jsoup.connect("http://shosh.uz/namoz-vaqtlari-toshkent-yanvar-2020/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert document != null;
        Elements rows = document.select("tbody>tr");
        for (Element row : rows) {
            Elements columns = row.select("td");
            for (Element column : columns) {
                Times times = new Times();
                times.setSana(Long.valueOf(column.child(2).text()));
            }
            System.out.println("============");
        }

    }

    public static void addPrayerTime(String month) {
        EntityTransaction transaction = manager.getTransaction();
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Document document = null;
        try {
            document = Jsoup.connect("http://shosh.uz/namoz-vaqtlari-toshkent-" + month.toLowerCase() + "-2020/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert document != null;
        Elements rows = document.select("tbody>tr");
        for (Element row : rows) {
            int columnNumber = 1;
            Elements columns = row.select("td");
            Times times = new Times();
            transaction.begin();
            for (Element column : columns) {
                switch (columnNumber) {
                    case 1: {
                        times.setSana(Long.valueOf(column.text()));
                        break;
                    }
                    case 2: {
                        times.setHafta_kuni(column.text());
                        break;
                    }
                    case 3: {
                        try {
                            times.setTong(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 4: {
                        try {
                            times.setQuyosh(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 5: {
                        try {
                            times.setPeshin(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 6: {
                        try {
                            times.setAsr(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 7: {
                        try {
                            times.setShom(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 8: {
                        try {
                            times.setHufton(new Time(formatter.parse(column.text()).getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                columnNumber++;
            }
            Month month_id = new Month();
            month_id.setName(month);
            month_id.setId("1");
            times.setMonth(month_id);
            manager.persist(times);
            transaction.commit();
            System.out.println("Successfully added");
        }
    }

    public static void addCounty() {
        EntityTransaction transaction = manager.getTransaction();
        String avval = "Чимкент (-1), Конибодом (-5), Хўжанд (-6), Қўқон (-7), Жамбул (-7), Наманган (-10), Фарғона (-10)," +
                " Марғилон (-10), Андижон (-11), Ўш (-14), Жалолобод (-15), Бишкек (-21), Олма–ота (-21), " +
                "Бекобод (+4), Туркистон (+4), Жиззах (+6), Гулистон (+7), Денов (+7), Жомбой (+7), Самарқанд (+9)," +
                " Шаҳрисабз (+10), Каттақўрғон (+12), Қарши (+12), Нурота (+14), Навоий (+19), Бухоро (+21), Хива (+35), " +
                "Нукус (+42)";

        String[] strings = avval.split(", ");
        for (String s1 : strings) {
            transaction.begin();
            String[] result = s1.split(" ");
            Country country = new Country();
            country.setName(result[0]);
            String diff = result[1].substring(2, result[1].length() - 1);
            if (result[1].charAt(1) == '-') {
                diff = "-" + diff;
            }
            country.setDifference(Long.valueOf(diff));
            manager.persist(country);
            transaction.commit();
            System.out.println("County Successfully Added");
        }
    }


    public static List<Times> getCurrentTimes() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        transaction.begin();
        Query query = manager.createQuery("select t from  Times t where t.sana=" + day + "and t.month.id =" + month);
        List<Times> times = new ArrayList<>();
        times = query.getResultList();
        transaction.commit();
        return times;
    }

    public static Time getTimeTong(String event) {
        transaction.begin();
        Calendar calendar = Calendar.getInstance();
        long day = calendar.get(Calendar.DAY_OF_MONTH);

        Query query = manager.createQuery("select t from Times  t where t.sana = " + day);
        Times times = (Times) query.getResultList().get(0);
        transaction.commit();
        switch (event) {
            case "tong":
                return times.getTong();
            case "quyosh":
                return times.getQuyosh();
            case "peshin":
                return times.getPeshin();
            case "asr":
                return times.getAsr();
            case "shom":
                return times.getShom();
            case "xufton":
                return times.getHufton();
        }

        return null;
    }


}

