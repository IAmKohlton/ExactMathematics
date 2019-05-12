package dataStructures;

public class DoublyLinkedListIterator<I> {
    protected DoublyLinkedNode currentNode;

    protected DoublyLinkedList<I> list;

    public boolean isFirst(){
        return list.getHead() == currentNode;
    }

    public boolean isLast(){
        return list.getTail() == currentNode;
    }

    public boolean isAfter(){
        return currentNode == null;
    }

    public void goFirst(){
        if(list.getSize() == 0)
            throw new IllegalStateException("Cannot go to the first element of an empty list");

        currentNode = list.getHead();
    }

    public void goLast(){
        if(list.getSize() == 0)
            throw new IllegalStateException("Cannot go to the first element of an empty list");

        currentNode = list.getTail();
    }

    public void goForth(){
        if(list.getSize() == 0)
            throw new IllegalStateException("Cannot go forth on an empty list");

        if(this.isAfter())
            throw new IllegalStateException("Cannot go to the next item while already after");

        currentNode = currentNode.getNext();
    }

    public void goBack(){
        if(list.getSize() == 0)
            throw new IllegalStateException("Cannot go back on an empty list");

        if(list.getHead() == currentNode)
            throw new IllegalStateException("Cannot go before the first element");

        currentNode = currentNode.getNext();
    }

    public void goToIth(int i){
        this.goFirst();
        if(i > list.getSize()){
            throw new IllegalStateException("Cannot go past the end of a list");
        }

        for (int j = 0; j < i; j++) {
            this.goForth();
        }
    }

}
