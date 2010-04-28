/**
 * File: RuleSetContainer.java
 *
 * Author: David Hay (dhay@localmatters.com)
 * Creation Date: Apr 28, 2010
 * Creation Time: 10:14:46 AM
 *
 * Copyright 2010 Local Matters, Inc.
 * All Rights Reserved
 *
 * Last checkin:
 *  $Author$
 *  $Revision$
 *  $Date$
 */
package org.lesscss4j.model;

import java.util.List;

public interface RuleSetContainer {
    void addRuleSet(RuleSet ruleSet, int index);
    List<RuleSet> getRuleSet(Selector selector);
}
