package tests;

import mathClasses.ProductOfPolynomial;
import mathClasses.RationalOperations.RationalFactoring;
import mathClasses.RationalPolynomial;

import static mathClasses.Rational.R;
import static mathClasses.RationalOperations.RationalFactoring.eisenstein;

public class FactoringTest {
    public static void main(String[] args){test(false);}
    public static void test(boolean quietEnding){
        RationalPolynomial positiveTest = new RationalPolynomial(R(2,1), R(4,1), R(6,1), R(3,1));
        long prime = eisenstein(positiveTest);
        if(prime != 2L)
            System.out.println("Doesn't find p=2");

        RationalPolynomial negativeTest = new RationalPolynomial(R(4,1), R(4,1), R(6,1), R(8,1));
        prime = eisenstein(negativeTest);
        if(prime != -1L)
            System.out.println("Thinks the constant term doesn't divide the prime squared");

        RationalPolynomial negativeTest2 = new RationalPolynomial(R(2,1), R(4,1), R(6,1), R(2,1));
        prime = eisenstein(negativeTest2);
        if(prime != -1L)
            System.out.println("Thinks the highest order term doesn't divide the prime");

        RationalPolynomial negativeTest3 = new RationalPolynomial(R(2,1), R(3,1), R(4,1), R(3,1));
        prime = eisenstein(negativeTest3);
        if(prime != -1L)
            System.out.println("Thinks all medium order terms divide 2");

        RationalPolynomial negativeTest4 = new RationalPolynomial(R(3,1), R(2,1), R(4,1), R(3,1));
        prime = eisenstein(negativeTest4);
        if(prime != -1L)
            System.out.println("Thinks eisensteins criterion applies");

        RationalPolynomial positiveTest2 = new RationalPolynomial(R(6,1), R(2,1), R(4,1), R(5,1));
        prime = eisenstein(positiveTest2);
        if(prime != 2L)
            System.out.println("doesn't think eisensteins criterion applies with p=2");

        RationalPolynomial positiveTest3 = new RationalPolynomial(R(6,1), R(3,1), R(6,1), R(5,1));
        prime = eisenstein(positiveTest3);
        if(prime != 3L) {
            System.out.println(prime);
            System.out.println("doesn't think the criterion applies with p=3");
        }

        // now test all the positive cases against
        RationalFactoring irreducible1 = new RationalFactoring(positiveTest);
        irreducible1.compute();
        ProductOfPolynomial irreducibleProduct1 = irreducible1.getOutput();
        ProductOfPolynomial irreducibleProductExpected1 = new ProductOfPolynomial(1L, positiveTest);
        if (!(irreducibleProductExpected1.equals(irreducibleProduct1))){
            System.out.println("Didn't properly factorize an irreducible polynomial");
        }

        RationalFactoring irreducible2 = new RationalFactoring(positiveTest2);
        irreducible2.compute();
        ProductOfPolynomial irreducibleProduct2 = irreducible2.getOutput();
        ProductOfPolynomial irreducibleProductExpected2 = new ProductOfPolynomial(1L, positiveTest);
        if(!(irreducibleProductExpected2.equals(irreducibleProduct2))){
            System.out.println("Didn't properly factorize an irreducible polynomial");
        }
    }
}
