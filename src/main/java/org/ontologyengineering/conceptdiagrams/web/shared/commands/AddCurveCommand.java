package org.ontologyengineering.conceptdiagrams.web.shared.commands;

/**
 * Author: Michael Compton<br>
 * Date: September 2015<br>
 * See license information in base directory.
 */

import com.google.web.bindery.event.shared.Event;
import org.ontologyengineering.conceptdiagrams.web.client.events.AddCurveEvent;
import org.ontologyengineering.conceptdiagrams.web.client.events.AddZoneEvent;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteBoundaryRectangle;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteCurve;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteDiagram;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteZone;
import org.ontologyengineering.conceptdiagrams.web.shared.curvegeometry.Point;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.AddUnlabelledCurve;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.LabelledMultiDiagramTransformation;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.TransformAClassAndObjectPropertyDiagram;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.TransformADatatypeDiagram;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.HashSet;


public class AddCurveCommand extends Command {

    private Point topLeft, bottomRight;
    private ConcreteBoundaryRectangle boundaryRectangle;
    private ConcreteCurve curve;
    private RemoveCurveCommand myUndo;

    // The boundary rectangle implies the diagram set
    public AddCurveCommand(Point topLeft, Point bottomRight, ConcreteBoundaryRectangle boundaryRectangle) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.boundaryRectangle = boundaryRectangle;

        curve = new ConcreteCurve(topLeft, bottomRight);

        myUndo = new RemoveCurveCommand(this);
    }

    public AddCurveCommand(RemoveCurveCommand opposite) {
        curve = opposite.getCurve();
        topLeft = curve.topLeft();
        bottomRight = curve.bottomRight();
        boundaryRectangle = curve.getBoundaryRectangle();

        myUndo = opposite;
    }

    @Override
    public void execute() {
        // curve.setTopLeft(topLeft);
        // FIXME : set the top right also??
        curve.setBoundaryRectangle(boundaryRectangle);

        //boundaryRectangle.addCurve(curve);
    }

    @Override
    public void unExecute() {
        myUndo.execute();
    }

    public AbstractCollection<Event> getEvents() {
        HashSet<Event> result = new HashSet<Event>();
        result.add(new AddCurveEvent(curve));

        // every zone in a new curve is new, so we have a bunch of add zone events too
        result.add(new AddZoneEvent(curve.getMainZone()));
        for(ConcreteZone z : curve.getEnclosedZones()) {
            result.add(new AddZoneEvent(z));
        }

        return result;
    }

    @Override
    public AbstractCollection<Event> getUnExecuteEvents() {
        return myUndo.getEvents();
//        HashSet<Event> result = new HashSet<Event>();
//        result.add(new RemoveCurveEvent(curve));
//        return result;
    }

    @Override
    public ConcreteDiagram getDiagram() {
        return boundaryRectangle.getDiagram();
    }

    @Override
    public boolean leadsToValid() {
        return true;  // not sure what could be invalid here?
    }

    @Override
    public LabelledMultiDiagramTransformation asMultiDiagramTransformation(AbstractList<Command> commands, int myPlace) {
        if(boundaryRectangle.isObject()) {  // types should have been inferred by now
            return new TransformAClassAndObjectPropertyDiagram(new AddUnlabelledCurve(getCurve()));
        } else {
            return new TransformADatatypeDiagram(new AddUnlabelledCurve(getCurve()));
        }
    }

    protected ConcreteCurve getCurve () {
        return curve;
    }
}
