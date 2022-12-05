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

package com.jfcbuilder.builders;

import java.awt.Color;
import java.awt.Stroke;
import java.util.Date;

import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

import com.jfcbuilder.types.XYTimeSeriesElements;
import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Builder for producing TimeSeries instances from configured properties and source data.
 */
public class XYTimeSeriesBuilder implements IXYTimeSeriesBuilder<XYTimeSeriesBuilder> {

  private XYTimeSeriesElements elements;

  /**
   * Hidden constructor.
   */
  private XYTimeSeriesBuilder() {
    elements = new XYTimeSeriesElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYTimeSeriesBuilder get() {
    return new XYTimeSeriesBuilder();
  }

  @Override
  public XYTimeSeriesBuilder name(String name) {
    elements.name(name);
    return this;
  }

  @Override
  public XYTimeSeriesBuilder indexRange(ZeroBasedIndexRange indexRange) {
    elements.indexRange(indexRange);
    return this;
  }

  @Override
  public XYTimeSeriesBuilder data(double[] data) {
    elements.data(data);
    return this;
  }

  @Override
  public XYTimeSeriesBuilder timeData(long[] timeData) {
    elements.timeData(timeData);
    return this;
  }

  @Override
  public XYTimeSeriesBuilder showTimeGaps(boolean showTimeGaps) {
    elements.showTimeGaps(showTimeGaps);
    return this;
  }
  
  @Override
  public XYTimeSeriesBuilder color(Color color) {
    elements.color(color);
    return this;
  }

  @Override
  public Color color() {
    return elements.color();
  }

  @Override
  public XYTimeSeriesBuilder style(Stroke stroke) {
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

  @Override
  public TimeSeries build() throws IllegalStateException {

    elements.checkBuildPreconditions();

    final ZeroBasedIndexRange range = elements.indexRange();
    final long[] timeData = elements.timeData();
    final double[] data = elements.data();

    TimeSeries series = new TimeSeries(elements.name());

    for (int i = range.getStartIndex(); i <= range.getEndIndex(); ++i) {
      try {
        series.add(new Millisecond(new Date(timeData[i])), data[i], false);
      } catch (SeriesException e) {
        // Duplicate value being added within the time period.
        continue;
      }
    }

    return series;
  }
}
