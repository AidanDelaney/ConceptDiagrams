package org.ontologyengineering.conceptdiagrams.web.shared.abstractsyntax;

/**
 * Author: Michael Compton<br> Date: September 2015<br> See license information in base directory.
 */


import java.util.AbstractSet;
import java.util.HashSet;

/**
 * a LabelledDiagram where
 *
 * lambda_s has range V_I
 * lambda_c has range V_C
 */
public class ClassAndObjectPropertyDiagram extends LabelledDiagram {

    public ClassAndObjectPropertyDiagram() {
        super();
    }

    public ClassAndObjectPropertyDiagram(LabelledMultiDiagram parent) {
        super(parent);
    }

    // TODO : add constraints


    @Override
    public AbstractSet<Zone> getZonesToTest(AbstractSet<Zone> Zdash, FastCurveSet curveMask, FastCurveSet IN, FastCurveSet OUT, FastCurveSet removedCurves) {
        AbstractSet<Zone> zonesToTest = new HashSet<Zone>();
        for (Zone z : Z()) {
            if (!Zdash.contains(z) && !Zstar().contains(z)) {
                // fine but do we need to test it given what we have removed from OUT
                if(removedCurves.intersectionEmpty(z.inAsFastCurveSet(), curveMask)) {
                    zonesToTest.add(z);
                }
            }
        }
        return zonesToTest;
    }




}