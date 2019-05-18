package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import mathClasses.RationalOperations.RationalOperationOutput;

public class ProductOfPolynomial implements RationalOperationOutput {
    DoublyLinkedList<RationalPolynomial> listOfPolys;

    public ProductOfPolynomial(RationalPolynomial ... polys){
        listOfPolys = new DoublyLinkedList<>();
        for(RationalPolynomial x : polys){
            listOfPolys.insert(x);
        }
    }

    public RationalPolynomial multiplyTogether(){
        DoublyLinkedListIterator<RationalPolynomial> iterator = listOfPolys.getIterator();
        RationalPolynomial product = new RationalPolynomial(new Rational(1,1));
        while(!listOfPolys.isAfter()){
            product = product.multiply(listOfPolys.item().item());
            listOfPolys.goForth();
        }
        return product;
    }

    public void insertFactor(RationalPolynomial poly){
        listOfPolys.insert(poly);
    }

}
