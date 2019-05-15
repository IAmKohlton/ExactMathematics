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

    private static DoublyLinkedList<Long> factorize(long integer){
        DoublyLinkedList<Long> factorList = new DoublyLinkedList<>();
        for (int i = 1; i <= integer; i++) {
            if(integer % i == 0){
                factorList.insert((long)i);
            }
        }
        return factorList;
    }
}
