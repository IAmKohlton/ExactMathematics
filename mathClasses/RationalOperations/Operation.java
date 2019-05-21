package mathClasses.RationalOperations;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

/**
 * Class representing a polynomial operation. Doesn't do anything by itself
 */
public abstract class Operation {
    /**
     * RationalPolynomial that is the first input to the operation
     */
    protected RationalPolynomial firstPoly;

    /**
     * RationalPolynomial this is the second input to the operation (optional)
     */
    protected RationalPolynomial secondPoly;

    /**
     * Rational consant input to the operation
     */
    protected Rational constant;

    /**
     * output of the operation
     */
    protected RationalOperationOutput output;

    /**
     * constructs a new operation
     * @param firstPoly first input to the operation
     * @param secondPoly second input to the operation
     * @param constant constant input to the operation
     */
    public Operation(RationalPolynomial firstPoly, RationalPolynomial secondPoly, Rational constant){
        this.firstPoly = firstPoly;
        this.secondPoly = secondPoly;
        this.constant = constant;
    }

    /**
     * constructs a new operation
     * @param firstPoly first input to the operation
     * @param secondPoly second input to the operation
     */
    public Operation(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        this(firstPoly, secondPoly, null);
    }

    /**
     * constructs a new operation
     * @param poly first input to the operation
     * @param constant constant input to the operation
     */
    public Operation(RationalPolynomial poly, Rational constant){
        this(poly, null, constant);
    }

    /**
     * constructs a new operation
     * @param poly first input to the operation
     */
    public Operation(RationalPolynomial poly){
        this(poly, null, null);
    }

    /**
     * Does the desired computation. Differs wildly based on what operation is being performed
     */
    public abstract void compute(); // the only requirement of compute is that it must set 'output' to the result of the computation

    /**
     * Retrieves the output of the operation.
     * @return output of operation
     */
    public abstract RationalOperationOutput getOutput();
    // only thing that should be in this method body is of the form:
    // return (ClassOfOutput) output
}
