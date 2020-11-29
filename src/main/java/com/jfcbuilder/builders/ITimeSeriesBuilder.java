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

import org.jfree.data.time.TimeSeries;

/**
 * Interface for all TimeSeries builders. Uses a generic to specify the concrete implementation of
 * the interface as the return type of setter methods. This is done to support method chaining on
 * the same builder instance. In the framework there can be different builder types that have
 * specialized methods so making the return types here be this interface can and will hide those
 * specialized methods from clients when they chain the method calls.
 * 
 * @param T The method chaining return type, which must be the type of the builder implementing this
 *        interface.
 */
public interface ITimeSeriesBuilder<T extends ITimeSeriesBuilder<T>> {

  /**
   * Sets the time data array associated with various other data to be plotted. The array should
   * define all time instances in milliseconds since the epoch start.
   * 
   * @param timeData The time data to be set. <b>Warning: this array's length must be equal to the
   *        length of all other data to be plotted that will be associated with this time series.
   *        Failure to do so will result in undefined and possibly fatal behavior.</b>
   * @return Reference to this builder for chaining method calls
   */
  T timeData(long[] timeData);

  /**
   * Builds a new TimeSeries instance from all data and settings supplied to this builder.
   * 
   * @return The new TimeSeries instance if successful
   * @throws IllegalStateException If the builder was not setup adequately to properly create new
   *         TimeSeries instances
   */
  TimeSeries build() throws IllegalStateException;

}