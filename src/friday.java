/*
ID: eddycyu
LANG: JAVA
TASK: friday
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class friday {

    // months
    private static final int JANUARY = 0;
    private static final int FEBRUARY = 1;
    private static final int MARCH = 2;
    private static final int APRIL = 3;
    private static final int MAY = 4;
    private static final int JUNE = 5;
    private static final int JULY = 6;
    private static final int AUGUST = 7;
    private static final int SEPTEMBER = 8;
    private static final int OCTOBER = 9;
    private static final int NOVEMBER = 10;
    private static final int DECEMBER = 11;

    // days of week
    private static final int MONDAY = 0;
    private static final int TUESDAY = 1;
    private static final int WEDNESDAY = 2;
    private static final int THURSDAY = 3;
    private static final int FRIDAY = 4;
    private static final int SATURDAY = 5;
    private static final int SUNDAY = 6;

    // number of days in a week
    private static final int DAYS_IN_WEEK = 7;

    private boolean isLeapYear(int year) {
        // special case for century year
        if (year % 100 == 0) {
            return (year % 400 == 0);
        }

        // check if year is divisible by 4
        return (year % 4 == 0);
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;

            case JANUARY:
            case MARCH:
            case MAY:
            case JULY:
            case AUGUST:
            case OCTOBER:
            case DECEMBER:
                return 31;

            case FEBRUARY: {
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            }
            default:
                return 0;
        }
    }

    private int getDayOfWeek(int startingDayOfWeek, int daysToIncrement) {
        // 0 = monday ... 6 = sunday
        return (startingDayOfWeek + (daysToIncrement - 1)) % DAYS_IN_WEEK;
    }

    public static void main(String[] args) throws IOException {
        try (final BufferedReader f = new BufferedReader(new FileReader("friday.in"));
             final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("friday.out")))) {

            // index 0 = monday ... 6 = sunday
            final int[] days = new int[7];

            // parse the number of years
            final int N = Integer.parseInt(f.readLine());

            // instantiate app
            final friday app = new friday();

            // calculate
            int dayOfWeekForFirst = 0;
            for (int year = 1900; year < (1900 + N); year++) {
                for (int month = 0; month < 12; month++) {
                    int daysInMonth = app.getDaysInMonth(month, year);
                    int dayOfWeekForThirteen = app.getDayOfWeek(dayOfWeekForFirst, 13); // advance to the 13th day
                    days[dayOfWeekForThirteen] += 1;

                    // advance to first day of next month
                    dayOfWeekForFirst = app.getDayOfWeek(dayOfWeekForFirst, daysInMonth + 1); // +1 for 1st day of new month
                }
            }

            // output
            out.print(days[SATURDAY] + " ");
            out.print(days[SUNDAY] + " ");
            out.print(days[MONDAY] + " ");
            out.print(days[TUESDAY] + " ");
            out.print(days[WEDNESDAY] + " ");
            out.print(days[THURSDAY] + " ");
            out.println(days[FRIDAY]);
        }
    }
}
