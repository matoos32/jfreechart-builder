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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

import com.jfcbuilder.types.Orientation;

/**
 * A family of constants used throughout this builder framework.
 */
public abstract class BuilderConstants {

  /**
   * Stroke configured for drawing a solid line.
   */
  public static final Stroke SOLID_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND);

  /**
   * Stroke configured for drawing a thick solid line.
   */
  public static final Stroke THICK_SOLID_LINE = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND);
  
  /**
   * Stroke configured for drawing a thin solid line.
   */
  public static final Stroke THIN_SOLID_LINE = new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND);

  /**
   * BasicStroke dash pattern array.
   */
  private static final float dash_pattern[] = { 1.5f, 1.5f };
  
  /**
   * Stroke configured for drawing a dashed line.
   */
  public static final BasicStroke DASHED_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND, 10.0f, dash_pattern, 0.0f);

  /**
   * Stroke configured for drawing a thick dashed line.
   */
  public static final BasicStroke THICK_DASHED_LINE = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND, 10.0f, dash_pattern, 0.0f);
  
  /**
   * Stroke configured for drawing a thin dashed line.
   */
  public static final BasicStroke THIN_DASHED_LINE = new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND, 10.0f, dash_pattern, 0.0f);
  
  /**
   * A default font that can be used throughout the application.
   */
  public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 10);

  /**
   * A default plot weight that can be used throughout the application.
   */
  public static final int DEFAULT_PLOT_WEIGHT = 1;

  /**
   * A default plot grid-line color that can be used throughout the application.
   */
  public static final Color DEFAULT_GRIDLINE_PAINT = Color.LIGHT_GRAY;

  /**
   * A default color that can be used throughout the application to represent a price close up
   * event.
   */
  public static final Color DEFAULT_UP_COLOR = Color.WHITE;

  /**
   * A default color that can be used throughout the application to represent a price close down
   * event.
   */
  public static final Color DEFAULT_DOWN_COLOR = Color.RED;

  /**
   * A default color that can be used throughout the application to fill shapes.
   */
  public static final Color DEFAULT_FILL_COLOR = Color.WHITE;

  /**
   * A default color that can be used throughout the application for shape outlines.
   */
  public static final Color DEFAULT_OUTLINE_COLOR = Color.GRAY;

  /**
   * A default color that can be used throughout the application for drawing lines.
   */
  public static final Color DEFAULT_LINE_COLOR = Color.BLUE;

  /**
   * A default Stroke that can be used throughout the application for drawing lines.
   */
  public static final Stroke DEFAULT_LINE_STYLE = SOLID_LINE;

  /**
   * A reusable empty double value array to avoid creating many such instances.
   */
  public static final double[] EMPTY_SERIES_DATA = {};

  /**
   * A reusable empty milliseconds since epoch (long) value array to avoid creating many such
   * instances.
   */
  public static final long[] EMPTY_TIME_DATA = {};

  /**
   * A default orientation that can be used throughout the application for drawing various items.
   */
  public static final Orientation DEFAULT_ORIENTATION = Orientation.HORIZONTAL;

  /**
   * Whether or not to render time gaps by default in time series plots.
   */
  public static final boolean DEFAULT_SHOW_TIME_GAPS = true;

  /**
   * Percentage of total possible bar width to use for drawing XY bars and candlesticks. Use a large
   * value to create small spacing between bars and vice versa for creating large spacing.
   */
  public static final double DEFAULT_BAR_WIDTH_PERCENT = 0.95;
}
