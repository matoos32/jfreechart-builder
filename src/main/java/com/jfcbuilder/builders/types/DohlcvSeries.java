/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2020, by Matt E. and project contributors
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

package com.jfcbuilder.builders.types;

/**
 * Class for aggregating and representing a stock market Date Open High Low Close Volume (DOHLCV)
 * series. The values are stored in an individual primitive array for each component series. A
 * record represents the value in each array at the same particular element. All array lengths must
 * match otherwise logic errors could ensue.
 */
public class DohlcvSeries extends OhlcvSeries {

  private long[] dateArr;

  public DohlcvSeries() {
    super();
    dateArr = BuilderConstants.EMPTY_TIME_DATA;
  }

  public DohlcvSeries(long[] dateArr, double[] openArr, double[] highArr, double[] lowArr,
      double[] closeArr, double[] volumeArr) {
    super(openArr, highArr, lowArr, closeArr, volumeArr);
    this.dateArr = dateArr;
  }

  public long[] dates() {
    return dateArr;
  }
}