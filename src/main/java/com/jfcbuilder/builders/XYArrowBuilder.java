/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2020, by Matt E.
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

import java.awt.Color;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.ui.TextAnchor;

/**
 * Builder for producing XYPointerAnnotation (arrow) annotations.
 */
public class XYArrowBuilder implements IXYAnnotationBuilder<XYArrowBuilder> {

  /**
   * radial length of the arrow.
   */
  private static final double DEFAULT_ARROW_LENGTH = 20.0;
  
  /**
   * Radial spacing the tip of the arrow will be away from the anchored XY coordinate.
   */
  private static final double DEFAULT_XY_COORD_SPACING = 5.0;

  private XYAnnotationElements elems;

  /**
   * Hidden constructor.
   */
  private XYArrowBuilder() {
    elems = new XYAnnotationElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYArrowBuilder instance() {
    return new XYArrowBuilder();
  }

  @Override
  public XYArrowBuilder x(double x) {
    elems.x(x);
    return this;
  }

  @Override
  public XYArrowBuilder y(double y) {
    elems.y(y);
    return this;
  }

  @Override
  public XYArrowBuilder angle(double degrees) {
    elems.angle(degrees);
    return this;
  }

  @Override
  public XYArrowBuilder text(String text) {
    elems.text(text);
    return this;
  }

  @Override
  public XYArrowBuilder textPaddingLeft(int n) {
    elems.textPaddingLeft(n);
    return this;
  }

  @Override
  public XYArrowBuilder textPaddingRight(int n) {
    elems.textPaddingRight(n);
    return this;
  }
  
  @Override
  public XYArrowBuilder textAlign(TextAnchor alignment) {
    elems.textAlign(alignment);
    return this;
  }

  @Override
  public XYArrowBuilder color(Color color) {
    elems.color(color);
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {
    elems.checkBuildPreconditions();
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    XYPointerAnnotation arrow = new XYPointerAnnotation(elems.paddedText(), elems.x(), elems.y(),
        Math.toRadians(elems.angle()));
    arrow.setTextAnchor(elems.textAlignment());
    arrow.setBaseRadius(DEFAULT_ARROW_LENGTH);
    arrow.setTipRadius(DEFAULT_XY_COORD_SPACING);
    arrow.setArrowPaint(elems.color());
    return arrow;
  }

}
