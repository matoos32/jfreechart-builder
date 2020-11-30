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

import java.awt.Color;
import java.util.Objects;

import org.jfree.chart.renderer.xy.CandlestickRenderer;

import com.jfcbuilder.builders.types.BuilderConstants;

/**
 * Builder for creating configured CandlestickRenderer instances.
 */
public class CandlestickRendererBuilder {

  private Color upColor;
  private Color downColor;

  private CandlestickRendererBuilder() {
    upColor = BuilderConstants.DEFAULT_UP_COLOR;
    downColor = BuilderConstants.DEFAULT_DOWN_COLOR;
  }

  public static CandlestickRendererBuilder get() {
    return new CandlestickRendererBuilder();
  }

  public CandlestickRendererBuilder upColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    upColor = c;
    return this;
  }

  public Color upColor() {
    return upColor;
  }

  public CandlestickRendererBuilder downColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    downColor = c;
    return this;
  }

  public Color downColor() {
    return downColor;
  }

  public CandlestickRenderer build() {

    CandlestickRenderer renderer = new CandlestickRenderer();

    renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
    renderer.setUseOutlinePaint(true); // Make sure the desired outline paint actually gets used
    renderer.setSeriesOutlinePaint(0, BuilderConstants.DEFAULT_OUTLINE_COLOR);
    renderer.setUpPaint(upColor);
    renderer.setDownPaint(downColor);
    renderer.setDrawVolume(false);

    return renderer;
  }

}
