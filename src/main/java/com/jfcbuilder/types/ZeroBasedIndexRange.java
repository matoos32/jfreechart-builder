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

/**
 * Represents a range of container elements by defining both a start and end zero-based index.
 */
public class ZeroBasedIndexRange {

  private int startIndex;
  private int endIndex;

  /**
   * Constructor.
   */
  public ZeroBasedIndexRange() {
    startIndex = 0;
    endIndex = 0;
  }

  /**
   * Constructor that sets the zero-based indexes defining the range.
   * 
   * @param startIndex Zero based index of the start of the range
   * @param endIndex Zero based index of the end of the range
   * @throws IllegalArgumentException If startIndex or endIndex are negative, or if startIndex is
   *         greater than endIndex.
   */
  public ZeroBasedIndexRange(int startIndex, int endIndex) throws IllegalArgumentException {
    set(startIndex, endIndex);
  }

  /**
   * Sets the zero-based indexes defining the range.
   * 
   * @param startIndex Zero based index of the start of the range
   * @param endIndex Zero based index of the end of the range
   * @throws IllegalArgumentException If startIndex or endIndex are negative, or if startIndex is
   *         greater than endIndex.
   */
  public void set(int startIndex, int endIndex) throws IllegalArgumentException {

    if (startIndex < 0) {
      throw new IllegalArgumentException("Start index must be zero or greater");
    }

    if (endIndex < 0) {
      throw new IllegalArgumentException("End index must be zero or greater");
    }

    if (startIndex > endIndex) {
      throw new IllegalArgumentException("Start index must be smaller than end index");
    }

    this.startIndex = startIndex;
    this.endIndex = endIndex;
  }

  /**
   * Gets the zero-based start index defining the range.
   * 
   * @return Zero based start index of the start of the range
   */
  public int getStartIndex() {
    return startIndex;
  }

  /**
   * Gets the zero-based end index defining the range.
   * 
   * @return Zero based end index of the start of the range
   */
  public int getEndIndex() {
    return endIndex;
  }
}
