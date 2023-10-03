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

import org.jfree.data.xy.XYDataset;

import com.jfcbuilder.types.ZeroBasedIndexRange;

/**
 * Interface for all time series based dataset builders. Uses a generic to specify the concrete
 * implementation of the interface as the return type of setter methods. This is done to support
 * method chaining on the same builder instance. In this framework there can be different builder
 * types that have specialized methods. If the return types were made to be this interface instead
 * of the concrete class then those specialized methods of the classes not defined in the interface
 * would be hidden by only having access to the interface.
 * 
 * @param <T> The method chaining return type, which must be the type of the builder implementing
 *            this interface.
 */
public interface IXYTimeSeriesDatasetBuilder<T extends IXYTimeSeriesDatasetBuilder<T>> {

  /**
   * Sets the time data to be used for generating the XYDataset.
   * 
   * @param timeData Ascending date-time values represented as milliseconds since the epoch start
   * @return Reference to this builder instance for method chaining
   */
  T timeData(long[] timeData);

  /**
   * Sets the zero-based index range used to index into all source data for building the XYDataset.
   * This is an optimization to allow clients to supply existing data without having to copy and/or
   * crop it to match the desired size in chart.
   * 
   * @param indexRange The index range to be set
   * @return Reference to this builder instance for method chaining
   */
  T indexRange(ZeroBasedIndexRange indexRange);

  /**
   * Builds the XYDataset
   * 
   * @return New instance of the corresponding XYDataset
   * @throws IllegalStateException If source array sizes don't match or if an index range is
   *                               configured and its indexes are out of bounds.
   */
  XYDataset build() throws IllegalStateException;
}
