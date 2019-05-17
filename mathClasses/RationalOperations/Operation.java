package mathClasses.RationalOperations;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

public abstract class Operation {
    protected RationalPolynomial firstPoly;
    protected RationalPolynomial secondPoly;
    protected Rational constant;
    protected RationalOperationOutput output;

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

    public abstract void compute(); // the only requirement of compute is that it must set 'output' to the result of the computation

    public RationalOperationOutput getOutput(){
        return output;
    }
}
