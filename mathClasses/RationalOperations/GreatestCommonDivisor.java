package mathClasses.RationalOperations;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

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
        return gcd;
    }

    private static RationalPolynomial euclidRecursive(RationalPolynomial lessDegree, RationalPolynomial greaterDegree){
        if(lessDegree.equals(new RationalPolynomial(new Rational(0,1)))){
            return greaterDegree;
        }else{
            return euclidRecursive(greaterDegree, greaterDegree.remainder(lessDegree));
        }
    }
}
