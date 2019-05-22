package tests;

import mathClasses.RationalOperations.GreatestCommonDivisor;
import mathClasses.RationalPolynomial;

import static mathClasses.Rational.R;

public class GCDTest {
    public static void main(String[] args){
        test(false);
    }
    public static void test(boolean quietExit) {
        RationalPolynomial firstPoly = new RationalPolynomial(R(1, 1), R(1, 1));
        RationalPolynomial secondPoly = new RationalPolynomial(R(2, 1), R(5, 1));
        RationalPolynomial thirdPoly = new RationalPolynomial(R(3, 1), R(7, 1));
        RationalPolynomial fourthPoly = firstPoly.multiply(secondPoly);
        RationalPolynomial fifthPoly = firstPoly.multiply(thirdPoly);
        GreatestCommonDivisor computation = new GreatestCommonDivisor(fourthPoly, fifthPoly);
        computation.compute();
        RationalPolynomial resultPoly = computation.getOutput();
        if(!(resultPoly.equals(firstPoly))){
            System.out.println("Didn't correctly find the greatest common divisor");
            System.out.println(resultPoly);
            System.out.println(firstPoly);
        }

        if(!quietExit)
            System.out.println("Finished testing greatest common divisors");
    }
}
