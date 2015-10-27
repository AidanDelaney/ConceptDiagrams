package org.ontologyengineering.conceptdiagrams.web.shared.commands;

/**
 * Author: Michael Compton<br>
 * Date: September 2015<br>
 * See license information in base directory.
 */

import com.google.web.bindery.event.shared.Event;
import org.ontologyengineering.conceptdiagrams.web.client.events.AddSpiderEvent;
import org.ontologyengineering.conceptdiagrams.web.client.events.RemoveSpiderEvent;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteBoundaryRectangle;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteDiagram;
import org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax.ConcreteSpider;
import org.ontologyengineering.conceptdiagrams.web.shared.curvegeometry.Point;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.AddLabelledSpider;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.LabelledMultiDiagramTransformation;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.TransformAClassAndObjectPropertyDiagram;
import org.ontologyengineering.conceptdiagrams.web.shared.transformations.TransformADatatypeDiagram;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.HashSet;


public class AddSpiderCommand extends Command {


    private Point centre;
    private ConcreteBoundaryRectangle boundaryRectangle;
    private ConcreteSpider theSpider;

    public AddSpiderCommand(Point centre, ConcreteBoundaryRectangle boundaryRectangle) {
        this.centre = centre;
        this.boundaryRectangle = boundaryRectangle;

        theSpider = new ConcreteSpider(centre);
    }

    @Override
    public void execute() {
        theSpider.setBoundaryRectangle(boundaryRectangle);
    }

    @Override
    public void unExecute() {
        boundaryRectangle.removeSpider(theSpider);
    }

    @Override
    public AbstractCollection<Event> getEvents() {
        HashSet<Event> result = new HashSet<Event>();
        result.add(new AddSpiderEvent(theSpider));
        return result;
    }

    @Override
    public AbstractCollection<Event> getUnExecuteEvents() {
        HashSet<Event> result = new HashSet<Event>();
        result.add(new RemoveSpiderEvent(theSpider));
        return result;
    }

    @Override
    public ConcreteDiagram getDiagram() {
        return boundaryRectangle.getDiagram();
    }

    protected ConcreteSpider getSpider() {
        return theSpider;
    }

    @Override
    public boolean leadsToValid() {
        return getSpider().hasLabel();
    }

    @Override
    public LabelledMultiDiagramTransformation asMultiDiagramTransformation(AbstractList<Command> commands, int myPlace) {
        if(getSpider().isObject()) {
            return new TransformAClassAndObjectPropertyDiagram(new AddLabelledSpider(getSpider()));
        } else {
            return new TransformADatatypeDiagram(new AddLabelledSpider(getSpider()));
        }
    }
}
