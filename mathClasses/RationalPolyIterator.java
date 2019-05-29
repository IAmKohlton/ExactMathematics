package mathClasses;

import dataStructures.DoublyLinkedNode;

/**
 * Class that iterates over a RationalPolynomial
 */
public class RationalPolyIterator{
    protected DoublyLinkedNode<Rational> currentNode;

    protected RationalPolynomial list;

    public RationalPolyIterator(RationalPolynomial list){
        this.list = list;
        currentNode = list.poly.getHead();
    }

    public Rational currentRational(){
        return currentNode.item().clone();
    }

    public boolean isFirst(){
        return list.poly.getHead() == currentNode;
    }

    public boolean isLast(){
        return list.poly.getTail() == currentNode;
    }

    public boolean isAfter(){
        return currentNode == null;
    }

    public void goFirst(){
        if(list.poly.getSize() == 0)
            throw new IllegalStateException("Cannot go to the first element of an empty list");

        currentNode = list.poly.getHead();
    }

    public void goLast(){
        if(list.poly.getSize() == 0)
            throw new IllegalStateException("Cannot go to the first element of an empty list");

        currentNode = list.poly.getTail();
    }

    public void goForth(){
        if(list.poly.getSize() == 0)
            throw new IllegalStateException("Cannot go forth on an empty list");

        if(this.isAfter())
            throw new IllegalStateException("Cannot go to the next item while already after");

        currentNode = currentNode.getNext();
    }

    public void goBack(){
        if(list.poly.getSize() == 0)
            throw new IllegalStateException("Cannot go back on an empty list");

        if(list.poly.getHead() == currentNode)
            throw new IllegalStateException("Cannot go before the first element");

        currentNode = currentNode.getNext();
    }

    public void goToIth(int i){
        this.goFirst();
        if(i > list.poly.getSize()){
            throw new IllegalStateException("Cannot go past the end of a list");
        }

        for (int j = 0; j < i; j++) {
            this.goForth();
        }
    }
}
