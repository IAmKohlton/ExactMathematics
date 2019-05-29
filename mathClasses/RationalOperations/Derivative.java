package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import mathClasses.Rational;
import mathClasses.RationalPolyIterator;
import mathClasses.RationalPolynomial;

/**
 * Operation to calculate the derivative of a polynomial
 */
public class Derivative extends Operation {

    public Derivative(RationalPolynomial poly){
        super(poly);
    }

    public void compute(){
        getDerivative();
    }

    public RationalPolynomial getOutput(){
        return (RationalPolynomial) output;
    }

    private void getDerivative(){
        if(firstPoly.isNull()){
            throw new IllegalStateException("Can't take derivative of null polynomial");
        }

        if(firstPoly.getDegree() == 0){ // derivative of a constant is zero
            output = new RationalPolynomial(new Rational(0));
        }else{
            RationalPolyIterator iterator = firstPoly.getIterator();
            iterator.goFirst();
            iterator.goForth();
            DoublyLinkedList<Rational> newPoly = new DoublyLinkedList<>();
            int i = 1;
            Rational currentRat;
            while(!iterator.isAfter()){
                currentRat = iterator.currentRational().multiply(new Rational(i));
                newPoly.insert(currentRat);
                iterator.goForth();
                i++;
            }
            output = new RationalPolynomial(newPoly);
        }

    }
}
