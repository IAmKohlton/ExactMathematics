package tests;

import mathClasses.ProductOfPolynomial;
import mathClasses.Rational;
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
        ProductOfPolynomial irreducibleProductExpected1 = new ProductOfPolynomial(new Rational(1), positiveTest);
        if (!(irreducibleProductExpected1.equals(irreducibleProduct1))){
            System.out.println("Didn't properly factorize an irreducible polynomial 1");
        }

        RationalFactoring irreducible2 = new RationalFactoring(positiveTest2);
        RationalPolynomial checkForChange = positiveTest2.copy();
        irreducible2.compute();
        ProductOfPolynomial irreducibleProduct2 = irreducible2.getOutput();
        ProductOfPolynomial irreducibleProductExpected2 = new ProductOfPolynomial(new Rational(1), positiveTest2);
        if(!(irreducibleProductExpected2.equals(irreducibleProduct2))){
            System.out.println("Didn't properly factorize an irreducible polynomial 2");
        }

        if(!(checkForChange.equals(positiveTest2)))
            System.out.println("Factoring a polynomial changed the original polynomial");


        // check if (x+1)*(x-1) factors correctly
        RationalPolynomial reducible1 = new RationalPolynomial(R(-1,1), R(0,1), R(1,1));
        RationalPolynomial reducible1factor1 = new RationalPolynomial(R(1,1), R(1,1));
        RationalPolynomial reducible1factor2 = new RationalPolynomial(R(-1,1), R(1,1));
        ProductOfPolynomial reducibleFactorization1Expected = new ProductOfPolynomial(new Rational(1), reducible1factor1, reducible1factor2);
        RationalFactoring reducibleFactorization1 = new RationalFactoring(reducible1);
        reducibleFactorization1.compute();
        ProductOfPolynomial reducibleFactorization1Actual = reducibleFactorization1.getOutput();
        if(!(reducibleFactorization1Actual.equals(reducibleFactorization1Expected))){
            System.out.println("Didn't factor a reducible polynomial correctly");
            System.out.println(reducibleFactorization1Actual);
            System.out.println(reducibleFactorization1Expected);
        }

        reducible1 = new RationalPolynomial(R(5,4), R(113,28), R(59, 28), R(2,7));
        reducible1factor1 = new RationalPolynomial(R(5,4), R(2,7));
        reducible1factor2 = new RationalPolynomial(R(1,1), R(3,1), R(1,1));
        reducibleFactorization1Expected = new ProductOfPolynomial(new Rational(1), reducible1factor1, reducible1factor2);
        reducibleFactorization1 = new RationalFactoring(reducible1);
        reducibleFactorization1.compute();
        reducibleFactorization1Actual = reducibleFactorization1.getOutput();
        if(!(reducibleFactorization1Actual.equals(reducibleFactorization1Expected))){
            System.out.println("Didn't factor an irreducible order two poly times an order one poly");
            System.out.println(reducibleFactorization1Actual);
            System.out.println(reducibleFactorization1Expected);
        }

        reducible1 = new RationalPolynomial(R(3,2), R(6,2), R(3,2));
        reducible1factor1 = new RationalPolynomial(R(1,1), R(2,1), R(1,1));
        reducible1factor2 = new RationalPolynomial(R(3,2));
        reducibleFactorization1Expected = new ProductOfPolynomial(new Rational(1), reducible1factor1, reducible1factor2);
        reducibleFactorization1 = new RationalFactoring(reducible1);
        reducibleFactorization1.compute();
        reducibleFactorization1Actual = reducibleFactorization1.getOutput();
        if(!(reducibleFactorization1Actual.equals(reducibleFactorization1Expected))){
            System.out.println("Didn't factor an irreducible order two poly times an order one poly");
            System.out.println(reducibleFactorization1Actual);
            System.out.println(reducibleFactorization1Expected);
        }

        ProductOfPolynomial expectedResult;
        RationalPolynomial resultOfMultiplication;
        RationalPolynomial term1;
        RationalPolynomial term2;
        ProductOfPolynomial result;
        RationalFactoring resultObject;

        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                for (int k = -5; k < 5; k++) {
                    for (int l = -5; l < 5; l++) {
                        term1 = new RationalPolynomial(R(i,1), R(j,1));
                        term1.unPadPoly();
                        term2 = new RationalPolynomial(R(k,1), R(l,1));
                        term2.unPadPoly();
                        resultOfMultiplication = term1.multiply(term2);
                        resultObject = new RationalFactoring(resultOfMultiplication);

                        resultObject.compute();
                        result = resultObject.getOutput();
                        expectedResult = new ProductOfPolynomial(new Rational(1), term1, term2);
                        if(!(result.equals(expectedResult))){
                            System.out.println(term1);
                            System.out.println("*" );
                            System.out.println(term2);
                            System.out.println("!=");
                            System.out.println(expectedResult);
                            System.out.println("instead");
                            System.out.println(result);
                            System.out.println();

                        }
                    }
                }
            }
        }

        if(!quietEnding){
            System.out.println("Factoring test complete");
        }
    }
}
