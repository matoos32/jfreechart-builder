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
import java.awt.Stroke;

import org.jfree.chart.plot.ValueMarker;

import com.jfcbuilder.types.BuilderConstants;
import com.jfcbuilder.types.Orientation;

/**
 * Uses configured properties to Build a {@link ValueMarker} representing a plot overlay line.
 */
public class MarkerBuilder {

  private static final double DEFAULT_VALUE = 0.0;

  private Orientation orientation;
  private double value;
  private Color color;
  private Stroke style;

  /**
   * Hidden constructor.
   */
  private MarkerBuilder() {
    orientation = BuilderConstants.DEFAULT_ORIENTATION;
    value = DEFAULT_VALUE;
    color = BuilderConstants.DEFAULT_LINE_COLOR;
    style = BuilderConstants.DEFAULT_LINE_STYLE;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static MarkerBuilder get() {
    return new MarkerBuilder();
  }

  /**
   * Sets the orientation of the line to be drawn.
   * 
   * @param orientation The desired orientation of the line
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder orientation(Orientation orientation) {
    this.orientation = orientation;
    return this;
  }

  /**
   * Sets the orientation of the line to horizontal.
   * 
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder horizontal() {
    this.orientation = Orientation.HORIZONTAL;
    return this;
  }

  /**
   * Sets the orientation of the line to vertical.
   * 
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder vertical() {
    this.orientation = Orientation.VERTICAL;
    return this;
  }

  /**
   * Gets the currently configured line orientation
   * 
   * @return The configured orientation
   */
  public Orientation orientation() {
    return orientation;
  }

  /**
   * Sets the value at which the line show be drawn.
   * 
   * @param value The desired line value
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder at(double value) {
    this.value = value;
    return this;
  }

  /**
   * Sets the color to be used when drawing the line
   * 
   * @param color The desired color
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder color(Color color) {
    this.color = color;
    return this;
  }

  /**
   * Sets the line style ({@code Stroke}) to be used when drawing the line
   * 
   * @param style The desired line style
   * @return Reference to this builder for chaining method calls
   */
  public MarkerBuilder style(Stroke style) {
    this.style = style;
    return this;
  }

  /**
   * Builds a ValueMarker from the configured properties.
   * 
   * @return The new ValueMarker instance
   */
  public ValueMarker build() {
    ValueMarker marker = new ValueMarker(value);
    marker.setPaint(color);
    marker.setStroke(style);
    return marker;
  }

}
