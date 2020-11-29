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

package com.jfcbuilder.builders.renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Objects;

import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import com.jfcbuilder.builders.types.BuilderConstants;

/**
 * Builder for creating configured XYBarRenderer instances.
 */
public class XYBarRendererBuilder {

  private static final BasicStroke DEFAULT_OUTLINE_STROKE = new BasicStroke(1.0f);

  private Color fillColor;
  private Color outlineColor;

  private XYBarRendererBuilder() {
    fillColor = BuilderConstants.DEFAULT_UP_COLOR;
    outlineColor = BuilderConstants.DEFAULT_FILL_COLOR;
  }

  public static XYBarRendererBuilder get() {
    return new XYBarRendererBuilder();
  }

  public XYBarRendererBuilder fillColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    fillColor = c;
    return this;
  }

  public Color fillColor() {
    return fillColor;
  }

  public XYBarRendererBuilder outlineColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    outlineColor = c;
    return this;
  }

  public Color outlineColor() {
    return outlineColor;
  }

  public XYBarRenderer build() {
    
    XYBarRenderer renderer = new XYBarRenderer();
    renderer = new XYBarRenderer();
    renderer.setBarPainter(new StandardXYBarPainter());
    renderer.setSeriesPaint(0, fillColor(), false);
    renderer.setSeriesOutlinePaint(0, outlineColor(), false);
    renderer.setSeriesOutlineStroke(0, DEFAULT_OUTLINE_STROKE, false);
    renderer.setDrawBarOutline(true);
    renderer.setShadowVisible(false);
    return renderer;
  }

}
