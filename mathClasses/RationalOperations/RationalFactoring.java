package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import dataStructures.Pair;
import mathClasses.Rational;
import mathClasses.RationalPolyIterator;
import mathClasses.RationalPolynomial;

public class RationalFactoring extends Operation{

    public RationalFactoring(RationalPolynomial poly){
        super(poly);
    }

    public void compute(){
    }

    private static DoublyLinkedList<RationalPolynomial> factor(){
        return null;
    }

    private static long eisenstein(RationalPolynomial polynomial){
        RationalPolyIterator iterator = polynomial.getIterator();
        polynomial.goFirst();
        Long constant = Rational.toLong(iterator.getCurrentRational());
        DoublyLinkedList<Pair<Long, Integer>> primeDivisorsOfConstant = primeFactors(constant);
        Long prime;
        primeDivisorsOfConstant.goFirst();

        boolean continueLoop;

        // the basic flow of this is that if we look at every prime, and check them against the given criteria one by one
        // if they don't follow a criterion then we 'continue'
        // if we find a prime that follows all criteria then we know that it's irreducible
        while(!primeDivisorsOfConstant.isAfter()){
            primeDivisorsOfConstant.goForth();
            prime = primeDivisorsOfConstant.item().item().getFirst(); // lots of nested data structures *sigh*

            // if the highest order term divides the prime then it doesn't satisfy the criterion
            if(Rational.toLong(polynomial.getLast()) % prime == 0){
                continue;
            }

            // if the lowest order term divides the prime squared then it doesn't satisfy the criterion
            if(Rational.toLong(polynomial.getFirst()) % (prime * prime) == 0){
                continue;
            }

            iterator.goForth();
            // if all the terms but the last term also divide the polynomial then it satisfies the last criterion
            continueLoop = false;
            while(!iterator.isLast()){
                if(Rational.toLong(iterator.getCurrentRational()) % prime != 0){
                    continueLoop = true;
                    break;
                }
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
        while(brokenDownInt != arrayPrimeNumbers[i] && arrayPrimeNumbers[i] != 211){
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
        for (int i = 1; i <= integer; i++) {
            if(integer % i == 0){
                factorList.insert((long)i);
            }
        }
        return factorList;
    }

}
