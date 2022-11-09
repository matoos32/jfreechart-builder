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
import java.util.Arrays;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.ui.TextAnchor;

/**
 * Builder for producing {@link XYTextAnnotation} objects.
 */
public class XYTextBuilder implements IXYAnnotationBuilder<XYTextBuilder> {

  private XYTextAnnotationElements elems;

  /**
   * Hidden constructor.
   */
  private XYTextBuilder() {
    elems = new XYTextAnnotationElements();
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static XYTextBuilder get() {
    return new XYTextBuilder();
  }

  public XYTextBuilder x(double x) {
    elems.x(x);
    return this;
  }

  public double x() {
    return elems.x();
  }
  
  public XYTextBuilder y(double y) {
    elems.y(y);
    return this;
  }

  public double y() {
    return elems.y();
  }
  
  public XYTextBuilder angle(double degrees) {
    elems.angle(degrees);
    return this;
  }

  public XYTextBuilder text(String text) {
    elems.text(text);
    return this;
  }

  public XYTextBuilder textAlign(TextAnchor alignment) {
    elems.textAlign(alignment);
    return this;
  }

  public XYTextBuilder color(Color color) {
    elems.color(color);
    return this;
  }

  public XYTextBuilder textPaddingLeft(int n) {
    elems.textPaddingLeft(n);
    return this;
  }

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

    XYTextAnnotation annotation = new XYTextAnnotation(elems.paddedText(), elems.x(), elems.y());
    annotation.setTextAnchor(elems.textAlignment());
    annotation.setRotationAngle(Math.toRadians(elems.angle()));
    annotation.setPaint(elems.color());
    return annotation;
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
    int xIndex = Arrays.binarySearch(timeData, indexRangeStartIndex, indexRangeEndIndex + 1, (long) x());

    if (xIndex >= 0) {
      x((double) (xIndex - indexRangeStartIndex));
    }
  }
}
