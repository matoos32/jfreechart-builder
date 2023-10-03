/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2023, by Matt E. and project contributors
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.jfcbuilder.types;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Specialized {@link DateFormat} that generates the first letter(s) of the month for the first axis tick in that month
 * followed by that new month's day number for subsequent ticks. Does not generate time strings.
 * <p>
 * To determine if a new month is seen when formatting, class instances are stateful in remembering the last date value
 * passed to the format method.
 * <p>
 * <b>WARNING: No special localization formatting is deliberately done. The first letters of the month obtained for the
 * default locale settings are used. This generally works for the English language but is perhaps not be suitable for
 * other languages.</b>
 */
public class MinimalDateFormat extends DateFormat {

  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_MONTH_CHARS = 3;
  
  private int monthChars;
  private LocalDate lastDate;

  /**
   * Constructor using the default number of month label characters.
   */
  public MinimalDateFormat() {
    this(DEFAULT_MONTH_CHARS);
  }
  
  /**
   * Constructor for using a specific number of month characters. 
   * 
   * @param monthChars The number of month characters to display starting from the first letter of the month.
   * @throws IllegalArgumentException if monthChars is less than one.
   */
  public MinimalDateFormat(int monthChars) {
    
    if(monthChars < 1) {
      throw new IllegalArgumentException("Month characters must be at least 1 but " + monthChars + " was specified.");
    }
    
    this.monthChars = monthChars;
    lastDate = null;
  }

  @Override
  public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {

    if (date == null) {
      return toAppendTo;
    }

    LocalDate currentDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    
    StringBuffer strBuf = null;

    if (lastDate != null && currentDate.getMonth() != lastDate.getMonth()) {
      final String monthStr = currentDate.getMonth().toString();
      strBuf = toAppendTo
        .append(monthStr.substring(0, 1).toUpperCase() + monthStr.substring(1, monthChars).toLowerCase());

    } else {
      strBuf = toAppendTo.append(currentDate.getDayOfMonth());
    }

    lastDate = currentDate;

    return strBuf;
  }

  @Override
  public Date parse(String source, ParsePosition pos) {
    // Not supported
    return null;
  }

}
