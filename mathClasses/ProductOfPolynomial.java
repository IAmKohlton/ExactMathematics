package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import mathClasses.RationalOperations.RationalOperationOutput;

public class ProductOfPolynomial implements RationalOperationOutput {
    protected DoublyLinkedList<RationalPolynomial> listOfPolys;
    Long constant;

    public ProductOfPolynomial(Long constant, RationalPolynomial ... polys){
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

    public Long getConstant() {
        return constant;
    }

    public void setConstant(Long constant) {
        this.constant = constant;
    }

    public void insertFactor(RationalPolynomial poly){
        listOfPolys.insert(poly);
    }

    public void goFirst(){
        listOfPolys.goFirst();
    }

    public void goForth(){
        listOfPolys.goForth();
    }

    public void goLast(){
        listOfPolys.goLast();
    }

    public void goBack(){
        listOfPolys.goBack();
    }

    public RationalPolynomial getFactor(){
        return listOfPolys.item().item();
    }

}
