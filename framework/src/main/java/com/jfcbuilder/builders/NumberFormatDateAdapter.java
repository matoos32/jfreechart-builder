/*
 * jfreechart-builder-demo: a demonstration app for jfreechart-builder
 * 
 * (C) Copyright 2022, by Matt E.
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

package com.jfcbuilder.builders;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Objects;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Class wrapping {@link DateFormat} to first lookup date values from supplied value indices then double-dispatch the
 * values to an actual {@link DateFormat}.
 */
public class NumberFormatDateAdapter extends NumberFormat {

  private static final long serialVersionUID = 1L;
  
  //private int lastIntNum = 0;

  private ZeroBasedIndexRange range;
  private long[] timeData;
  private DateFormat dateFormat;
  
  public NumberFormatDateAdapter(ZeroBasedIndexRange range, long[] timeData, DateFormat dateFormat) {

    Objects.requireNonNull(range, "Index range not set");
    Objects.requireNonNull(timeData, "Time data not set");
    Objects.requireNonNull(dateFormat, "Date format not set");

    this.range = range;
    this.timeData = timeData;
    this.dateFormat = dateFormat;
  }
  
  @Override
  public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {

    if (Double.isNaN(number)) {
      return toAppendTo;
    }

    final int intNum = (int) number;

    final int timeIndex = range.getStartIndex() + intNum;
    
    if ((intNum < 0) || (timeIndex >= timeData.length)) {
      
      return toAppendTo;

    } else {
      
      return toAppendTo.append(dateFormat.format(new Date(timeData[timeIndex])));
    }
  }

  @Override
  public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
    return format((double) number, toAppendTo, pos);
  }

  @Override
  public Number parse(String source, ParsePosition parsePosition) {
    // Not supported
    return null;
  }

}
