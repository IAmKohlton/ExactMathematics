package tests;

import dataStructures.DoublyLinkedList;

public class DoublyLinkedListTest {
    public static void main(String[] args){
        test(false);
    }

    public static void test(boolean quietEnding){
        DoublyLinkedList<Integer> test = new DoublyLinkedList<>();
        test.insert(1);
        test.insert(2);
        test.insert(3);
        test.insert(4);
        test.insert(5);

        test.goBack();
        if(test.item().item() != 4)
            System.out.println("second last item of list isn't 4 when using goBack");

        test.goBack();
        if(test.item().item() != 3)
            System.out.println("third last item of list isn't 3 when using goBack");

        test.goLast();
        if(test.item().item() != 5)
            System.out.println("last item of list isn't 5 when using goLast");

        test.goToIth(1);
        if(test.item().item() != 2)
            System.out.println("second item of list isn't 2");

        test.goToIth(3);
        if(test.item().item() != 4)
            System.out.println("4th item of list isn't 4");

        test.goFirst();
        if(test.item().item() != 1)
            System.out.println("first item of list isn't 1");

        boolean caught = false;
        try{
            test.goToIth(6);
        }catch(IllegalStateException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw exception properly");

        // now try delete stuff
        test.goToIth(2);
        test.delete();
        test.goToIth(0);
        test.delete();
        test.goLast();
        test.delete();


        test.goLast();
        if(test.item().item() != 4)
            System.out.println("last item isn't 4 after deletions");
        if(test.item().getPrev().item() != 2)
            System.out.println("deletion broke connection to 2");

        if(!quietEnding)
            System.out.println("Doubly linked list tests complete");

    }
}
