package vip.superbrain.immersion.toolbar.java;

import org.junit.Test;

import vip.superbrain.immersion.toolbar.utils.DateUtils;

public class DateTimeUtilsTest {

    @Test
    public void voidTestDataTime() {
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2019-01-17 00:10:56"));
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2020-01-17 00:10:56"));
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2020-01-18 00:10:56"));
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2020-01-19 00:10:56"));
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2020-01-20 00:10:56"));
        System.out.println(DateUtils.INSTANCE.showTodayTomorrowOrDateTimeWithOutSecond("2021-01-20 00:10:56"));
    }
}
