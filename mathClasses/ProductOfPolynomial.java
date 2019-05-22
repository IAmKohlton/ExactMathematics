package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import mathClasses.RationalOperations.RationalOperationOutput;

/**
 * product of many distinct RationalPolynomials
 */
public class ProductOfPolynomial implements RationalOperationOutput {
    /**
     * Collections of all polynomials in the full producty
     */
    private DoublyLinkedList<RationalPolynomial> listOfPolys;

    /**
     * Constant by which this polynomial is scaled by
     */
    private Rational constant;

    /**
     * Constructs a new ProductOfPolynomials based on a constant, and a chain of polynomials
     * @param constant Rational scaler
     * @param polys sequence of polynomials
     */
    public ProductOfPolynomial(Rational constant, RationalPolynomial ... polys){
        this.constant = constant;
        listOfPolys = new DoublyLinkedList<>();
        for(RationalPolynomial x : polys){
            listOfPolys.insert(x);
        }
    }

    /**
     * multiply all the polynomials in this product together
     * @return RationalPolynomial representing the product of all terms
     */
    public RationalPolynomial multiplyTogether(){
        DoublyLinkedListIterator<RationalPolynomial> iterator = listOfPolys.getIterator();
        if(listOfPolys.getSize() == 0){
            return new RationalPolynomial(constant);
        }

        // iterate through all polynomials and multiply them all into the final product
        iterator.goFirst();
        RationalPolynomial product = new RationalPolynomial(constant);
        while(!iterator.isAfter()){
            product = product.multiply(iterator.item());
            iterator.goForth();
        }
        return product;
    }

    public boolean equals(ProductOfPolynomial other){
        // TODO more efficient check where we compare the factors against eachother
        // for now we'll just multiply each together and check if they're equal
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
