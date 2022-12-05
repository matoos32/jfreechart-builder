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

import org.jfree.chart.annotations.XYAnnotation;

/**
 * Interface for all XYAnnotation builders. Uses a generic to specify the concrete implementation of
 * the interface as the return type of setter methods. This is done to support method chaining on
 * the same builder instance. In this framework there can be different builder types that have
 * specialized methods. If the return types were made to be this interface instead of the concrete
 * class then those specialized methods of the classes not defined in the interface would be hidden
 * by only having access to the interface.
 * 
 * @param <T> The method chaining return type, which must be the type of the builder implementing
 *            this interface.
 */
public interface IXYAnnotationBuilder<T extends IXYAnnotationBuilder<T>> {

  /**
   * Builds the XYAnnotation from all configured data and properties.
   * 
   * @return New instance of an XYAnnotation corresponding to all configured data and properties
   * @throws IllegalStateException If the builder is missing properties when {@code build()} is
   *                               called
   */
  XYAnnotation build() throws IllegalStateException;

  /**
   * Helper mutation method for replacing the configured x-axis date values with corresponding
   * source array element index value.
   * <p>
   * Meant to be called internally by the framework itself, not by framework clients.
   * <p>
   * For facilitating the removal of visible time gaps on charts.
   * <p>
   * Different implementations may use different methods to map configured x value(s) to numeric
   * indices, including search algorithms that may have higher order Big-O complexity.
   * 
   * @param timeData             The array of source time values
   * @param indexRangeStartIndex The start index of the series index range
   * @param indexRangeEndIndex   The end index of the series index range
   */
  void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex);
}
