package org.drools.planner.core.heuristic.selector.entity.decorator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.drools.planner.core.domain.entity.PlanningEntityDescriptor;
import org.drools.planner.core.heuristic.selector.common.SelectionCacheLifecycleBridge;
import org.drools.planner.core.heuristic.selector.common.SelectionCacheLifecycleListener;
import org.drools.planner.core.heuristic.selector.common.SelectionCacheType;
import org.drools.planner.core.heuristic.selector.entity.AbstractEntitySelector;
import org.drools.planner.core.heuristic.selector.entity.EntitySelector;
import org.drools.planner.core.solver.DefaultSolverScope;

public abstract class AbstractCachingEntitySelector extends AbstractEntitySelector implements SelectionCacheLifecycleListener {

    protected final EntitySelector childEntitySelector;
    protected final SelectionCacheType cacheType;

    protected List<Object> cachedEntityList = null;

    public AbstractCachingEntitySelector(EntitySelector childEntitySelector, SelectionCacheType cacheType) {
        this.childEntitySelector = childEntitySelector;
        this.cacheType = cacheType;
        if (childEntitySelector.isNeverEnding()) {
            throw new IllegalStateException("The childEntitySelector (" + childEntitySelector + ") has neverEnding ("
                    + childEntitySelector.isNeverEnding() + ") on a class (" + getClass().getName() + ") instance.");
        }
        solverPhaseLifecycleSupport.addEventListener(childEntitySelector);
        if (cacheType.isNotCached()) {
            throw new IllegalArgumentException("The cacheType (" + cacheType
                    + ") is not supported on the class (" + getClass().getName() + ").");
        }
        solverPhaseLifecycleSupport.addEventListener(new SelectionCacheLifecycleBridge(cacheType, this));
    }

    public EntitySelector getChildEntitySelector() {
        return childEntitySelector;
    }

    // ************************************************************************
    // Worker methods
    // ************************************************************************

    public void constructCache(DefaultSolverScope solverScope) {
        long childSize = childEntitySelector.getSize();
        if (childSize > (long) Integer.MAX_VALUE) {
            throw new IllegalStateException("The entitySelector (" + this + ") has a childEntitySelector ("
                    + childEntitySelector + ") with childSize (" + childSize
                    + ") which is higher than Integer.MAX_VALUE.");
        }
        cachedEntityList = new ArrayList<Object>((int) childSize);
        CollectionUtils.addAll(cachedEntityList, childEntitySelector.iterator());
    }

    public void disposeCache(DefaultSolverScope solverScope) {
        cachedEntityList = null;
    }

    public PlanningEntityDescriptor getEntityDescriptor() {
        return childEntitySelector.getEntityDescriptor();
    }

    public boolean isContinuous() {
        return false;
    }

    public long getSize() {
        return cachedEntityList.size();
    }

}