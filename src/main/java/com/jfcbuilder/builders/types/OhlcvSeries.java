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

import java.util.Objects;

/**
 * Class for aggregating and representing a stock market Open High Low Close Volume (OHLCV) series.
 * The values are stored in an individual primitive array for each component series. A record
 * represents the value in each array at the same particular element. All array lengths must match
 * otherwise logic errors could ensue.
 */
public class OhlcvSeries {

  private double[] openArr;
  private double[] highArr;
  private double[] lowArr;
  private double[] closeArr;
  private double[] volumeArr;

  public OhlcvSeries() {
    openArr = BuilderConstants.EMPTY_SERIES_DATA;
    highArr = BuilderConstants.EMPTY_SERIES_DATA;
    lowArr = BuilderConstants.EMPTY_SERIES_DATA;
    closeArr = BuilderConstants.EMPTY_SERIES_DATA;
    volumeArr = BuilderConstants.EMPTY_SERIES_DATA;
  }

  public OhlcvSeries(double[] openArr, double[] highArr, double[] lowArr, double[] closeArr,
      double[] volumeArr) {
    Objects.requireNonNull(openArr);
    Objects.requireNonNull(highArr);
    Objects.requireNonNull(lowArr);
    Objects.requireNonNull(closeArr);

    this.openArr = openArr;
    this.highArr = highArr;
    this.lowArr = lowArr;
    this.closeArr = closeArr;
    this.volumeArr = volumeArr;
  }

  public double[] opens() {
    return openArr;
  }

  public double[] highs() {
    return highArr;
  }

  public double[] lows() {
    return lowArr;
  }

  public double[] closes() {
    return closeArr;
  }

  public double[] volumes() {
    return volumeArr;
  }

}
