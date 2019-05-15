package mathClasses.RationalOperations;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

public abstract class Operation {
    RationalPolynomial firstPoly;
    RationalPolynomial secondPoly;
    Rational constant;

    RationalPolynomial outputPoly;
    Rational outputConstant;

    public Operation(RationalPolynomial firstPoly, RationalPolynomial secondPoly, Rational constant){
        this.firstPoly = firstPoly;
        this.secondPoly = secondPoly;
        this.constant = constant;
    }

    public Operation(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        this(firstPoly, secondPoly, null);
    }

    public Operation(RationalPolynomial poly, Rational constant){
        this(poly, null, constant);
    }

    public Operation(RationalPolynomial poly){
        this(poly, null, null);
    }

    public abstract void compute();
}
