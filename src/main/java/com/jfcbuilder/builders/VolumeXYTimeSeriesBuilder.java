/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2020, by Matt E.
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
import java.util.Date;
import java.util.Objects;

import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import com.jfcbuilder.builders.types.DohlcvSeries;
import com.jfcbuilder.builders.types.ZeroBasedIndexRange;

/**
 * Builder for producing stock market volume data series using configured builder properties and
 * series data. Supports the ability to produce different series for rendering "close up" and "close
 * down" colors or rendering with a single color without consideration of close up/down days. To
 * configure this, use the {@code closeUpSeries()}, {@code closeDownSeries()}, or
 * {@code uniformSeries()} method calls on the builder to indicate which of these it is. The
 * configured color will be the rendering color for the series.
 * <p>
 * Based on how JFreeChart is believed to work, for up/down colors you can use one of these series
 * for "close up" days, and another for "close down" days. As such, the up day series is given a
 * {@code Double.NaN} value on down days, and the down day series will have a {@code Double.NaN}
 * given on up days.
 */
public class VolumeXYTimeSeriesBuilder implements IXYTimeSeriesBuilder<VolumeXYTimeSeriesBuilder> {

  private enum SeriesType {

    /**
     * The built series values and drawing settings are for bars where the OHLC price closed higher
     * than at open.
     */
    CLOSE_UP,

    /**
     * The built series values and drawing settings are for bars where the OHLC price closed lower
     * than at open.
     */
    CLOSE_DOWN,

    /**
     * The built series values and drawing settings are the same for all bars regardless of closing
     * up or down.
     */
    UNIFORM
  }

  private static final SeriesType DEFAULT_SERIES_TYPE = SeriesType.UNIFORM;

  /**
   * What type of series will be built by this builder.
   */
  private SeriesType seriesType;

  /**
   * The associated DOHLCV series from which up/down days will be determined.
   */
  private DohlcvSeries dohlcvSeries;

  private XYTimeSeriesElements elements;

  /**
   * Hidden constructor.
   */
  private VolumeXYTimeSeriesBuilder() {
    seriesType = DEFAULT_SERIES_TYPE;
    elements = new XYTimeSeriesElements();
    dohlcvSeries = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static VolumeXYTimeSeriesBuilder instance() {
    return new VolumeXYTimeSeriesBuilder();
  }

  @Override
  public VolumeXYTimeSeriesBuilder name(String name) {
    elements.name(name);
    return this;
  }

  @Override
  public VolumeXYTimeSeriesBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public VolumeXYTimeSeriesBuilder data(double[] data) {
    elements.data(data);
    return this;
  }

  @Override
  public VolumeXYTimeSeriesBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  @Override
  public VolumeXYTimeSeriesBuilder color(Color color) {
    elements.color(color);
    return this;
  }

  @Override
  public Color color() {
    return elements.color();
  }

  @Override
  public VolumeXYTimeSeriesBuilder style(Stroke stroke) {
    elements.style(stroke);
    return this;
  }

  @Override
  public Stroke style() {
    return elements.style();
  }

  @Override
  public String toString() {
    return elements.name();
  }

  public VolumeXYTimeSeriesBuilder ohlcv(DohlcvSeries dohlcv) {
    Objects.requireNonNull(dohlcv, "DOHLCV data cannot be set to null");
    dohlcvSeries = dohlcv;
    data(dohlcv.volumes()); // To satisfy xyBuilder precondition check but not needed otherwise.
    return this;
  }

  public VolumeXYTimeSeriesBuilder closeUpSeries() {
    seriesType = SeriesType.CLOSE_UP;
    return this;
  }

  public boolean isCloseUpSeries() {
    return seriesType == SeriesType.CLOSE_UP;
  }

  public VolumeXYTimeSeriesBuilder closeDownSeries() {
    seriesType = SeriesType.CLOSE_DOWN;
    return this;
  }

  public boolean isCloseDownSeries() {
    return seriesType == SeriesType.CLOSE_DOWN;
  }

  public VolumeXYTimeSeriesBuilder uniformSeries() {
    seriesType = SeriesType.UNIFORM;
    return this;
  }

  public boolean isUniformSeries() {
    return seriesType == SeriesType.UNIFORM;
  }

  protected void checkBuildPreconditions() throws IllegalStateException {
    elements.checkBuildPreconditions();

    if (dohlcvSeries == null) {
      throw new IllegalStateException("No DOHLCV data configured");
    }
  }

  @Override
  public TimeSeries build() throws IllegalStateException {

    checkBuildPreconditions();

    switch (seriesType) {
      case CLOSE_UP:
        return buildSeries(dohlcvSeries.opens(), dohlcvSeries.closes());
      case CLOSE_DOWN:
        return buildSeries(dohlcvSeries.closes(), dohlcvSeries.opens());
      case UNIFORM:
        return buildSeries();
      default:
        // Should never be here
        throw new IllegalStateException(
            "Unsupported series type configured (" + seriesType + "). Did someone add a new type?");
    }
  }

  private TimeSeries buildSeries(double[] lowerPrices, double[] higherPrices) {

    final ZeroBasedIndexRange range = elements.indexRange();
    final long[] timeData = elements.timeData();
    final double[] data = elements.data();

    TimeSeries series = new TimeSeries(elements.name());

    for (int i = range.getStartIndex(); i <= range.getEndIndex(); ++i) {

      try {
        // FIXME: Using Millisecond prevents rendering volume bars.
        series.add(new Day(new Date(timeData[i])),
            lowerPrices[i] <= higherPrices[i] ? data[i] : Double.NaN, false);
      } catch (SeriesException e) {
        // Duplicate value being added within the time period.
        continue;
      }
    }

    return series;
  }

  private TimeSeries buildSeries() {

    final ZeroBasedIndexRange range = elements.indexRange();
    final long[] timeData = elements.timeData();
    final double[] data = elements.data();

    TimeSeries series = new TimeSeries(elements.name());

    for (int i = range.getStartIndex(); i <= range.getEndIndex(); ++i) {

      try {
        // FIXME: Using Millisecond prevents rendering volume bars.
        series.add(new Day(new Date(timeData[i])), data[i], false);
      } catch (SeriesException e) {
        // Duplicate value being added within the time period.
        continue;
      }
    }

    return series;
  }

}
