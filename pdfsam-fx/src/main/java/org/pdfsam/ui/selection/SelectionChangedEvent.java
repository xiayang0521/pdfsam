/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 26/giu/2013
 * Copyright 2013 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui.selection;

import static org.pdfsam.support.RequireUtils.require;
import static org.pdfsam.support.RequireUtils.requireNotNull;

import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Event sent when the selection on the selection table changed
 * 
 * @author Andrea Vacondio
 * 
 */
final class SelectionChangedEvent {

    private int top = Integer.MAX_VALUE;
    private int bottom = -1;
    private int totalRows = 0;

    private SelectionChangedEvent(Collection<? extends Integer> selected) {
        requireNotNull(selected, "Input selection cannot be null");
        selected.forEach((i) -> {
            bottom = Math.max(i, bottom);
            top = Math.min(i, top);
        });
    }

    private SelectionChangedEvent() {
        // nothing
    }

    /**
     * @return true the selection has been cleared
     */
    public boolean isClearSelection() {
        return top == Integer.MAX_VALUE && bottom == -1;
    }

    /**
     * @return true if its a single row selection event
     */
    public boolean isSingleSelection() {
        return !isClearSelection() && top == bottom;
    }

    public boolean canMove(MoveType type) {
        if (type == MoveType.DOWN) {
            return !isClearSelection() && bottom < totalRows - 1;
        }
        return !isClearSelection() && top > 0;
    }

    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @return the event where the selection has been cleared
     */
    public static SelectionChangedEvent clearSelectionEvent() {
        return new SelectionChangedEvent();
    }

    /**
     * @param index
     * @return the event where a single selection has been set
     */
    public static SelectionChangedEvent select(Collection<? extends Integer> index) {
        return new SelectionChangedEvent(index);
    }

    /**
     * @param totalNumberOfRows
     * @return the event where the total number of rows available has been set
     */
    public SelectionChangedEvent ofTotalRows(int totalNumberOfRows) {
        require(totalNumberOfRows >= 0, "Cannot select rows if no row is available");
        this.totalRows = totalNumberOfRows;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
