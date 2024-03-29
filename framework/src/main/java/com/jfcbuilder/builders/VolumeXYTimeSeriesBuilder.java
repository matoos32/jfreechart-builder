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

package com.jfcbuilder.builders;

import java.awt.Color;
import java.util.Date;
import java.util.Objects;

import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.XYDataset;

import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.OhlcvSeries;
import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Builder for producing stock market volume data sets that can be used in Volume XY plots.
 */
public class VolumeXYTimeSeriesBuilder
    implements IXYTimeSeriesDatasetBuilder<VolumeXYTimeSeriesBuilder> {

  private OhlcvSeries ohlcv;
  private Color upColor;
  private Color downColor;
  private ZeroBasedIndexRange indexRange;
  private long[] timeData;

  /**
   * Hidden constructor.
   */
  private VolumeXYTimeSeriesBuilder() {
    ohlcv = new OhlcvSeries();
    upColor = BuilderConstants.DEFAULT_UP_COLOR;
    downColor = BuilderConstants.DEFAULT_DOWN_COLOR;
    indexRange = null;
    timeData = BuilderConstants.EMPTY_TIME_DATA;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static VolumeXYTimeSeriesBuilder get() {
    return new VolumeXYTimeSeriesBuilder();
  }

  /**
   * Sets the OHLCV data to be used for building the XYDataset.
   * 
   * @param ohlcv The data to be set
   * @return Reference to this builder instance for method chaining
   */
  public VolumeXYTimeSeriesBuilder ohlcv(OhlcvSeries ohlcv) {
    Objects.requireNonNull(ohlcv, "OHLCV series cannot be set to null");
    this.ohlcv = ohlcv;
    return this;
  }

  /**
   * Sets the color to use for drawing OHLCV items on days where price closed up (higher than open).
   * 
   * @param color The color to be set
   * @return Reference to this builder instance for method chaining
   */
  public VolumeXYTimeSeriesBuilder upColor(Color color) {
    Objects.requireNonNull(color, "Color cannot be set to null");
    upColor = color;
    return this;
  }

  /**
   * Gets the color to use for drawing OHLCV items on days where price closed up (higher than open).
   * 
   * @return The color
   */
  public Color upColor() {
    return upColor;
  }

  /**
   * Sets the color to use for drawing OHLCV items on days where price closed down (lower than
   * open).
   * 
   * @param color The color to be set
   * @return Reference to this builder instance for method chaining
   */
  public VolumeXYTimeSeriesBuilder downColor(Color color) {
    Objects.requireNonNull(color, "Color cannot be set to null");
    downColor = color;
    return this;
  }

  /**
   * Gets the color to use for drawing OHLCV items on days where price closed down (lower than
   * open).
   * 
   * @return The color
   */
  public Color downColor() {
    return downColor;
  }

  @Override
  public VolumeXYTimeSeriesBuilder indexRange(ZeroBasedIndexRange indexRange) {
    this.indexRange = indexRange;
    return this;
  }

  @Override
  public VolumeXYTimeSeriesBuilder timeData(long[] timeData) {
    Objects.requireNonNull(timeData, "Time data cannot be set to null");
    this.timeData = timeData;
    return this;
  }

  @Override
  public XYDataset build() throws IllegalStateException {

    if (timeData == null) {
      throw new IllegalStateException("No time data configured");
    }

    if (indexRange != null && indexRange.getEndIndex() > (timeData.length - 1)) {
      throw new IllegalStateException(
          "Configured index range is greater than configured data size");
    }

    final double[] opens = ohlcv.opens();
    final double[] closes = ohlcv.closes();
    final double[] volumes = ohlcv.volumes();

    if (timeData.length != opens.length && timeData.length != closes.length
        && timeData.length != volumes.length) {

      throw new IllegalStateException("Time and OHLCV array sizes do not match");
    }

    if (timeData.length != volumes.length) {

      throw new IllegalStateException("Time and OHLCV array sizes do not match");
    }

    OHLCSeriesCollection collection = new OHLCSeriesCollection();
    OHLCSeries series = new OHLCSeries("volume");

    ZeroBasedIndexRange range = (indexRange != null) ? indexRange
        : new ZeroBasedIndexRange(0, timeData.length - 1);

    final double low = 0.0;

    for (int i = range.getStartIndex(); i <= range.getEndIndex(); ++i) {
      try {
        final double open = opens[i] <= closes[i] ? 0.0 : volumes[i];
        final double high = volumes[i];
        final double close = opens[i] <= closes[i] ? volumes[i] : 0.0;

        series.add(new Millisecond(new Date(timeData[i])), open, high, low, close);
      } catch (SeriesException e) {
        // Duplicate value being added within the time period.
        continue;
      }
    }

    collection.addSeries(series);

    return collection;
  }

}
