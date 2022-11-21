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

package com.jfcbuilder.types;

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

  /**
   * Constructor. Initializes all value arrays to an empty one.
   */
  public OhlcvSeries() {
    openArr = BuilderConstants.EMPTY_SERIES_DATA;
    highArr = BuilderConstants.EMPTY_SERIES_DATA;
    lowArr = BuilderConstants.EMPTY_SERIES_DATA;
    closeArr = BuilderConstants.EMPTY_SERIES_DATA;
    volumeArr = BuilderConstants.EMPTY_SERIES_DATA;
  }

  /**
   * Constructor for initializing with specific value arrays. All arrays should be of same length
   * with date values corresponding to values in other arrays at the same element index.
   * 
   * @param openArr Array of price open values
   * @param highArr Array of price high values
   * @param lowArr Array of price low values
   * @param closeArr Array of price close values
   * @param volumeArr Array of volume values
   */
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

  /**
   * Gets the configured open values.
   * 
   * @return The array of open values.
   */
  public double[] opens() {
    return openArr;
  }

  /**
   * Gets the configured high values.
   * 
   * @return The array of high values.
   */
  public double[] highs() {
    return highArr;
  }

  /**
   * Gets the configured low values.
   * 
   * @return The array of low values.
   */
  public double[] lows() {
    return lowArr;
  }

  /**
   * Gets the configured date values.
   * 
   * @return The array of date values.
   */
  public double[] closes() {
    return closeArr;
  }

  /**
   * Gets the configured volume values.
   * 
   * @return The array of volume values.
   */
  public double[] volumes() {
    return volumeArr;
  }

}
