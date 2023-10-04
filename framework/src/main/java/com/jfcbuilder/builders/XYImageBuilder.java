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

import java.awt.Image;
import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.ui.RectangleAnchor;

/**
 * Builder for producing {@link XYImageAnnotation} objects.
 */
public class XYImageBuilder implements IXYAnnotationBuilder<XYImageBuilder> {

  // See XYImageAnnotation for this default
  private static final RectangleAnchor DEFAULT_ANCHOR = RectangleAnchor.CENTER;

  private double x;
  private double y;
  private Image image;
  private RectangleAnchor anchor;

  /**
   * Hidden constructor.
   */
  private XYImageBuilder() {
    x = Double.NaN;
    y = Double.NaN;
    image = null;
    anchor = DEFAULT_ANCHOR;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYImageBuilder get() {
    return new XYImageBuilder();
  }

  /**
   * Sets the image's x-axis data coordinate.
   * 
   * @param x The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYImageBuilder x(double x) {
    this.x = x;
    return this;
  }

  /**
   * Gets the x-axis data coordinate that is set.
   * 
   * @return The data coordinate value
   */
  public double x() {
    return x;
  }

  /**
   * Sets the image's y-axis data coordinate.
   * 
   * @param y The data coordinate to set
   * @return Reference to this builder instance for method chaining
   */
  public XYImageBuilder y(double y) {
    this.y = y;
    return this;
  }

  /**
   * Gets the y-axis data coordinate that is set.
   * 
   * @return The data coordinate value
   */
  public double y() {
    return y;
  }

  /**
   * Sets the {@link java.awt.Image} to be annotated.
   * 
   * @param image The image object to annotate
   * @return Reference to this builder instance for method chaining
   */
  public XYImageBuilder image(Image image) {
    this.image = image;
    return this;
  }

  /**
   * Gets the {@link java.awt.Image} to be annotated.
   * 
   * @return The image object to annotate
   */
  public Image image() {
    return image;
  }

  /**
   * Sets the image rectangle's position anchor
   * 
   * @param anchor The anchor to be set
   * @return Reference to this builder instance for method chaining
   * @see org.jfree.chart.annotations.XYImageAnnotation#getImageAnchor()
   */
  public XYImageBuilder anchor(RectangleAnchor anchor) {
    this.anchor = anchor;
    return this;
  }

  /**
   * Gets the image rectangle's position anchor
   * 
   * @return The anchor
   * @see org.jfree.chart.annotations.XYImageAnnotation#getImageAnchor()
   */
  public RectangleAnchor anchor() {
    return anchor;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalStateException("X or Y value not set");
    }

    if (image == null) {
      throw new IllegalStateException("Image not set");
    }

    if (anchor == null) {
      throw new IllegalStateException("Anchor not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    return new XYImageAnnotation(x, y, image, anchor);
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
