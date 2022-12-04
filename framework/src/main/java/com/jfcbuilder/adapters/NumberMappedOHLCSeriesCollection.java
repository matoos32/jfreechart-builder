/*
 * jfreechart-builder: a builder pattern module for working with the jfreechart library
 * 
 * (C) Copyright 2022, by Matt E. and project contributors
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

package com.jfcbuilder.adapters;

import java.util.Objects;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.OHLCDataset;

/**
 * Wrapper for OHLCSeriesCollection to map timestamp values to x-axis numeric indexes.
 */
public class NumberMappedOHLCSeriesCollection implements OHLCDataset, INumberMappedCollection {

  private OHLCSeriesCollection collection;
  
  /**
   * Constructor
   * 
   * @param collection The collection to be wrapped.
   */
  public NumberMappedOHLCSeriesCollection(OHLCSeriesCollection collection) {
    Objects.nonNull(collection);
    this.collection = collection;
    this.collection.setXPosition(TimePeriodAnchor.START);
  }

  @Override
  public double getXValue(int series, int item) {
    if ((item < 0) || (item >= collection.getItemCount(series))) {
      return Double.NaN;
    }
    return item;
  }

  @Override
  public int getSeriesCount() {
    return collection.getSeriesCount();
  }

  @Override
  public Comparable<?> getSeriesKey(int series) {
    return collection.getSeriesKey(series);
  }

  // TODO: Remove this raw type suppression when a new version of
  // of JFreeChart is release that adds the type
  @SuppressWarnings("rawtypes")
  @Override
  public int indexOf(Comparable seriesKey) {
    return collection.indexOf(seriesKey);
  }

  @Override
  public void addChangeListener(DatasetChangeListener listener) {
    collection.addChangeListener(listener);
  }

  @Override
  public void removeChangeListener(DatasetChangeListener listener) {
    collection.removeChangeListener(listener);
  }

  @Override
  public DatasetGroup getGroup() {
    return collection.getGroup();
  }

  @Override
  public void setGroup(DatasetGroup group) {
    collection.setGroup(group);
  }

  @Override
  public DomainOrder getDomainOrder() {
    return collection.getDomainOrder();
  }

  @Override
  public int getItemCount(int series) {
    return collection.getItemCount(series);
  }

  @Override
  public Number getX(int series, int item) {
    return collection.getX(series, item);
  }

  @Override
  public Number getY(int series, int item) {
    return collection.getY(series, item);
  }

  @Override
  public double getYValue(int series, int item) {
    return collection.getYValue(series, item);
  }

  @Override
  public Number getHigh(int series, int item) {
    return collection.getHigh(series, item);
  }

  @Override
  public double getHighValue(int series, int item) {
    return collection.getHighValue(series, item);
  }

  @Override
  public Number getLow(int series, int item) {
    return collection.getLow(series, item);
  }

  @Override
  public double getLowValue(int series, int item) {
    return collection.getLowValue(series, item);
  }

  @Override
  public Number getOpen(int series, int item) {
    return collection.getOpen(series, item);
  }

  @Override
  public double getOpenValue(int series, int item) {
    return collection.getOpenValue(series, item);
  }

  @Override
  public Number getClose(int series, int item) {
    return collection.getClose(series, item);
  }

  @Override
  public double getCloseValue(int series, int item) {
    return collection.getCloseValue(series, item);
  }

  @Override
  public Number getVolume(int series, int item) {
    return collection.getVolume(series, item);
  }

  @Override
  public double getVolumeValue(int series, int item) {
    return collection.getVolumeValue(series, item);
  }
}