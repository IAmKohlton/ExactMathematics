package mathClasses.RationalOperations;

import dataStructures.DoublyLinkedList;
import dataStructures.Pair;
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

    public static void main(String[] args){
        System.out.println(factorize(2));
        System.out.println(factorize(8));
        System.out.println(factorize(360));
        System.out.println(factorize(4));
        System.out.println(factorize(4));
    }
}
