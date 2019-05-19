package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import dataStructures.Pair;
import mathClasses.ProductOfPolynomial;
import mathClasses.Rational;
import mathClasses.RationalPolyIterator;
import mathClasses.RationalPolynomial;

import static mathClasses.Rational.R;

public class RationalFactoring extends Operation{

    public RationalFactoring(RationalPolynomial poly){
        super(poly);
        // not sure if I should have that in the constructor, or whether that should be something explicitly done by the programmer
        // compute();
    }

    public void compute(){
        output = factor(firstPoly);
    }

    public ProductOfPolynomial getOutput(){
        return (ProductOfPolynomial) output;
    }

    private static ProductOfPolynomial factor(RationalPolynomial polynomial){
        // this uses the rational roots test saying that every possible factor must be of the form r/s
        // where r divides the constant term and s divides the highest order term

        // integerize the polynomial
        Pair<RationalPolynomial, Long> integerized = polynomial.integerize();
        Long scalerTerm = integerized.getSecond();
        RationalPolynomial integerPoly = integerized.getFirst();

        if(polynomial.getDegree() == 0){
            // return a polynomial that's just the constant term
            return new ProductOfPolynomial(Rational.toLong(integerPoly.getFirst()));
        }

        if(eisenstein(integerPoly) != -1L){
            // if it's irreducible by eisenstiens criterion
            return new ProductOfPolynomial(scalerTerm, integerPoly.copy());
        }

        Rational constant = integerPoly.getFirst();
        Rational highestOrder = integerPoly.getLast();

        Rational potentialFactor;
        DoublyLinkedList<Long> constantFactors = allFactors(Rational.toLong(constant));
        DoublyLinkedList<Long> highestOrderFactors = allFactors(Rational.toLong(highestOrder));
        DoublyLinkedListIterator<Long> constantIterator = constantFactors.getIterator();
        DoublyLinkedListIterator<Long> highestIterator = highestOrderFactors.getIterator();
        constantIterator.goFirst();

        RationalPolynomial factor;
        ProductOfPolynomial factorization = new ProductOfPolynomial(scalerTerm);
        Rational zero = new Rational(0,1);

        // loop through every possible factor
        while(!constantIterator.isAfter()){
            highestIterator.goFirst();
            while(!highestIterator.isAfter()){
                // the potentialFactor is r/s from before
                potentialFactor = new Rational(constantIterator.getCurrentNode().item(), highestIterator.getCurrentNode().item());
                while(integerPoly.solve(potentialFactor).equals(zero)){
                    // then the new factor is (x - potentialFactor) by the factor theorem
                    factor = new RationalPolynomial(zero.subtract(potentialFactor), new Rational(1,1));
                    factorization.insertFactor(factor);
                    integerPoly = integerPoly.divide(factor);
                }

                highestIterator.goForth();
            }
            constantIterator.goForth();
        }
        return factorization;
    }

    /**
     * checks if a polynomial with integer coefficients satisfies eisenstein's criterion.
     * @param polynomial non-null RationalPolynomial
     * @precond rational polynomial must have integer coefficients. polynomial can't be null
     * @return -1 if the polynomial doesn't satisfy the criterion. Otherwise it gives the lowest prime number for which the criterion is satisfied
     */
    public static long eisenstein(RationalPolynomial polynomial){
        if(polynomial.isNull())
            throw new IllegalStateException("Cannot factor a null polynomial");

        RationalPolyIterator iterator = polynomial.getIterator();

        iterator.goFirst();
        while(!iterator.isAfter()){
            if(iterator.getCurrentRational().getDenom() != 1){
                throw new IllegalStateException("Cannot apply eisenstien's criterion with non-integer coefficients");
            }
            iterator.goFirst();
        }

        polynomial.goFirst();
        iterator.goFirst();
        Long constant = Rational.toLong(iterator.getCurrentRational());
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
                if(Rational.toLong(iterator.getCurrentRational()) % prime != 0){
                    continueLoop = true;
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

    private static DoublyLinkedList<Long> allFactors(long integer){
        DoublyLinkedList<Long> factorList = new DoublyLinkedList<>();
        for (long i = 1; i <= integer; i++) {
            if(integer % i == 0){
                factorList.insert(i);
            }
        }
        return factorList;
    }
}
