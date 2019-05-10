package tests;

public class TestAll {
    public static void main(String[] args){
        boolean quietSuccess = false;
        DoublyLinkedListTest.test(quietSuccess);
        RatTest.test(quietSuccess);
        RatPolyTest.test(quietSuccess);
    }
}
