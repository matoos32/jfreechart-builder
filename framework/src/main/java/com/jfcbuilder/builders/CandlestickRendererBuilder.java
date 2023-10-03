/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2023, by Matt E. and project contributors
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
import java.util.Objects;

import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.renderer.xy.CandlestickRenderer;

import com.jfcbuilder.types.BuilderConstants;

/**
 * Builder for creating configured CandlestickRenderer instances.
 */
public class CandlestickRendererBuilder {

  private Color upColor;
  private Color downColor;
  private XYToolTipGenerator toolTipGenerator;

  private CandlestickRendererBuilder() {
    upColor = BuilderConstants.DEFAULT_UP_COLOR;
    downColor = BuilderConstants.DEFAULT_DOWN_COLOR;
    toolTipGenerator = null;
  }

  /**
   * Factory method for obtaining new instances of this class.
   * 
   * @return New instance of this class
   */
  public static CandlestickRendererBuilder get() {
    return new CandlestickRendererBuilder();
  }

  /**
   * Sets the close-up candle color to be used when rendering candles.
   * 
   * @param c The {@link Color} to use
   * @return Reference to this builder for chaining method calls
   */
  public CandlestickRendererBuilder upColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    upColor = c;
    return this;
  }

  /**
   * Gets the configured close-up candle color.
   * 
   * @return The color
   */
  public Color upColor() {
    return upColor;
  }

  /**
   * Sets the close-down candle color to be used when rendering candles.
   * 
   * @param c The {@link Color} to use
   * @return Reference to this builder for chaining method calls
   */
  public CandlestickRendererBuilder downColor(Color c) {
    Objects.requireNonNull(c, "Color cannot be set to null");
    downColor = c;
    return this;
  }

  /**
   * Gets the configured close-down candle color.
   * 
   * @return The color
   */
  public Color downColor() {
    return downColor;
  }

  /**
   * Gets the configured tooltip generator.
   * 
   * @return The tooltip generator instance if one was set. Null otherwise.
   */
  public XYToolTipGenerator toolTipGenerator() {
    return toolTipGenerator;
  }

  /**
   * Sets the tooltip generator to use. If this method is not invoked the
   * {@link CandlestickRenderer} default is used.
   * 
   * @param toolTipGenerator The tooltip generator that should be used.
   */
  public CandlestickRendererBuilder toolTipGenerator(XYToolTipGenerator toolTipGenerator) {
    this.toolTipGenerator = toolTipGenerator;
    return this;
  }

  /**
   * Builds the renderer using all configured settings.
   * 
   * @return New instance of a CandlestickRenderer corresponding to all configured settings
   */
  public CandlestickRenderer build() {

    CandlestickRenderer renderer = new CandlestickRenderer();
    renderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_AVERAGE);
    renderer.setUseOutlinePaint(true); // Make sure the desired outline paint actually gets used
    renderer.setSeriesOutlinePaint(0, BuilderConstants.DEFAULT_OUTLINE_COLOR);
    renderer.setUpPaint(upColor);
    renderer.setDownPaint(downColor);
    renderer.setDrawVolume(false);
    renderer.setAutoWidthFactor(BuilderConstants.DEFAULT_BAR_WIDTH_PERCENT);

    if (toolTipGenerator != null) {
      renderer.setDefaultToolTipGenerator(toolTipGenerator);
    }

    return renderer;
  }

}
