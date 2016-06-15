package org.ontologyengineering.conceptdiagrams.web.shared.concretesyntax;


/**
 * Author: Michael Compton<br>
 * Date: September 2015<br>
 * See license information in base directory.
 */


import com.fasterxml.jackson.annotation.*;
import org.ontologyengineering.conceptdiagrams.web.shared.curvegeometry.Point;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")  // Jackson serialization
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "jacksontype")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConcreteArrow.class, name = "ConcreteArrow"),
        @JsonSubTypes.Type(value = ConcreteBoundaryRectangle.class, name = "ConcreteBoundaryRectangle"),
        @JsonSubTypes.Type(value = ConcreteCurve.class, name = "ConcreteCurve"),
        @JsonSubTypes.Type(value = ConcreteDiagram.class, name = "ConcreteDiagram"),
        @JsonSubTypes.Type(value = ConcreteIntersectionZone.class, name = "ConcreteIntersectionZone"),
        @JsonSubTypes.Type(value = ConcreteRectangularElement.class, name = "ConcreteRectangularElement"),
        @JsonSubTypes.Type(value = ConcreteSpider.class, name = "ConcreteSpider"),
        @JsonSubTypes.Type(value = ConcreteStarRectangle.class, name = "ConcreteStarRectangle"),
        @JsonSubTypes.Type(value = ConcreteZone.class, name = "ConcreteZone")
})
public abstract class ConcreteDiagramElement implements Serializable {

    protected static final double curveCornerRadius = 10;
    protected static final double curveBorderWidth = 3;
    public static final double curveMinWidth = (curveCornerRadius * 4);  // no small curves constraint
    public static final double curveMinHeight = curveMinWidth;           // no small curves constraint

    public static final double spiderRadius = 7;

    protected static final double boundaryRectangleBorderWidth = curveBorderWidth * 2;
    public static final double boundaryRectangleMinWidth = (curveMinWidth * 2) + (curveCornerRadius * 3);
    public static final double boundaryRectangleBorderHeight = boundaryRectangleMinWidth;

    protected static final double arrowLineWidth = 2;
    protected static final int pointsInArrowLine = 4;

    protected static final double zoneCornerRadius = curveCornerRadius - curveBorderWidth;




    // for gson serialization
    private String type;

    public enum ConcreteDiagramElement_TYPES {
        CONCRETEELEMENT, CONCRETERECTANGULARELEMENT,
        CONCRETEARROW, CONCRETEBOUNDARYRECTANGE, CONCRETESTARRECTANGLE,
        CONCRETECURVE, CONCRETEDIAGRAM,
        CONCRETESPIDER,
        CONCRETEZONE, CONCRETEINTERSECTIONZONE
    }
    private ConcreteDiagramElement_TYPES myType;

//    private Boolean abstractSyntaxUpToDate;
//    private T abstractSyntaxRepresentation;

    protected ConcreteBoundaryRectangle myBoundaryRectangle; // everything should have only one except arrows, which pick source
    // can always get from an arrow any way with destination's boundary rectangle.


    private HashSet<ConcreteArrow> sourcedArrows;
    private HashSet<ConcreteArrow> targettedArrows;

    private Point topLeft;

    private String label;

    private boolean isObject = true;
    private boolean typeKnown = true;
    private boolean isValid = true;

    // just for serialization
    public ConcreteDiagramElement() {
        //this(new Point(0,0), ConcreteDiagramElement_TYPES.CONCRETEELEMENT);
    }

    public ConcreteDiagramElement(Point topLeft, ConcreteDiagramElement_TYPES type) {
        setTopLeft(topLeft);
        setType(type);
        //setAbstractSyntaxNOTUpToDate();

        sourcedArrows = new HashSet<ConcreteArrow>();
        targettedArrows = new HashSet<ConcreteArrow>();
    }

    protected ConcreteDiagramElement(ConcreteDiagramElement_TYPES type) {
        setType(type);
    }

    private void setType(ConcreteDiagramElement_TYPES newType) {
        myType = newType;
        setTypeString();
    }

    public ConcreteDiagramElement_TYPES getType() {
        return myType;
    }

    private void setTypeString() {
        switch (getType()) {
            case CONCRETEELEMENT:
                type = "ConcreteDiagramElement";
                break;
            case CONCRETERECTANGULARELEMENT:
                type = "ConcreteRectangularElement";
                break;
            case CONCRETEARROW:
                type = "ConcreteArrow";
                break;
            case CONCRETEBOUNDARYRECTANGE:
                type = "ConcreteBoundaryRectangle";
                break;
            case CONCRETESTARRECTANGLE:
                type = "ConcreteStarRectangle";
                break;
            case CONCRETECURVE:
                type = "ConcreteCurve";
                break;
            case CONCRETEDIAGRAM:
                type = "ConcreteDiagram";
                break;
            case CONCRETESPIDER:
                type = "ConcreteSpider";
                break;
            case CONCRETEZONE:
                type = "ConcreteZone";
                break;
            case CONCRETEINTERSECTIONZONE:
                type = "ConcreteIntersectionZone";
                break;
        }
    }


    public abstract void setBoundaryRectangle(ConcreteBoundaryRectangle rect);// {
//        myBoundaryRectangle = rect;
//        rect.addChild(this);
//    }

    public ConcreteBoundaryRectangle getBoundaryRectangle() {
        return myBoundaryRectangle;
    }



//    public Boolean isAbstractRepresentationSyntaxUpToDate() {
//        return abstractSyntaxUpToDate;
//    }
//
//    protected void setAbstractSyntaxUpToDate() {
//        abstractSyntaxUpToDate = true;
//    }
//
//    protected void setAbstractSyntaxNOTUpToDate() {
//        abstractSyntaxUpToDate = false;
//    }

//    public void setAbstractSyntaxRepresentation(T representation) {
//        abstractSyntaxRepresentation = representation;
//        setAbstractSyntaxUpToDate();
//    }

//    public T getAbstractSyntaxRepresentation() {
//        return abstractSyntaxRepresentation;
//    }


    public void setLabel(String newLabel) {
        label = newLabel;
    }

    public String labelText() {
        return label;
    }

    public Boolean hasLabel() {
        return !(label == null);
    }


    public void setAsObject() {
        isObject = true;
        setTypeKnown();
    }

    public void setAsData() {
        isObject = false;
        setTypeKnown();
    }

    public void setTypeKnown() {
        typeKnown = true;
    }

    public boolean typeIsKnown() {
        return typeKnown;
    }

    public boolean isObject() {
        return typeKnown && isObject;
    }

    public boolean isData() {
        return typeKnown && ! isObject();
    }

    public boolean isValid() {
        return isValid;
    }

    protected void setValid(boolean validity) {
        isValid = validity;
    }

    public abstract void checkValidity();


    public double getX() {
        return topLeft().getX();
    }

    public double getY() {
        return topLeft().getY();
    }

    public Point topLeft() {
        return topLeft;
    }

    public abstract Point bottomRight();

    public double getWidth() {
        return bottomRight().getX() - topLeft().getX();
    }

    public double getHeight() {
        return bottomRight().getY() - topLeft().getY();
    }

    protected void setTopLeft(Point newTopLeft) {
        topLeft = newTopLeft;
    }

    public abstract Point centre();

    public ConcreteDiagram getDiagram() {
        return getBoundaryRectangle().getParentDiagram();
    }


    public void move(Point topLeft) {
        setTopLeft(topLeft);
    }


    protected void setAsArrowSource(ConcreteArrow arrow) {
        sourcedArrows.add(arrow);
    }

    protected void setAsArrowTarget(ConcreteArrow arrow) {
        targettedArrows.add(arrow);
    }

    protected void removeAsArrowSource(ConcreteArrow arrow) {
        sourcedArrows.remove(arrow);
    }

    protected void removeAsArrowTarget(ConcreteArrow arrow){
        targettedArrows.remove(arrow);
    }

    public Set<ConcreteArrow> getAllAttachedArrows() {
        Set<ConcreteArrow> result = new HashSet<ConcreteArrow>();
        result.addAll(sourcedArrows);
        result.addAll(targettedArrows);
        return result;
    }

    // FIXME to remove ... done now through the translations
//    public abstract void makeAbstractRepresentation();

    public abstract void deleteMe();

}
