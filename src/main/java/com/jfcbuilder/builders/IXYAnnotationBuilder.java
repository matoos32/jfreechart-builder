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
import org.jfree.chart.ui.TextAnchor;

/**
 * Interface for all XYAnnotation builders. Uses a generic to specify the concrete implementation of
 * the interface as the return type of setter methods. This is done to support method chaining on
 * the same builder instance. In this framework there can be different builder types that have
 * specialized methods. If the return types were made to be this interface instead of the concrete
 * class then those specialized methods of the classes not defined in the interface would be hidden
 * by only having access to the interface.
 * 
 * @param <T> The method chaining return type, which must be the type of the builder implementing
 *        this interface.
 */
interface IXYAnnotationBuilder<T extends IXYAnnotationBuilder<T>> {

  /**
   * Sets the color used to draw the annotation.
   * 
   * @param color The color to be used when drawing the annotation
   * @return Reference to this builder for chaining method calls
   */
  T color(Color color);

  /**
   * Sets the alignment of the annotation relative to the XY anchor point.
   * 
   * @param alignment The TextAnchor specifying the alignment
   * @return Reference to this builder for chaining method calls
   */
  T textAlign(TextAnchor alignment);

  /**
   * Sets the annotation text to be drawn
   * 
   * @param text The text to be drawn
   * @return Reference to this builder for chaining method calls
   */
  T text(String text);

  /**
   * Pads the right side of the text with a number of spaces. Use this to offset the text a desired
   * distance from the anchored XY coordinate.
   * 
   * @param n Number of space characters to insert at right of text
   * @return Reference to this builder instance for method chaining
   */
  T textPaddingRight(int n);

  /**
   * Pads the left side of the text with a number of spaces. Use this to offset the text a desired
   * distance from the anchored XY coordinate.
   * 
   * @param n Number of space characters to insert at left of text
   * @return Reference to this builder instance for method chaining
   */
  T textPaddingLeft(int n);

  /**
   * Sets the rotational orientation angle of the annotation in degrees.
   * 
   * @param degrees The rotation angle of the annotation
   * @return Reference to this builder for chaining method calls
   */
  T angle(double degrees);

  /**
   * Sets the x-coordinate of the annotation.
   * 
   * @param x The x-coordinate
   * @return Reference to this builder for chaining method calls
   */
  T x(double x);

  /**
   * Gets the x-coordinate of the annotation.
   * 
   * @return The x-coordinate
   */
  double x();
  
  /**
   * Sets the y-coordinate of the annotation.
   * 
   * @param y The y-coordinate
   * @return Reference to this builder for chaining method calls
   */
  T y(double y);

  /**
   * Gets the y-coordinate of the annotation.
   * 
   * @return The y-coordinate
   */
  double y();

  /**
   * Builds the XYAnnotation from all configured data and properties.
   * 
   * @return New instance of an XYAnnotation corresponding to all configured data and properties
   * @throws IllegalStateException If the builder is missing properties when {@code build()} is
   *         called
   */
  XYAnnotation build() throws IllegalStateException;
}
