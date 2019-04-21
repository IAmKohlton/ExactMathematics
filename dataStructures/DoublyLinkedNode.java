package dataStructures;

public class DoublyLinkedNode<I> {
    I item;

    DoublyLinkedNode<I> next;

    DoublyLinkedNode<I> prev;

    public DoublyLinkedNode(I item){
        this.setItem(item);
    }

    public DoublyLinkedNode(){
        this.setItem(null);
    }

    public I item() {
        return item;
    }

    public void setItem(I item) {
        this.item = item;
    }

    public DoublyLinkedNode<I> getNext() {
        return next;
    }

    public void setNext(DoublyLinkedNode<I> next) {
        this.next = next;
    }

    public DoublyLinkedNode<I> getPrev() {
        return prev;
    }

    public void setPrev(DoublyLinkedNode<I> prev) {
        this.prev = prev;
    }

    public String toString(){
        return item.toString();
    }
}
