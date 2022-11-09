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

package com.jfcbuilder.builders;

import java.awt.Color;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.ui.TextAnchor;

/**
 * Builder for producing XYTextAnnotation instances.
 */
public class XYTextBuilder implements IXYAnnotationBuilder<XYTextBuilder> {

  private XYAnnotationElements elems;

  /**
   * Hidden constructor.
   */
  private XYTextBuilder() {
    elems = new XYAnnotationElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYTextBuilder get() {
    return new XYTextBuilder();
  }

  @Override
  public XYTextBuilder x(double x) {
    elems.x(x);
    return this;
  }

  @Override
  public double x() {
    return elems.x();
  }
  
  @Override
  public XYTextBuilder y(double y) {
    elems.y(y);
    return this;
  }

  @Override
  public double y() {
    return elems.y();
  }
  
  @Override
  public XYTextBuilder angle(double degrees) {
    elems.angle(degrees);
    return this;
  }

  @Override
  public XYTextBuilder text(String text) {
    elems.text(text);
    return this;
  }

  @Override
  public XYTextBuilder textAlign(TextAnchor alignment) {
    elems.textAlign(alignment);
    return this;
  }

  @Override
  public XYTextBuilder color(Color color) {
    elems.color(color);
    return this;
  }

  @Override
  public XYTextBuilder textPaddingLeft(int n) {
    elems.textPaddingLeft(n);
    return this;
  }

  @Override
  public XYTextBuilder textPaddingRight(int n) {
    elems.textPaddingRight(n);
    return this;
  }

  private void checkBuildPreconditions() throws IllegalStateException {
    elems.checkBuildPreconditions();
  }

  @Override
  public XYAnnotation build() throws IllegalStateException {

    checkBuildPreconditions();

    XYTextAnnotation txt = new XYTextAnnotation(elems.paddedText(), elems.x(), elems.y());
    txt.setTextAnchor(elems.textAlignment());
    txt.setRotationAngle(Math.toRadians(elems.angle()));
    txt.setPaint(elems.color());
    return txt;
  }

}
