/*
 *
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 * Copyright (C) 2015 GuDong <maoruibin9035@gmail.com>
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package yyl.yungirl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *  日期转换工具类
 *  Created by yinyiliang on 2016/6/14 0014.
 */
public class DateUtil {

    /**
     * 转换成yyyy/MM/dd形式的字符串
     * @param date
     * @return
     */
    public static String toDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    /**
     * 转换成yyyy-MM-dd形式的字符串
     * @param date
     * @return
     */
    public static String toOneData(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 将yyyy-MM-dd"形式的字符串转换成Date
     * @param s
     * @return
     * @throws ParseException
     */
    public static Date parseToDate(String s) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(s);
    }

    /**
     * 获得前一天的Date日期
     * @param date
     * @return
     */
    public static Date getLastdayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获得后一天的Date日期
     * @param date
     * @return
     */
    public static Date getNextdayDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
