package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import mathClasses.Rational;
import mathClasses.RationalPolyIterator;
import mathClasses.RationalPolynomial;

/**
 * Computes integral of a given polynomial
 * Currently sets the constant term to be zero, but that is mathematically incorrect.
 * Will come back to fix that when I implement generic scaler terms
 */
public class Integrate extends Operation {
    public Integrate(RationalPolynomial poly){
        super(poly);
    }

    public void compute() {

    }

    public RationalPolynomial getOutput(){
        return (RationalPolynomial) output;
    }

    public void getIntegral(){
        if(firstPoly.isNull()){
            throw new IllegalStateException("Can't compute integral of null polynomial");
        }
        RationalPolyIterator iterator = firstPoly.getIterator();
        DoublyLinkedList<Rational> newPoly = new DoublyLinkedList<>();
        newPoly.insert(new Rational(0)); // TODO fix this after I implement generic scaler Rational terms
        iterator.goFirst();
        int i = 1;
        Rational currentRat;
        while(!iterator.isAfter()){
            currentRat = iterator.getCurrentRational().divide(new Rational(i));
            newPoly.insert(currentRat);
        }
        output = new RationalPolynomial(newPoly);
    }
}
