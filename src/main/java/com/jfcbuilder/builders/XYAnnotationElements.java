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
import java.util.Collections;

import org.jfree.chart.ui.TextAnchor;

import com.jfcbuilder.builders.types.BuilderConstants;

/**
 * Helper class for storing and accessing properties common to different kinds of XYAnnotation
 * builders. Intended for use in composition-type implementations.
 */
public class XYAnnotationElements {

  private static final String DEFAULT_TEXT = "";
  private static final int DEFAULT_TEXT_PADDING = 0;
  protected static final double DEFAULT_ANGLE = 0.0;
  protected static final TextAnchor DEFAULT_TEXT_ALIGNMENT = TextAnchor.BASELINE_RIGHT;

  protected double x;
  protected double y;
  protected double angle;
  protected String text;
  private int textPaddingLeft;
  private int textPaddingRight;
  protected TextAnchor textAlign;
  protected Color color;

  /**
   * Constructor.
   */
  public XYAnnotationElements() {
    x = Double.NaN;
    y = Double.NaN;
    angle = DEFAULT_ANGLE;
    text = DEFAULT_TEXT;
    textPaddingLeft = DEFAULT_TEXT_PADDING;
    textPaddingRight = DEFAULT_TEXT_PADDING;
    textAlign = DEFAULT_TEXT_ALIGNMENT;
    color = BuilderConstants.DEFAULT_LINE_COLOR;
  }

  /**
   * Sets the x-coordinate of the annotation.
   * 
   * @param x The x-coordinate
   */
  public void x(double x) {
    this.x = x;
  }
  
  /**
   * Gets the x-coordinate of the annotation.
   * 
   * @return The x-coordinate
   */
  public double x() {
    return x;
  }

  /**
   * Sets the y-coordinate of the annotation.
   * 
   * @param y The y-coordinate
   */
  public void y(double y) {
    this.y = y;
  }

  /**
   * Gets the y-coordinate of the annotation.
   * 
   * @return The y-coordinate
   */
  public double y() {
    return y;
  }
  
  /**
   * Sets the angular orientation of the annotation in degrees.
   * 
   * @param degrees The annotation orientation angle
   */
  public void angle(double degrees) {
    angle = Double.isNaN(degrees) ? DEFAULT_ANGLE : degrees;
  }

  /**
   * Gets the angular orientation of the annotation in degrees.
   * 
   * @return The annotation orientation angle
   */
  public double angle() {
    return angle;
  }
  
  /**
   * Sets the annotation text.
   * 
   * @param text The text to be set
   */
  public void text(String text) {
    this.text = text == null ? DEFAULT_TEXT : text;
  }

  /**
   * Gets the annotation text.
   * 
   * @return The annotation's text
   */
  public String text() {
    return text;
  }
  
  /**
   * Gets the number of padding spaces to left side of the text.
   * 
   * @return The number of left side padding spaces
   */
  public void textPaddingLeft(int n) {
    textPaddingLeft = (n < 0) ? 0 : n;
  }
  
  /**
   * Sets the number of padding spaces to left side of the text. Use this to offset the text a
   * desired distance from the anchored XY coordinate.
   * 
   * @param n Number of space characters to insert at left of text
   */
  public int textPaddingLeft() {
    return textPaddingLeft;
  }

  /**
   * Sets the number of padding spaces to right side of the text. Use this to offset the text a
   * desired distance from the anchored XY coordinate.
   * 
   * @param n Number of space characters to insert at right of text
   */
  public void textPaddingRight(int n) {
    textPaddingRight = (n < 0) ? 0 : n;
  }
  
  /**
   * Gets the number of padding spaces to right side of the text.
   * 
   * @return The number of right side padding spaces
   */
  public int textPaddingRight() {
    return textPaddingRight;
  }
  
  /**
   * Sets the annotation's text alignment relative to the anchored XY coordinate.
   * 
   * @param alignment The annotation's text alignment
   */
  public void textAlign(TextAnchor alignment) {
    this.textAlign = alignment == null ? DEFAULT_TEXT_ALIGNMENT : alignment;
  }
  
  /**
   * Gets the annotation's text alignment relative to the anchored XY coordinate.
   * 
   * @return The annotation's text alignment
   */
  public TextAnchor textAlignment() {
    return textAlign;
  }

  /**
   * Sets the annotation's color.
   * 
   * @param color The color to be set
   */
  public void color(Color color) {
    this.color = color == null ? BuilderConstants.DEFAULT_LINE_COLOR : color;
  }

  /**
   * Gets the color of the annotation
   * 
   * @return The annotation's color
   */
  public Color color() {
    return color;
  }

  /**
   * Checks to see if all preconditions for building the annotation are satisfied and throws an
   * exception if not.
   * 
   * @throws IllegalStateException If x or y have not been configured.
   */
  public void checkBuildPreconditions() throws IllegalStateException {

    if (Double.isNaN(x) || Double.isNaN(y)) {
      throw new IllegalStateException("X or Y value not set");
    }
  }
  
  /**
   * Helper method to build the left/right padded text to be displayed.
   * 
   * @return The text including the configured left and right padding spaces
   */
  public String paddedText() {

    // TODO: Can and should this be made more efficient? Strings are accumulated with +=
    // would StringBuilder be better for such a low number of +=? How does this
    // fare for huge numbers of annotations and/or charts being generated in bulk?

    String label = "";

    if (textPaddingLeft > 0) {
      label += String.join("", Collections.nCopies(textPaddingLeft, " "));
    }

    label += text;

    if (textPaddingRight > 0) {
      label += String.join("", Collections.nCopies(textPaddingRight, " "));
    }
    return label;
  }

}