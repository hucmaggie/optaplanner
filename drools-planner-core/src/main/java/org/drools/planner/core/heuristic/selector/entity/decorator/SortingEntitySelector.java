/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.planner.core.heuristic.selector.entity.decorator;

import java.util.Iterator;
import java.util.ListIterator;

import org.drools.planner.core.heuristic.selector.common.SelectionCacheType;
import org.drools.planner.core.heuristic.selector.common.decorator.SelectionSorter;
import org.drools.planner.core.heuristic.selector.entity.EntitySelector;
import org.drools.planner.core.solver.scope.DefaultSolverScope;

public class SortingEntitySelector extends AbstractCachingEntitySelector {

    protected final SelectionSorter entitySelectionSorter;

    public SortingEntitySelector(EntitySelector childEntitySelector, SelectionCacheType cacheType,
            SelectionSorter entitySelectionSorter) {
        super(childEntitySelector, cacheType);
        this.entitySelectionSorter = entitySelectionSorter;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    @Override
    public void constructCache(DefaultSolverScope solverScope) {
        super.constructCache(solverScope);
        entitySelectionSorter.sort(solverScope.getScoreDirector(), cachedEntityList);
        logger.trace("    Sorted cachedEntityList with size ({}) in entitySelector({}).",
                cachedEntityList.size(), this);
    }

    public boolean isNeverEnding() {
        return false;
    }

    public Iterator<Object> iterator() {
        return cachedEntityList.iterator();
    }

    public ListIterator<Object> listIterator() {
        return cachedEntityList.listIterator();
    }

    public ListIterator<Object> listIterator(int index) {
        return cachedEntityList.listIterator(index);
    }

    @Override
    public String toString() {
        return "Sorting(" + childEntitySelector + ")";
    }

}