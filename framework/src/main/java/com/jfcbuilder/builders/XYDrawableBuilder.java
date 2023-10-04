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

import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.ui.Drawable;

/**
 * Builder for producing {@link XYDrawableAnnotation} objects.
 */
public class XYDrawableBuilder implements IXYAnnotationBuilder<XYDrawableBuilder> {

  /**
   * The drawing scale factor to use if none is specified.
   */
  public static final double DEFAULT_DRAW_SCALE_FACTOR = 1.0;

  private double x;
  private double y;
  private double displayWidth;
  private double displayHeight;
  private double drawScaleFactor;
  private Drawable drawable;

  /**
   * Hidden constructor.
   */
  private XYDrawableBuilder() {
    x = Double.NaN;
    y = Double.NaN;
    displayWidth = Double.NaN;
    displayHeight = Double.NaN;
    drawScaleFactor = DEFAULT_DRAW_SCALE_FACTOR;
    drawable = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYDrawableBuilder get() {
    return new XYDrawableBuilder();
  }

  /**
   * Sets the drawable's x-coordinate.
   * 
   * @param x The coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder x(double x) {
    this.x = x;
    return this;
  }

  /**
   * Gets the x-coordinate that is set.
   * 
   * @return The coordinate value
   */
  public double x() {
    return x;
  }

  /**
   * Sets the drawable's y-coordinate.
   * 
   * @param y The coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder y(double y) {
    this.y = y;
    return this;
  }

  /**
   * Gets the y-coordinate that is set.
   * 
   * @return The coordinate value
   */
  public double y() {
    return y;
  }

  /**
   * Sets the width of the area into which to draw the {@link org.jfree.chart.ui.Drawable}.
   * 
   * @param w The width to be set (Java2D units)
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder displayWidth(double w) {
    displayWidth = w;
    return this;
  }

  /**
   * Gets the width of the area into which to draw the {@link org.jfree.chart.ui.Drawable}.
   * 
   * @return The width (Java2D units)
   */
  public double displayWidth() {
    return displayWidth;
  }

  /**
   * Sets the height of the area into which to draw the {@link org.jfree.chart.ui.Drawable}.
   * 
   * @param h The height to be set (Java2D units)
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder displayHeight(double h) {
    displayHeight = h;
    return this;
  }

  /**
   * Gets the height of the area into which to draw the {@link org.jfree.chart.ui.Drawable}.
   * 
   * @return The height (Java2D units)
   */
  public double displayHeight() {
    return displayHeight;
  }

  /**
   * Sets the {@link org.jfree.chart.ui.Drawable} object to be annotated.
   * 
   * @param d The {@link org.jfree.chart.ui.Drawable}
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder drawable(Drawable d) {
    drawable = d;
    return this;
  }

  /**
   * Gets the {@link org.jfree.chart.ui.Drawable} object to be annotated.
   * 
   * @return The {@link org.jfree.chart.ui.Drawable}
   */
  public Drawable drawable() {
    return drawable;
  }

  /**
   * Sets the draw scale factor to use.
   * 
   * @param f The scale factor
   * @return Reference to this builder instance for method chaining
   */
  public XYDrawableBuilder drawScaleFactor(double f) {
    drawScaleFactor = f;
    return this;
  }

  /**
   * Gets the draw scale factor to be used
   * 
   * @return The scale factor
   */
  public double drawScaleFactor() {
    return drawScaleFactor;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalStateException("X or Y value not set");
    }

    if (Double.isNaN(displayWidth)) {
      throw new IllegalStateException("Display width not set");
    }

    if (Double.isNaN(displayHeight)) {
      throw new IllegalStateException("Display height not set");
    }

    if (Double.isNaN(drawScaleFactor)) {
      throw new IllegalStateException("Draw scale factor not set");
    }

    if (drawable == null) {
      throw new IllegalStateException("Drawable item not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    return new XYDrawableAnnotation(x, y, displayWidth, displayHeight, drawScaleFactor, drawable);
  }

  /**
   * Uses {@code Arrays.binarySearch()} to search the source array for the date value. If found,
   * replaces that value in the builder with the found array index relative to the configured index
   * range. The source time values are assumed to be timestamps in milliseconds since the epoch
   * start. It's also assumed these are in ascending chronologic order. Failure to provide them in
   * sorted order will result in undefined behavior as per {@code Arrays.binarySearch()}.
   */
  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {
    // +1 as the binary search method end index is exclusive.
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
        (long) x());

    if (xIndex >= 0) {
      x((double) (xIndex - indexRangeStartIndex));
    }
  }
}
