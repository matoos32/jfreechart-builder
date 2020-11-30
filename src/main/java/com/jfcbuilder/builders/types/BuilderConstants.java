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

package com.jfcbuilder.builders.types;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

/**
 * A family of constants used throughout this builder framework.
 */
public abstract class BuilderConstants {

  public static final Stroke SOLID_LINE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
      BasicStroke.JOIN_ROUND);

  public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 10);

  public static final int DEFAULT_PLOT_WEIGHT = 1;

  public static final Color DEFAULT_UP_COLOR = Color.WHITE;
  public static final Color DEFAULT_DOWN_COLOR = Color.RED;

  public static final Color DEFAULT_FILL_COLOR = Color.WHITE;
  public static final Color DEFAULT_OUTLINE_COLOR = Color.GRAY;
  
  public static final Color DEFAULT_LINE_COLOR = Color.BLUE;
  public static final Stroke DEFAULT_LINE_STYLE = SOLID_LINE;
  
  public static final double[] EMPTY_SERIES_DATA = {};
  public static final long[] EMPTY_TIME_DATA = {};

  public static final Orientation DEFAULT_ORIENTATION = Orientation.HORIZONTAL;

}
