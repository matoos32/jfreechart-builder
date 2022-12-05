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
import java.awt.Stroke;
import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPolygonAnnotation;

/**
 * Builder for producing {@link XYPolygonAnnotation} objects.
 */
public class XYPolygonBuilder implements IXYAnnotationBuilder<XYPolygonBuilder> {

  // See XYPolygonAnnotation for source of these defaults (they're hard-coded, no constants).

  /**
   * The default outline color to use if none is specified.
   */
  public static final Color DEFAULT_OUTLINE_COLOR = Color.BLACK;

  /**
   * The default outline style to use if none is specified.
   */
  public static final Stroke DEFAULT_OUTLINE_STYLE = new BasicStroke(1.0f);

  private double[] polygon;
  private Stroke outlineStyle;
  private Paint outlineColor;
  private Paint fillColor;

  /**
   * Hidden constructor.
   */
  private XYPolygonBuilder() {
    polygon = null;
    outlineStyle = DEFAULT_OUTLINE_STYLE;
    outlineColor = DEFAULT_OUTLINE_COLOR;
    fillColor = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYPolygonBuilder get() {
    return new XYPolygonBuilder();
  }

  /**
   * Gets the copy of the polygon array, which was possibly mutated by {@code mapXToTimeIndex()}.
   * 
   * @return The serialized polygon (x,y) pairs.
   */
  public double[] polygon() {
    return polygon;
  }

  /**
   * Sets the polygon array by storying a copy of the supplied array.
   * <p>
   * The copy is because {@code mapXToTimeIndex()} will mutate the array.
   * 
   * @param polygon The serialized polygon (x,y) pairs to be copied.
   * @return Reference to this builder instance for method chaining
   */
  public XYPolygonBuilder polygon(double[] polygon) {
    this.polygon = (polygon != null) ? Arrays.copyOf(polygon, polygon.length) : null;
    return this;
  }

  /**
   * Gets the polygon outline style.
   * 
   * @return The outline style
   */
  public Stroke outlineStyle() {
    return outlineStyle;
  }

  /**
   * Sets the polygon outline style.
   * 
   * @param outlineStyle The style to set
   * @return Reference to this builder instance for method chaining
   */
  public XYPolygonBuilder outlineStyle(Stroke outlineStyle) {
    this.outlineStyle = outlineStyle;
    return this;
  }

  /**
   * Gets the color of the polygon outline.
   * 
   * @return The color
   */
  public Paint outlineColor() {
    return outlineColor;
  }

  /**
   * Sets the color of the polygon outline.
   * 
   * @param outlineColor The color to set
   * @return Reference to this builder instance for method chaining
   */
  public XYPolygonBuilder outlineColor(Paint outlineColor) {
    this.outlineColor = outlineColor;
    return this;
  }

  /**
   * Gets the polygon fill color.
   * 
   * @return The fill color
   */
  public Paint fillColor() {
    return fillColor;
  }

  /**
   * Sets the polygon fill color.
   * 
   * @param fillColor The color to set
   * @return Reference to this builder instance for method chaining
   */
  public XYPolygonBuilder fillColor(Paint fillColor) {
    this.fillColor = fillColor;
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (polygon == null) {
      throw new IllegalStateException("Polygon not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    if (outlineStyle == null && outlineColor == null && fillColor == null) {
      return new XYPolygonAnnotation(polygon);
    }

    Stroke theOutlineStyle = (outlineStyle != null) ? outlineStyle : DEFAULT_OUTLINE_STYLE;
    Paint theOutlineColor = (outlineColor != null) ? outlineColor : DEFAULT_OUTLINE_COLOR;

    // A null fill color is accepted
    return new XYPolygonAnnotation(polygon, theOutlineStyle, theOutlineColor, fillColor);
  }

  /**
   * Uses {@code Arrays.binarySearch()} for each polygon vertex to search the source array for the X
   * date values. If found, replaces the values in the builder with the found array index relative
   * to the configured index range. The source time values are assumed to be timestamps in
   * milliseconds since the epoch start. It's also assumed these are in ascending chronologic order.
   * Failure to provide them in sorted order will result in undefined behavior as per
   * {@code Arrays.binarySearch()}.
   */
  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {

    // x is at even indices
    // y is at odd indices

    if (polygon != null) {
      for (int i = 0; (i < polygon.length); i++) {
        if (i % 2 == 0) {
          // +1 as the binary search method end index is exclusive.
          int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1,
              (long) polygon[i]);

          if (xIndex >= 0) {
            // Hopefully always here.
            polygon[i] = (double) (xIndex - indexRangeStartIndex);
          }
        }
      }
    }
  }
}
