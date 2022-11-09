/*
 * jfreechart-builder-demo: a demonstration app for jfreechart-builder
 * 
 * (C) Copyright 2020, by Matt E.
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

package com.jfcbuilder.demo.data.providers;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation for generating arrays of date-time values in ascending chronological order.
 *
 */
public class AscendingDateTimeGenerator implements IDateTimeSeriesProvider {

  /**
   * Hidden constructor
   */
  private AscendingDateTimeGenerator() {
    // Explicitly do nothing
  }

  /**
   * Factory method for create new instances of this class.
   * 
   * @return New instance of this class
   */
  public static AscendingDateTimeGenerator get() {
    return new AscendingDateTimeGenerator();
  }

  @Override
  public long[] getDateTimes(LocalDateTime startDate, LocalDateTime endDate,
      ChronoUnit spacing, Set<DayOfWeek> skipDays) {

    Objects.requireNonNull(startDate, "Start date cannot be null.");
    Objects.requireNonNull(endDate, "End date cannot be null.");
    Objects.requireNonNull(spacing, "Temporal spacing cannot be null.");

    final Set<DayOfWeek> skipDaysOfWeek = skipDays == null ? Collections.emptySet() : skipDays;

    List<Long> dates = new ArrayList<>();

    for (LocalDateTime nextDate = startDate.plus(1, spacing); !nextDate.isAfter(endDate); nextDate = nextDate
        .plus(1, spacing)) {

      if (skipDaysOfWeek.contains(nextDate.getDayOfWeek())) {
        continue;
      }

      dates.add(nextDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    return dates.stream().mapToLong(Long::longValue).toArray();
  }

}
