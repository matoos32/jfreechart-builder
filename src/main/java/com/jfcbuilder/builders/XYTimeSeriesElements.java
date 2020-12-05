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

package com.jfcbuilder.builders;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Objects;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Helper class for storing and accessing properties common to different kinds of XY TimeSeries
 * builders. Intended for use in composition-type implementations.
 */
class XYTimeSeriesElements {

  private static final String DEFAULT_NAME = "";
  private static final ZeroBasedIndexRange DEFAULT_INDEX_RANGE = new ZeroBasedIndexRange(0, 0);
  public static final Stroke DEFAULT_STYLE = BuilderConstants.SOLID_LINE;
  public static final Color DEFAULT_COLOR = Color.BLACK;

  private String name;
  private ZeroBasedIndexRange indexRange;
  private double[] data;
  private long[] timeData;
  private Color color;
  private Stroke style;

  /**
   * Constructor.
   */
  public XYTimeSeriesElements() {
    name = DEFAULT_NAME;
    indexRange = DEFAULT_INDEX_RANGE;
    data = BuilderConstants.EMPTY_SERIES_DATA;
    timeData = BuilderConstants.EMPTY_TIME_DATA;
    color = DEFAULT_COLOR;
    style = DEFAULT_STYLE;
  }

  /**
   * Gets the series name.
   * 
   * @return The series name
   */
  public String name() {
    return name;
  }

  /**
   * Sets the series name.
   * 
   * @param name The name to be set
   */
  public void name(String name) {
    this.name = (name == null) ? DEFAULT_NAME : name;
  }

  /**
   * Gets the zero-based index range to be used when generating the series.
   * 
   * @return The zero-based index range that will be used
   */
  public ZeroBasedIndexRange indexRange() {
    return indexRange;
  }

  /**
   * Sets the zero-based index range to be used when generating the series.
   * 
   * @param indexRange The zero-based index range to be set
   */
  public void indexRange(ZeroBasedIndexRange indexRange) {
    Objects.requireNonNull(indexRange, "Indexing reange cannot be null");
    this.indexRange = indexRange;
  }

  /**
   * Gets the data that will be used for generating the series.
   * 
   * @return The series data
   */
  public double[] data() {
    return data;
  }

  /**
   * Sets the data that will be used for generating the series.
   * 
   * @param data The data to set
   */
  public void data(double[] data) {
    Objects.requireNonNull(data, "Data series cannot be null");
    this.data = data;
  }

  /**
   * Gets the time data that will be used for generating the series.
   * 
   * @return The time data that will be used
   */
  public long[] timeData() {
    return timeData;
  }

  /**
   * Sets the time data that will be used for generating the series. Values should be in ascending
   * time and represent milliseconds since the epoch start.
   * 
   * @param timeData The time data to be used
   */
  public void timeData(long[] timeData) {
    Objects.requireNonNull(timeData, "Time data cannot be null");
    this.timeData = timeData;
  }

  /**
   * Gets the color that will be used to render the series.
   * 
   * @return The series rendering color
   */
  public Color color() {
    return color;
  }

  /**
   * Sets the desired color to be used when rendering the series. If set to null the default color
   * will be used.
   * 
   * @param color The color to be set
   */
  public void color(Color color) {
    this.color = (color == null) ? DEFAULT_COLOR : color;
  }

  /**
   * Gets the line style to be used when rendering the series.
   * 
   * @return The series rendering line style
   */
  public Stroke style() {
    return style;
  }

  /**
   * Sets the desired line style to be used when rendering the series. If set to null the default
   * style will be used.
   * 
   * @param style The style to be set
   */
  public void style(Stroke style) {
    this.style = (style == null) ? DEFAULT_STYLE : style;
  }

  /**
   * Checks to see if all preconditions for building the series are satisfied and throws an
   * exception if not.
   * 
   * @throws IllegalStateException If values data or timeData are null or empty, if values data and
   *         timeData lengths aren't equal, or if indexRange defines a range outside of the
   *         available data.
   */
  public void checkBuildPreconditions() throws IllegalStateException {

    final int timeDataLen = timeData.length;

    if (timeDataLen < 1) {
      throw new IllegalStateException("Configured time data is empty");
    }

    final int valDataLen = data.length;

    if (valDataLen < 1) {
      throw new IllegalStateException("Configured value data is empty");
    }

    if (timeDataLen != valDataLen) {
      throw new IllegalStateException("Length of time data series (");
    }

    if (indexRange != null && indexRange.getEndIndex() > (timeDataLen - 1)) {
      throw new IllegalStateException(
          "Configured index range is outside of the configures values and time data arrays");
    }
  }

}
