package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
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

    private static int eisenstein(RationalPolynomial polynomial){
        return 0;
    }

    private static DoublyLinkedList<Long> factorize(long integer){
        if(integer < 1)
            throw new ArithmeticException("Cannot factorize number less than zero");
        int[] arrayPrimeNumbers = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211};
        DoublyLinkedList<Long> factors = new DoublyLinkedList<>();
        long brokenDownInt = integer;
        int i = 0;
        while(brokenDownInt != arrayPrimeNumbers[i] && arrayPrimeNumbers[i] != 211){
            while(brokenDownInt % arrayPrimeNumbers[i] == 0){
                factors.insert((long)arrayPrimeNumbers[i]);
                brokenDownInt /= arrayPrimeNumbers[i];
            }
            i++;
        }
        // we now have a guarantee that every factor from here is between 211, and integer/211
        for (long j = 211; j <= integer/211; j++) {
            while(brokenDownInt % j == 0){
                factors.insert(j);
                brokenDownInt /= j;
            }
        }
        return factors;
    }
}
