package dataStructures;

public class DoublyLinkedList<I> {
    protected DoublyLinkedNode<I> head;

    protected DoublyLinkedNode<I> tail;

    protected DoublyLinkedNode<I> cursor;

    protected int size;

    public DoublyLinkedList(){
        head = null;
        tail = null;
        cursor = null;
        size = 0;
    }

    public int getSize(){return size;}

    public DoublyLinkedNode<I> item(){
        if(this.isAfter()){
            throw new IllegalStateException("Can't get item while in the after position");
        }
        return cursor;
    }

    public DoublyLinkedNode<I> getHead(){
        return head;
    }

    public DoublyLinkedNode<I> getTail(){
        return tail;
    }

    public boolean isFirst(DoublyLinkedNode<I> item){
        return item == head;
    }

    public boolean isFirst(){return cursor == head; }

    public boolean isLast(DoublyLinkedNode<I> item){
        return item == tail;
    }

    public boolean isLast(){return cursor == tail;}

    public boolean isAfter(){return cursor == null;} // will allow for cursor to be after, but not before

    /**
     * insert a new node into the list, and move the cursor to the next item
     * @param item the item we want to insert into the list
     */
    public void insert(I item){
        DoublyLinkedNode<I> newNode = new DoublyLinkedNode<>(item);

        if(size == 0){
            head = newNode;
            tail = newNode;
        }else if(isLast(this.item())){ // if the current item is the last item
            this.item().setNext(newNode);
            newNode.setPrev(this.item());
            tail = newNode;
        }else{ // if the current item isn't the last in the list
            DoublyLinkedNode<I> currentNext = this.item().getNext();
            currentNext.setPrev(newNode);
            newNode.setNext(currentNext);

            this.item().setNext(newNode);
            newNode.setPrev(this.item());
        }

        cursor = newNode;
        size++;
    }

    public void insertFirst(I item){
        if(size == 0){
            insert(item);
        }else{
            DoublyLinkedNode<I> newNode = new DoublyLinkedNode<>(item);
            head.setPrev(newNode);
            newNode.setNext(head);
            head = newNode;
            size++;
        }
    }

    /**
     * delete the current item the cursor is on
     */
    public void delete(){
        if(size == 0){
            throw new IllegalStateException("Cannot delete from an empty list");
        }else if(size == 1){
            head = null;
            tail = null;
            cursor = null;
        }else if(isFirst(this.item())){
            DoublyLinkedNode<I> nextNode = this.item().getNext();
            nextNode.setPrev(null);
            head = nextNode;
            cursor = nextNode;
        }else if(isLast(this.item())){
            DoublyLinkedNode<I> prevNode = this.item().getPrev();
            prevNode.setNext(null);
            tail = prevNode;
            cursor = prevNode;
        }else{
            DoublyLinkedNode<I> prevNode = this.item().getPrev();
            DoublyLinkedNode<I> nextNode = this.item().getNext();
            this.item().setNext(null);
            this.item().setPrev(null);
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
            cursor = nextNode;
        }

        size--;
    }

    public void goFirst(){
        if(size == 0)
            throw new IllegalStateException("Cannot go to the first element of an empty list");

        cursor = head;
    }

    public void goLast(){
        if(size == 0)
            throw new IllegalStateException("Cannot go to the last element of an empty list");

        cursor = tail;
    }

    public void goForth(){
        if(size == 0)
            throw new IllegalStateException("Cannot go forth on an empty list");

        if(this.isAfter())
            throw new IllegalStateException("Cannot go to the next item while already after");

        cursor = this.item().getNext();
    }

    public void goBack(){
        if(size == 0)
            throw new IllegalStateException("Cannot go back on an empty list");

        if(isFirst(this.item()))
            throw new IllegalStateException("Cannot go before the first element");

        cursor = this.item().getPrev();
    }


    public void goToIth(int i){
        this.goFirst();
        if(i > size){
            throw new IllegalStateException("Cannot go past the end of a list");
        }

        for (int j = 0; j < i; j++) {
            this.goForth();
        }
    }

    public DoublyLinkedListIterator<I> getIterator(){
        return getIterator(this.cursor);
    }


    public DoublyLinkedListIterator<I> getIterator(DoublyLinkedNode<I> pos){
        return new DoublyLinkedListIterator<I>(this, pos);
    }

    public String toString(){
        DoublyLinkedNode<I> saveCursor = cursor;

        if(size == 0){
            return "<empty list>";
        }

        this.goFirst();
        String outString = "";

        while(!isAfter()){
            outString += this.item().toString() + ", ";
            this.goForth();
        }

        cursor = saveCursor;
        return outString;
    }

}