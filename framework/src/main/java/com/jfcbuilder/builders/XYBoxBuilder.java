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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYBoxAnnotation;

/**
 * Builder for producing {@link XYBoxAnnotation} objects.
 */
public class XYBoxBuilder implements IXYAnnotationBuilder<XYBoxBuilder> {

  // See XYBoxAnnotation for source of these defaults (they're hard-coded, no constants).

  /**
   * The outline color to use if none is specified.
   */
  public static final Color DEFAULT_OUTLINE_COLOR = Color.BLACK;

  /**
   * The outline style to use if none is specified.
   */
  public static final Stroke DEFAULT_OUTLINE_STYLE = new BasicStroke(1.0f);

  private double x1;
  private double y1;
  private double x2;
  private double y2;
  private Stroke outlineStyle;
  private Paint outlineColor;
  private Paint fillColor;

  /**
   * Hidden constructor.
   */
  private XYBoxBuilder() {
    x1 = Double.NaN;
    y1 = Double.NaN;
    x2 = Double.NaN;
    y2 = Double.NaN;
    outlineStyle = null;
    outlineColor = null;
    fillColor = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYBoxBuilder get() {
    return new XYBoxBuilder();
  }

  /**
   * Gets the x-axis data coordinate of the first corner of the box.
   * 
   * @return The data coordinate value
   */
  public double x1() {
    return x1;
  }

  /**
   * Sets the x-axis data coordinate of the first corner of the box.
   * 
   * @param x1 The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder x1(double x1) {
    this.x1 = x1;
    return this;
  }

  /**
   * Gets the y-axis data coordinate of the first corner of the box.
   * 
   * @return The data coordinate value
   */
  public double y1() {
    return y1;
  }

  /**
   * Sets the y-axis data coordinate of the first corner of the box.
   * 
   * @param y1 The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder y1(double y1) {
    this.y1 = y1;
    return this;
  }

  /**
   * Gets the x-axis data coordinate of the second corner of the box.
   * 
   * @return The data coordinate value
   */
  public double x2() {
    return x2;
  }

  /**
   * Sets the x-axis data coordinate of the second corner of the box.
   * 
   * @param x2 The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder x2(double x2) {
    this.x2 = x2;
    return this;
  }

  /**
   * Gets the y-axis data coordinate of the second corner of the box.
   * 
   * @return The data coordinate value
   */
  public double y2() {
    return y2;
  }

  /**
   * Sets the y-axis data coordinate of the second corner of the box.
   * 
   * @param y2 The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder y2(double y2) {
    this.y2 = y2;
    return this;
  }

  /**
   * Gets the outline style of the box.
   * 
   * @return The outline's style
   */
  public Stroke outlineStyle() {
    return outlineStyle;
  }

  /**
   * Sets outline style of the box.
   * 
   * @param outlineStyle The style to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder outlineStyle(Stroke outlineStyle) {
    this.outlineStyle = outlineStyle;
    return this;
  }

  /**
   * Gets the outline color of the box.
   * 
   * @return The color
   */
  public Paint outlineColor() {
    return outlineColor;
  }

  /**
   * Sets the outline color of the box.
   * 
   * @param outlineColor The color to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder outlineColor(Paint outlineColor) {
    this.outlineColor = outlineColor;
    return this;
  }

  /**
   * Gets the fill color of the box.
   * 
   * @return The fill color
   */
  public Paint fillColor() {
    return fillColor;
  }

  /**
   * Sets the fill color of the box.
   * 
   * @param fillColor The color to set
   * @return Reference to this builder instance for method chaining
   */
  public XYBoxBuilder fillColor(Paint fillColor) {
    this.fillColor = fillColor;
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

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

    double lowerX, lowerY, upperX, upperY;
    
    // XYBoxAnnotation requires its (x0,y0) to be the lower coordinate and (x1,y1) to be the upper.
    //
    // As a convenience sort this out for the class client ...
    //
    // Based on the configured values of x1, y1, x2, y2 the transformations here can be:
    //
    // With points:
    //
    //   top left    | top right
    //   ------------|-------------
    //   bottom left | bottom right
    //
    // Idempotent:
    //        | x2y2            | x2y2
    //   -----|-----  ==>  -----|-----
    //   x1y1 |            x1y1 |
    //
    // Swap the y-values:
    //   x1y2 |                 | x2y2
    //   -----|-----  ==>  -----|-----
    //        | x2y1       x1y1 |
    //
    // Swap the x-values:
    //        | x1y2            | x2y2
    //   -----|-----  ==>  -----|-----
    //   x2y1 |            x1y1 |
    //
    // Swap both x and y values:
    //   x2y1 |                 | x2y2
    //   -----|-----  ==>  -----|-----
    //        | x1y2       x1y1 |
    
    if(y2 > y1) {
      upperY = y2;
      lowerY = y1;
    } else {
      upperY = y1;
      lowerY = y2;
    }
    
    if(x2 > x1) {
      upperX = x2;
      lowerX = x1;
    } else {
      upperX = x1;
      lowerX = x2;
    }

    if (outlineStyle == null && outlineColor == null && fillColor == null) {
      return new XYBoxAnnotation(lowerX, lowerY, upperX, upperY);
    }

    Stroke theOutlineStyle = (outlineStyle != null) ? outlineStyle : DEFAULT_OUTLINE_STYLE;
    Paint theOutlineColor = (outlineColor != null) ? outlineColor : DEFAULT_OUTLINE_COLOR;

    // A null fill color is accepted
    return new XYBoxAnnotation(lowerX, lowerY, upperX, upperY, theOutlineStyle, theOutlineColor,
        fillColor);
  }

  /**
   * Uses {@code Arrays.binarySearch()} two times to search the source array for the x1 and x2 date
   * value. If found, replaces the values in the builder with the found array index relative to the
   * configured index range. The source time values are assumed to be timestamps in milliseconds
   * since the epoch start. It's also assumed these are in ascending chronologic order. Failure to
   * provide them in sorted order will result in undefined behavior as per
   * {@code Arrays.binarySearch()}.
   */
  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {
    // +1 as the binary search method end index is exclusive.
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
        (long) x1());

    if (xIndex >= 0) {
      x1((double) (xIndex - indexRangeStartIndex));
    }

    // +1 as the binary search method end index is exclusive.
    xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
        (long) x2());

    if (xIndex >= 0) {
      x2((double) (xIndex - indexRangeStartIndex));
    }
  }

}
