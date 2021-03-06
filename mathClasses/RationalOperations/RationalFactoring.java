package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import dataStructures.Pair;
import mathClasses.ProductOfPolynomial;
import mathClasses.Rational;
import mathClasses.RationalPolyIterator;
import mathClasses.RationalPolynomial;

/**
 * Factors a polynomial
 * Only words if there is only one factor in the polynomial that is greater than degree 1 :(
 */
public class RationalFactoring extends Operation{

    /**
     * constructs the operation
     * @param poly input RationalPolynomial
     */
    public RationalFactoring(RationalPolynomial poly){
        super(poly);
        // not sure if I should have that in the constructor, or whether that should be something explicitly done by the programmer
        // compute();
    }

    /**
     * factors the polynomial and stores the result in 'output'
     */
    public void compute(){
        output = factor();
    }

    /**
     * returns the output of the operation
     * @return ProductOfPolynomial output
     */
    public ProductOfPolynomial getOutput(){
        return (ProductOfPolynomial) output;
    }

    /**
     * Factors the first input polynomial based on the Rational Roots Test which
     * @return ProductOfPolynomials representing the factorization
     */
    private ProductOfPolynomial factor(){
        // this uses the rational roots test saying that every possible factor must be of the form r/s
        // where r divides the constant term and s divides the highest order term
        // this gives is a finite number of possible roots to test

        if(firstPoly.isNull()) {
            throw new IllegalStateException("Cannot factor an empty polynomial");
        }

        // integerize the polynomial
        Pair<RationalPolynomial, Long> integerized = firstPoly.integerize();
        Rational scalerTerm = new Rational(integerized.getSecond(),1);
        RationalPolynomial integerPoly = integerized.getFirst();

        // if it's equal to zero return the zero Product
        if(firstPoly.equals(new RationalPolynomial(new Rational(0)))){
            return new ProductOfPolynomial(new Rational(0));
        }

        // if it's of degree zero return a constant
        if(firstPoly.getDegree() == 0){
            // return a polynomial that's just the constant term
            return new ProductOfPolynomial(integerPoly.getFirst());
        }

        // if it satisfies eisenstein's criterion then it's irreducible
        if(eisenstein(integerPoly) != -1L){
            // if it's irreducible by eisenstiens criterion
            return new ProductOfPolynomial(scalerTerm, integerPoly.copy());
        }

        // first we get rid of any factors of x
        Rational potentialFactor;
        RationalPolynomial factor;
        ProductOfPolynomial factorization = new ProductOfPolynomial(scalerTerm.getInverse());
        RationalPolynomial x = new RationalPolynomial(new Rational(0), new Rational(1));
        while(integerPoly.getFirst().equals(new Rational(0))){
            factorization.insertFactor(x);
            integerPoly = integerPoly.divide(x);
        }

        // get all numbers that divide the constant term and the highest order terms
        Rational constant = integerPoly.getFirst();
        Rational highestOrder = integerPoly.getLast();

        DoublyLinkedList<Long> constantFactors = allDivisors(Rational.toLong(constant));
        DoublyLinkedList<Long> highestOrderFactors = allDivisors(Rational.toLong(highestOrder));
        DoublyLinkedListIterator<Long> constantIterator = constantFactors.getIterator();
        DoublyLinkedListIterator<Long> highestIterator = highestOrderFactors.getIterator();
        constantIterator.goFirst();



        Rational zero = new Rational(0,1);
        // loop through every possible factor
        while(!constantIterator.isAfter()){
            highestIterator.goFirst();
            while(!highestIterator.isAfter()){
                // the potentialFactor is r/s from before
                potentialFactor = new Rational(constantIterator.getCurrentNode().item(), highestIterator.getCurrentNode().item());
                // only enters loop if f(constant) = zero
                // by the factor theorem this tells us (x - potentialFactor) is a factor
                while(integerPoly.solve(potentialFactor).equals(zero)){
                    factor = new RationalPolynomial(zero.subtract(potentialFactor), new Rational(1,1));
                    factorization.insertFactor(factor);
                    integerPoly = integerPoly.divide(factor);
                }

                highestIterator.goForth();
            }
            constantIterator.goForth();
        }

        // cleanup. could technically get rid of this if I refactored the above code
        if(integerPoly.getDegree() == 0){
            Rational prevConstant = factorization.getConstant();
            factorization.setConstant(prevConstant.multiply(integerPoly.getFirst()));
        }else{
            factorization.insertFactor(integerPoly);
        }
        return factorization;
    }

    /**
     * checks if a polynomial with integer coefficients satisfies eisenstein's criterion.
     * @param polynomial non-null RationalPolynomial
     * precond: rational polynomial must have integer coefficients. polynomial can't be null
     * @return -1 if the polynomial doesn't satisfy the criterion. Otherwise it gives the lowest prime number for which the criterion is satisfied
     */
    public static long eisenstein(RationalPolynomial polynomial){
        // eisenstein's criterion says that a polynomial is irreducible if a prime number p exists that satisfy the following:
        // p divides every term except the highest order term
        // p does not divide the highest order term
        // p^2 does not divide the constant factor
        // through this theorem we have a relatively quick way of telling if something is irreducible
        // the only drawback is that the converse of the statement isn't true
        // that means that if a polynomial is irreducible it doesn't necessarily satisfy eisenstein's criterion

        if(polynomial.isNull())
            throw new IllegalStateException("Cannot factor a null polynomial");

        RationalPolyIterator iterator = polynomial.getIterator();

        // check that the eisenstein's criterion can even be applied
        iterator.goFirst();
        while(!iterator.isAfter()){
            if(iterator.currentRational().getDenom() != 1){
                throw new IllegalStateException("Cannot apply eisenstien's criterion with non-integer coefficients");
            }
            iterator.goForth();
        }

        polynomial.goFirst();
        iterator.goFirst();
        long constant = Rational.toLong(iterator.currentRational());
        constant = constant > -constant ? constant : -constant;
        if(constant == 0){
            // if the constant is 0 then it can be factored by x which guarantees that eisenteins criterion doesn't apply
            return -1L;
        }
        DoublyLinkedList<Pair<Long, Integer>> primeDivisorsOfConstant = primeFactors(constant);
        Long prime;
        primeDivisorsOfConstant.goFirst();

        boolean continueLoop;

        // the basic flow of this is that if we look at every prime, and check them against the given criteria one by one
        // if they don't follow a criterion then we 'continue'
        // if we find a prime that follows all criteria then we know that it's irreducible
        while(!primeDivisorsOfConstant.isAfter()){

            prime = primeDivisorsOfConstant.item().item().getFirst(); // lots of nested data structures *sigh*
            primeDivisorsOfConstant.goForth();
            // if the highest order term divides the prime then it doesn't satisfy the criterion
            if(Rational.toLong(polynomial.getLast()) % prime == 0){
                continue;
            }

            // if the lowest order term divides the prime squared then it doesn't satisfy the criterion
            if(Rational.toLong(polynomial.getFirst()) % (prime * prime) == 0){
                continue;
            }

            iterator.goFirst();
            // if all the terms but the last term also divide the polynomial then it satisfies the last criterion
            continueLoop = false;
            while(!iterator.isLast()){
                if(Rational.toLong(iterator.currentRational()) % prime != 0){
                    continueLoop = true; // need to do this since we have no other way to 'break' this loop and subsequently 'continue' the above loop
                    break;
                }
                iterator.goForth();
            }
            if(continueLoop){
                continue;
            }

            return prime;
        }
        return -1;
    }

    /**
     * finds all the prime numbers of a given integer
     * @param integer int we are factoring
     * @return DoublyLinkedList<Pair<Long,Integer>> where each element of the list gives the prime, and the exponent that it's raised to
     */
    private static DoublyLinkedList<Pair<Long,Integer>> primeFactors(long integer){
        if(integer < 1)
            throw new ArithmeticException("Cannot factorize number less than zero");
        int[] arrayPrimeNumbers = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211};
        DoublyLinkedList<Pair<Long,Integer>> factors = new DoublyLinkedList<>();
        long brokenDownInt = integer;
        int i = 0;
        int power;
        Pair<Long, Integer> factorPowerCombo;
        while(brokenDownInt != 1 && arrayPrimeNumbers[i] != 211){
            power = 0;
            while(brokenDownInt % arrayPrimeNumbers[i] == 0){
                power++;
                brokenDownInt /= arrayPrimeNumbers[i];
            }
            factorPowerCombo = new Pair<>((long)arrayPrimeNumbers[i], power);
            factors.insert(factorPowerCombo);
            i++;
        }
        // we now have a guarantee that every factor from here is between 211, and integer/211
        for (long j = 211; j <= integer/211; j++) {
            power = 0;
            while(brokenDownInt % j == 0){
                power++;
                brokenDownInt /= j;
            }
            factorPowerCombo = new Pair<>((long)arrayPrimeNumbers[i], power);
            factors.insert(factorPowerCombo);
        }

        if(factors.getSize() == 0){
            factors.insert(new Pair<>(integer, 1)); // if it's prime then it's only factor is itself
        }

        return factors;
    }

    /**
     * get all divisors of a given number
     * @param integer int we find divisors of
     * @return DoublyLinkedList of divisors
     */
    private static DoublyLinkedList<Long> allDivisors(long integer){
        DoublyLinkedList<Long> factorList = new DoublyLinkedList<>();
        integer = integer > -integer ? integer : -integer;
        // loop through all integers between 1 and 'integer' and see if each of them divides 'integer' evenly
        for (long i = 1; i <= integer; i++) {
            if(integer % i == 0){
                factorList.insert(i);
                factorList.insert(-i);
            }
        }
        return factorList;
    }
}
