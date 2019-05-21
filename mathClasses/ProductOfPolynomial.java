package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import mathClasses.RationalOperations.RationalOperationOutput;

public class ProductOfPolynomial implements RationalOperationOutput {
    protected DoublyLinkedList<RationalPolynomial> listOfPolys;
    Rational constant;

    public ProductOfPolynomial(Rational constant, RationalPolynomial ... polys){
        this.constant = constant;
        listOfPolys = new DoublyLinkedList<>();
        for(RationalPolynomial x : polys){
            listOfPolys.insert(x);
        }
    }

    public RationalPolynomial multiplyTogether(){
        DoublyLinkedListIterator<RationalPolynomial> iterator = listOfPolys.getIterator();
        if(listOfPolys.getSize() == 0){
            return new RationalPolynomial(constant);
        }
        iterator.goFirst();
        RationalPolynomial product = new RationalPolynomial(new Rational(1,1));
        while(!iterator.isAfter()){
            product = product.multiply(iterator.item());
            iterator.goForth();
        }
        product = product.scale(constant);
        return product;
    }

    public boolean equals(ProductOfPolynomial other){
        // TODO more efficient check where we compare the factors against eachother
        // for now we'll just multiply each together
        RationalPolynomial first = this.multiplyTogether();
        RationalPolynomial second = other.multiplyTogether();

        return first.equals(second);
    }

    public int getSize(){
        return listOfPolys.getSize();
    }

    public Rational getConstant() {
        return constant;
    }

    public void setConstant(Rational constant) {
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

    public boolean isAfter(){
        return listOfPolys.isAfter();
    }

    public RationalPolynomial getFactor(){
        return listOfPolys.item().item();
    }

    public String toString(){
        String outputString = constant.toString() + "\n";
        this.goFirst();
        while(!this.isAfter()){
            outputString += getFactor().toString() + "\n";
            this.goForth();
        }
        return outputString;
    }

}
