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

import java.awt.Paint;
import java.awt.Stroke;
import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;

import com.jfcbuilder.types.BuilderConstants;

/**
 * Builder for producing {@link XYLineAnnotation} objects.
 */
public class XYLineBuilder implements IXYAnnotationBuilder<XYLineBuilder> {

  private double x1;
  private double y1;
  private double x2;
  private double y2;
  private Stroke style;
  private Paint color;
  
  /**
   * Hidden constructor.
   */
  private XYLineBuilder() {
    x1 = Double.NaN;
    y1 = Double.NaN;
    x2 = Double.NaN;
    y2 = Double.NaN;
    style = BuilderConstants.SOLID_LINE;
    color = BuilderConstants.DEFAULT_LINE_COLOR;
  }
  
  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYLineBuilder get() {
    return new XYLineBuilder();
  }
  
  public double x1() {
    return x1;
  }

  public XYLineBuilder x1(double x1) {
    this.x1 = x1;
    return this;
  }

  public double y1() {
    return y1;
  }

  public XYLineBuilder y1(double y1) {
    this.y1 = y1;
    return this;
  }

  public double x2() {
    return x2;
  }

  public XYLineBuilder x2(double x2) {
    this.x2 = x2;
    return this;
  }

  public double y2() {
    return y2;
  }

  public XYLineBuilder y2(double y2) {
    this.y2 = y2;
    return this;
  }

  public Stroke style() {
    return style;
  }

  public XYLineBuilder style(Stroke style) {
    this.style = style;
    return this;
  }

  public Paint color() {
    return color;
  }

  public XYLineBuilder color(Paint color) {
    this.color = color;
    return this;
  }

  /**
   * Checks to see if all preconditions for building the annotation are satisfied and throws an exception if not.
   * 
   * @throws IllegalStateException If x1, y1, x2, or y2 have not been configured.
   */
  public void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x1) || Double.isNaN(y1)) {
      throw new IllegalStateException("Coordinate 1 X or Y value not set");
    }

    if (Double.isNaN(x2) || Double.isNaN(y2)) {
      throw new IllegalStateException("Coordinate 2 X or Y value not set");
    }
  }


  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();
    
    return new XYLineAnnotation(x1, y1, x2, y2, style, color);
  }

  /**
   * Uses {@code Arrays.binarySearch()} two times to search the source array for the x1 and x2 date value. If found,
   * replaces the values in the builder with the found array index relative to the configured index range. The source
   * time values are assumed to be timestamps in milliseconds since the epoch start. It's also assumed these are in
   * ascending chronologic order. Failure to provide them in sorted order will result in undefined behavior as per
   * {@code Arrays.binarySearch()}.
   */
  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {
    // +1 as the binary search method end index is exclusive.
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1, (long) x1());

    if (xIndex >= 0) {
      x1((double) (xIndex - indexRangeStartIndex));
    }

    // +1 as the binary search method end index is exclusive.
    xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1, (long) x2());

    if (xIndex >= 0) {
      x2((double) (xIndex - indexRangeStartIndex));
    }
  }

}
