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

import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.util.XYCoordinateType;

/**
 * Builder for producing {@link XYTitleAnnotation} objects.
 */
public class XYTitleBuilder implements IXYAnnotationBuilder<XYTitleBuilder> {

  public static final double DEFAULT_X_COORD = 0.5;
  public static final double DEFAULT_Y_COORD = 1.0;
  public static final double DEFAULT_MAX_WIDTH = 0.0;
  public static final double DEFAULT_MAX_HEIGHT = 0.0;
  public static final RectangleAnchor DEFAULT_ANCHOR = RectangleAnchor.TOP;

  private double x;
  private double y;
  private double maxWidth;
  private double maxHeight;
  private Title title;
  private RectangleAnchor anchor;

  /**
   * Hidden constructor.
   */
  private XYTitleBuilder() {
    x = DEFAULT_X_COORD;
    y = DEFAULT_Y_COORD;
    maxWidth = DEFAULT_MAX_WIDTH;
    maxHeight = DEFAULT_MAX_HEIGHT;
    title = null;
    anchor = DEFAULT_ANCHOR;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYTitleBuilder get() {
    return new XYTitleBuilder();
  }

  /**
   * Sets the x-coordinate of the annotation.
   * <p>
   * JFreeChart {@link XYTitleAnnotation} hard-codes the coordinate type to
   * {@link XYCoordinateType#RELATIVE} therefore the X value being set must be in range (0.0, 1.0).
   * 
   * @param x The coordinate value to be set. Should be in range (0.0, 1.0)
   * @return Reference to this builder instance for method chaining
   */
  public XYTitleBuilder x(double x) {
    this.x = x;
    return this;
  }

  
  public double x() {
    return x;
  }

  /**
   * Sets the y-coordinate of the annotation.
   * <p>
   * JFreeChart {@link XYTitleAnnotation} hard-codes the coordinate type to
   * {@link XYCoordinateType#RELATIVE} therefore the Y value being set must be in range (0.0, 1.0).
   * 
   * @param y The coordinate value to be set. Should be in range (0.0, 1.0)
   * @return Reference to this builder instance for method chaining
   */
  public XYTitleBuilder y(double y) {
    this.y = y;
    return this;
  }

  public double y() {
    return y;
  }

  public XYTitleBuilder maxWidth(double w) {
    maxWidth = w;
    return this;
  }

  public double maxWidth() {
    return maxWidth;
  }

  public XYTitleBuilder maxHeight(double h) {
    maxHeight = h;
    return this;
  }

  public double maxHeight() {
    return maxHeight;
  }

  public XYTitleBuilder title(Title t) {
    title = t;
    return this;
  }

  public Title title() {
    return title;
  }

  public XYTitleBuilder anchor(RectangleAnchor a) {
    anchor = a;
    return this;
  }

  public RectangleAnchor anchor() {
    return anchor;
  }

  private void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalStateException("X or Y value not set");
    }

    if (Double.isNaN(maxWidth)) {
      throw new IllegalStateException("Max width not set");
    }

    if (Double.isNaN(maxHeight) || Double.isNaN(y)) {
      throw new IllegalStateException("Max height not set");
    }

    if (title == null) {
      throw new IllegalStateException("Title not set");
    }

    if (anchor == null) {
      throw new IllegalStateException("Anchor not set");
    }
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    return new XYTitleAnnotation(x, y, title, anchor);
  }

  @Override
  public void mapXToTimeIndex(long[] timeData, int indexRangeStartIndex, int indexRangeEndIndex) {
    // Explicitly do nothing as XYTitleAnnotation uses data space coordinates in range (0.0, 1.0)
    // due to coordinate type XYCoordinateType.RELATIVE
  }
}