package tests;

/**
 * Driver class to run all test classes
 */
public class TestAll {
    public static void main(String[] args){
        Long timeToRun = System.currentTimeMillis();
        boolean quietSuccess = false;
        DoublyLinkedListTest.test(quietSuccess);
        RatTest.test(quietSuccess);
        RatPolyTest.test(quietSuccess);
        FactoringTest.test(quietSuccess);
        System.out.println("All tests completed in: " + (System.currentTimeMillis() - timeToRun)/1000.0);
        System.out.println("All tests complete");
    }
}
