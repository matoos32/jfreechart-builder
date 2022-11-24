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

import java.awt.Image;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDataImageAnnotation;

/**
 * Builder for producing {@link XYDataImageAnnotation} objects.
 * <p>
 * <b>Note: Limited to working with {@link org.jfree.chart.axis.DateAxis} (i.e. with time gaps) mode
 * only. See related comments in mapXToTimeIndex(long[], int, int)</b>
 * 
 * @see XYDataImageBuilder#mapXToTimeIndex(long[], int, int)
 */
public class XYDataImageBuilder implements IXYAnnotationBuilder<XYDataImageBuilder> {

  // See XYDataImageAnnotation for this default
  private static final boolean DEFAULT_INCLUDE_IN_DATA_BOUNDS = false;

  private double x;
  private double y;
  private Image image;
  double width;
  private double height;
  private boolean includeInDataBounds;

  /**
   * Hidden constructor.
   */
  private XYDataImageBuilder() {
    x = Double.NaN;
    y = Double.NaN;
    image = null;
    width = Double.NaN;
    height = Double.NaN;
    includeInDataBounds = DEFAULT_INCLUDE_IN_DATA_BOUNDS;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYDataImageBuilder get() {
    return new XYDataImageBuilder();
  }

  public XYDataImageBuilder x(double x) {
    this.x = x;
    return this;
  }

  public double x() {
    return x;
  }

  public XYDataImageBuilder y(double y) {
    this.y = y;
    return this;
  }

  public double y() {
    return y;
  }

  public XYDataImageBuilder image(Image image) {
    this.image = image;
    return this;
  }

  public Image image() {
    return image;
  }

  public XYDataImageBuilder width(double w) {
    width = w;
    return this;
  }

  public double width() {
    return width;
  }

  public XYDataImageBuilder height(double h) {
    height = h;
    return this;
  }

  public double height() {
    return height;
  }
  
  public XYDataImageBuilder includeInDataBounds(boolean include) {
    includeInDataBounds = include;
    return this;
  }

  public boolean includeInDataBounds() {
    return includeInDataBounds;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalStateException("X or Y value not set");
    }

    if (image == null) {
      throw new IllegalStateException("Image not set");
    }
    
    if (Double.isNaN(width) || Double.isNaN(height)) {
      throw new IllegalStateException("Width or height not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    return new XYDataImageAnnotation(image, x, y, width, height, includeInDataBounds);
  }


  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {

    // Gapless XYDataImageAnnotation is not currently supported due to conversion of the width.
    //
    // It's believed we need to somehow map the width to a number of numeric indices but the
    // configured width in the time domain can be fractional in the integer index domain.
    //
    // Instead of providing a best fit guess that could leave users wanting, this capability is left
    // unimplemented.
    //
    // If one really needs this one can try extending this class and overriding the
    // mapXToTimeIndex() method to write their own logic.
    //
    // There's a potential code snippet below inspired from the other builders.
    
    /*
    // +1 as the binary search method end index is exclusive.
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
        (long) x());

    if (xIndex >= 0) {
      x((double) (xIndex - indexRangeStartIndex));
      
      // And now how many indices does the width span? All time spacing is squashed into sequential
      // evenly spaced indices ...
      width( width() / ??? );
    }
    */
  }
}
