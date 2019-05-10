package tests;

import mathClasses.Rational;
import mathClasses.RationalPolynomial;

import static mathClasses.Rational.*;
import static mathClasses.Rational.R;
import static mathClasses.RationalPolynomial.createFromIntegers;

public class RatPolyTest {
    public static void main(String[] args){
        test(false);
    }
    public static void test(boolean quietEnding){
        // assumes the constructors, equals, and private methods work
        // this assumption can be made since if they were wrong then everything else would also go wrong
        // toString was also tested fairly thoroughly beforehand

        boolean caught;
        Rational inf = makePositiveInfinity();
        Rational negInf = makeNegativeInfinity();

        // now testing copy
        RationalPolynomial test1 = createFromIntegers(1,2,3);
        RationalPolynomial test1_1 = test1.copy();
        if(!test1.equals(test1_1) || test1 == test1_1)
            System.out.println("copy does not copy am integer polynomial properly");

        RationalPolynomial nullPoly = new RationalPolynomial();
        RationalPolynomial nullPoly_1 = nullPoly.copy();
        if(!nullPoly.equals(nullPoly_1) || nullPoly == nullPoly_1)
            System.out.println("copy doesn't copy null polynomial properly");

        RationalPolynomial test2 = new RationalPolynomial(new Rational(1,2), new Rational(2,3), new Rational(3,4), new Rational(4, 5));
        RationalPolynomial test2_1 = test2.copy();
        if(!test2.equals(test2_1) || test2 == test2_1)
            System.out.println("copy doesn't properly copy a rational polynomial");

        // now testing scale
        RationalPolynomial zero = new RationalPolynomial(R(0,1));
        if(!(zero.scale(100).equals(zero)))
            System.out.println("0 * 100 != 0");

        if(!(test1.scale(3).equals(createFromIntegers(3,6,9))))
            System.out.println("(1+2x+3x^2) * 3 != 3+6x+9x^2");

        if(!(nullPoly.scale(100000).equals(nullPoly)))
            System.out.println("null * 0 != null");

        RationalPolynomial test2_5 = test2.scale(4);
        RationalPolynomial test2_5_expected = new RationalPolynomial(R(2, 1), R(8,3), R(3, 1), R(16, 5));
        if(!(test2_5.equals(test2_5_expected)))
            System.out.println("Didn't scale rational polynomial correctly");

        RationalPolynomial test3 = new RationalPolynomial(R(-1,3), R(4, 1), R(-3,2));
        RationalPolynomial test3_1 = test3.scale(R(-3,4));
        RationalPolynomial test3_1_expected = new RationalPolynomial(R(3,12), R(-3, 1), R(9, 8));
        if(!(test3_1.equals(test3_1_expected)))
            System.out.println("Didn't scale rational by a rational correctly");

        caught = false;
        RationalPolynomial test0 = new RationalPolynomial(R(0,1));
        try{
            test0.scale(inf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught){
            System.out.println("Didn't throw exception on 0 poly * inf");
        }

        // now testing solve
        RationalPolynomial test4_5 = new RationalPolynomial(R(3,1), R(2,1), R(1,1));
        Rational test4 = test4_5.solve(0);
        if(!(test4.equals(R(3,1)))) {
            System.out.println("polynomial at x=0 isn't the value of the constant");
            System.out.println(test4);
        }


        Rational test5 = test1.solve(1);
        if(!(test5.equals(R(6,1)))) {
            System.out.println("polynomials at x=1 isn't sum of coefficients");
            System.out.println(test5);
        }

        Rational test6 = test1.solve(inf);
        if(!test6.equals(inf))
            System.out.println("polynomial at x=inf isn't inf when highest coefficient is positive");

        Rational test6_1 = test2.solve(R(5,8));
        if(!test6_1.equals(R(1079, 768))) {
            System.out.println("polynomial at x=5/8 isn't the calculated value");
        }

        // now testing add

        RationalPolynomial test7 = new RationalPolynomial(R(1,2), R(2,3), R(3,4));
        RationalPolynomial test8 = new RationalPolynomial(R(5,6), R(7,8));
        RationalPolynomial test9 = new RationalPolynomial(R(8,6), R(37, 24), R(3,4)); // = test7 + test8
        RationalPolynomial test10 = new RationalPolynomial(R(-2, 6), R(-5, 24), R(3,4)); // = test7 - test8

        if(!(test7.add(test8).equals(test9)))
            System.out.println("didn't add polynomials properly");

        if(!(test10.add(test8).equals(test7)))
            System.out.println("didn't add polynomials properly");

        if(!(test10.add(test9).equals(test7.scale(2))))
            System.out.println("(x - y) + (x + y) != 2x");

        // now testing subtract

        if(!(test7.subtract(test8).equals(test10)))
            System.out.println("didn't subtract properly");

        if(!(test8.subtract(test7).equals(test10.scale(-1))))
            System.out.println("given x - y = z then y - x != -z");

        if(!(test9.subtract(test7).equals(test8))) {
            System.out.println(test9.subtract(test7));
            System.out.println(test8);
            System.out.println("didn't subtract correctly");
        }

        if(!(test9.subtract(test9).equals(new RationalPolynomial(R(0,1))))) {
            System.out.println(new RationalPolynomial(R(0, 1)));
            System.out.println(test9.subtract(test9));
        }


        // now testing multiply
        RationalPolynomial test11 = new RationalPolynomial(R(2,1), R(3,1));
        RationalPolynomial test12 = new RationalPolynomial(R(-5,1), R(2,1));
        RationalPolynomial test13 = new RationalPolynomial(R(-10, 1), R(-11, 1), R(6,1));
        RationalPolynomial constantThree = new RationalPolynomial(R(3,1));

        if(!test11.multiply(constantThree).equals(test11.scale(3)))
            System.out.println("multiply by constant not same as scaling by constant");

        if(!test11.multiply(zero).equals(zero))
            System.out.println("multiplying zero poly by other poly doesn't result in zero poly");

        if(!(test11.multiply(test12).equals(test13))) {
            System.out.println("doesn't multiply polynomials correctly");
            System.out.println(test11.multiply(test12));
            System.out.println(test13);
        }

        if(!quietEnding)
            System.out.println("Rational polynomial tests complete");

    }
}
