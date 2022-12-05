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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;

/**
 * Builder for producing {@link XYShapeAnnotation} objects.
 * <p>
 * <b>Note: Limited to working with {@link org.jfree.chart.axis.DateAxis} (i.e. with time gaps) mode
 * only. See related comments in mapXToTimeIndex(long[], int, int)</b>
 * 
 * @see XYShapeBuilder#mapXToTimeIndex(long[], int, int)
 */
public class XYShapeBuilder implements IXYAnnotationBuilder<XYShapeBuilder> {

  // See XYShapeAnnotation for these defaults

  /**
   * The default line color to use if none is specified.
   */
  public static final Color DEFAULT_LINE_COLOR = Color.BLACK;

  /**
   * The default line style to use if none is specified.
   */
  public static final Stroke DEFAULT_LINE_STYLE = new BasicStroke(1.0f);

  /**
   * The default fill color to use if none is specified.
   */
  public static final Color DEFAULT_FILL_COLOR = null; // None specified

  private Shape shape;
  private Stroke lineStyle;
  private Paint lineColor;
  private Paint fillColor;

  /**
   * Hidden constructor.
   */
  private XYShapeBuilder() {
    shape = null;
    lineStyle = DEFAULT_LINE_STYLE;
    lineColor = DEFAULT_LINE_COLOR;
    fillColor = DEFAULT_FILL_COLOR;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYShapeBuilder get() {
    return new XYShapeBuilder();
  }

  /**
   * Gets the {@link java.awt.Shape} to be annotated.
   * 
   * @return The shape
   */
  public Shape shape() {
    return shape;
  }

  /**
   * Sets the {@link java.awt.Shape} to be annotated.
   * 
   * @param shape The {@link java.awt.Shape} to be set
   * @return Reference to this builder instance for method chaining
   */
  public XYShapeBuilder shape(Shape shape) {
    this.shape = shape;
    return this;
  }

  /**
   * Gets the line style of the shape.
   * 
   * @return The line style
   */
  public Stroke lineStyle() {
    return lineStyle;
  }

  /**
   * Sets the line style of the shape.
   * 
   * @param style The style to be set
   * @return Reference to this builder instance for method chaining
   */
  public XYShapeBuilder lineStyle(Stroke style) {
    lineStyle = style;
    return this;
  }

  /**
   * Gets the line color of the shape.
   * 
   * @return The color
   */
  public Paint lineColor() {
    return lineColor;
  }

  /**
   * Sets the line color of the shape.
   * 
   * @param color The color to be set
   * @return Reference to this builder instance for method chaining
   */
  public XYShapeBuilder lineColor(Paint color) {
    lineColor = color;
    return this;
  }

  /**
   * Gets the fill color of the shape.
   * 
   * @return The color
   */
  public Paint fillColor() {
    return fillColor;
  }

  /**
   * Sets the fill color of the shape.
   * 
   * @param color The color to be set
   * @return Reference to this builder instance for method chaining
   */
  public XYShapeBuilder fillColor(Paint color) {
    fillColor = color;
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {
    if (shape == null) {
      throw new IllegalStateException("Shape not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    return new XYShapeAnnotation(shape, lineStyle, lineColor, fillColor);
  }

  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {

    // Gapless shape annotations are not currently supported due to multiple complexities.
    //
    // We need to somehow map the java.awt.Shape's x-coordinate(s) to time indices, but Shape is an
    // interface and concrete classes can have any number of x-coordinates passed-in and in any
    // number of ways.
    //
    // The interface provides no means to get x-coordinates polymorphically. So it requires specific
    // logic for 30+ different known implementing classes:
    //
    // https://docs.oracle.com/javase/8/docs/api/java/awt/Shape.html
    //
    // That is a big effort possibly with high maintenance burden.

    /*
     * if(shape == null) { return; }
     * 
     * // +1 as the binary search method end index is exclusive. int xIndex =
     * Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1, (long) x());
     * 
     * if (xIndex >= 0) { x((double) (xIndex - indexRangeStartIndex)); }
     */

  }

}
