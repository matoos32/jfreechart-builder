/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2022, by Matt E. and project contributors
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

package com.jfcbuilder.adapters;

import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Extension to TimeSeriesCollection to map timestamp values to x-axis numeric indexes.
 */
public class NumberMappedTimeSeriesCollection extends TimeSeriesCollection
    implements INumberMappedCollection {

  /**
   * Generated value
   */
  private static final long serialVersionUID = -8069456517292390366L;

  /**
   * Constructor
   */
  public NumberMappedTimeSeriesCollection() {
    super();
    setXPosition(TimePeriodAnchor.MIDDLE);
  }

  @Override
  public double getStartXValue(int series, int item) {
    // Assume start and end are same
    return getXValue(series, item);
  }

  @Override
  public double getEndXValue(int series, int item) {
    // Assume start and end are same
    return getXValue(series, item);
  }
  
  @Override
  public double getXValue(int series, int item) {
    if ((item < 0) || (item >= getItemCount(series))) {
      return Double.NaN;
    }
    return item;
  }

  /**
   * TimeSeriesCollection.getX(RegularTimePeriod) maps a time period part. When
   * rendering with no gaps we just need the series index. This method will be
   * called by XYPlot.getDataRange(ValueAxis) when iterating through the
   * datasets to calculates their ranges.
   * <p>
   * It's unclear why JFreeChart needed 'synchronized' here.  For performance
   * perhaps it could be removed and let clients decide if they needed it.
   * They could implement their own concurrency solution.
   */
  @Override
  protected synchronized long getX(RegularTimePeriod period) {
    // Given the series index is not specified, assume that all series are using
    // the same number of elements and thus we can simply use index zero.
    return (long) getXValue(0, getSeries(0).getIndex(period));
  }
}
