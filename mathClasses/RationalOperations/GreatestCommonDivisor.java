package mathClasses.RationalOperations;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

/**
 * Operation to calculate the greatest common divisor
 */
public class GreatestCommonDivisor extends Operation {

    public GreatestCommonDivisor(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        super(firstPoly, secondPoly);
    }

    public void compute(){
        output = euclid();
    }

    public RationalPolynomial getOutput(){
        return (RationalPolynomial) output;
    }

    private RationalPolynomial euclid(){
        RationalPolynomial gcd;
        if(firstPoly.getDegree() < secondPoly.getDegree()){
            gcd = euclidRecursive(firstPoly, secondPoly);
        }else{
            gcd = euclidRecursive(secondPoly, firstPoly);
        }
        // at this point it's true that the poly in 'gcd' divides both, but it is not unique
        // to make it unique we scale the poly so that it is monic, and positive
        Rational highestOrderCoeff = gcd.getLast();
        gcd = gcd.scale(highestOrderCoeff.getInverse());
        return gcd;
    }

    private static RationalPolynomial euclidRecursive(RationalPolynomial lessDegree, RationalPolynomial greaterDegree){
        if(lessDegree.equals(new RationalPolynomial(new Rational(0,1)))){
            return greaterDegree;
        }else{
            return euclidRecursive(greaterDegree.remainder(lessDegree), lessDegree);
        }
    }
}
