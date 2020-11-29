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

import com.jfcbuilder.builders.types.ZeroBasedIndexRange;

/**
 * Interface for all XYTimeSeries builders. Uses a generic to specify the concrete implementation of
 * the interface as the return type of setter methods. This is done to support method chaining on
 * the same builder instance. In the framework there can be different builder types that have
 * specialized methods so making the return types here be this interface can and will hide those
 * specialized methods from clients when they chain the method calls.
 * 
 * @param T The method chaining return type, which must be the type of the builder implementing this
 *        interface.
 */
public interface IXYTimeSeriesBuilder<T extends IXYTimeSeriesBuilder<T>>
    extends ITimeSeriesBuilder<T> {

  /**
   * Sets the name associated with the series.
   * 
   * @param name The name to be set
   * @return Reference to this builder for chaining method calls
   */
  T name(String name);

  /**
   * Sets zero-based data indexing range (start index and end index) to be used. By using this
   * range, implementations can iterate over this particular sub-set of elements in the data array
   * and clients do not have to copy their data simply to provide the proper set of data starting at
   * index zero and ending at (length - 1). This should reduce memory usage and computational
   * overhead.
   * 
   * @param indexRange The data array indexing range to be set
   * @return Reference to this builder for chaining method calls
   */
  T indexRange(ZeroBasedIndexRange indexRange);

  /**
   * Sets the data value array to be plotted.
   * 
   * @param data The value data to be set. <b>Warning: this array's length must be equal to the
   *        length of the associated time series array set via
   *        <i>ITimeSeriesBuilder.timeData(long[])</i>. Failure to do so will result in undefined
   *        and possibly fatal behavior.</b>
   * @return Reference to this builder for chaining method calls
   */
  T data(double[] data);

  /**
   * Sets the color used to draw series that is built. This is not used explicitly by this builder
   * but is associated with the built series so is cached in the builder as a convenience for lookup
   * in a subsequent step after <i>ITimeSeriesBuilder.build()</i> is called.
   * 
   * @param color The color to be used when the series is drawn
   * @return Reference to this builder for chaining method calls
   */
  T color(Color color);

  /**
   * Gets the cached color to be used for drawing the series.
   * 
   * @return The series drawing color
   */
  Color color();

  /**
   * Sets the Stroke ("style") used to draw series that is built. This is not used explicitly by
   * this builder but is associated with the built series so is cached in the builder as a
   * convenience for lookup in a subsequent step after <i>ITimeSeriesBuilder.build()</i> is called.
   * 
   * @param stroke The stroke defining the XY line style to be used when the series is drawn
   * @return Reference to this builder for chaining method calls
   */
  T style(Stroke stroke);

  /**
   * Gets the cached style to be used for drawing the series.
   * 
   * @return The series drawing style
   */
  Stroke style();
}