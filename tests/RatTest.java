package tests;

import mathClasses.Rational;

import static mathClasses.Rational.makeNegativeInfinity;
import static mathClasses.Rational.makePositiveInfinity;

public class RatTest {
    public static void main(String[] args){
        test(false);
    }


    public static void test(boolean quietSuccess){

        Rational twoThirds = new Rational(2,3);
        Rational oneThird = new Rational(1,3);
        Rational one = new Rational(1);
        Rational oneHalf = new Rational(1,2);
        Rational negOneHalf = new Rational(-1, 2);
        Rational inf = makePositiveInfinity();
        Rational negInf = makeNegativeInfinity();

        // test constructors, and equals
        if(!twoThirds.equals(twoThirds))
            System.out.println("two thirds != two thirds");

        if(!twoThirds.equals(new Rational(4, 6)))
            System.out.println("two thirds != four sixths");

        if(!one.equals(new Rational(2,2)))
            System.out.println("two halves != one");

        if(one.equals(new Rational(-1)))
            System.out.println("-1 = 1");

        if(!inf.equals(inf))
            System.out.println("inf != inf");

        if(inf.equals(negInf))
            System.out.println("inf = negInf");

        // test add
        if(!twoThirds.add(oneThird).equals(one))
            System.out.println("two thirds plus one third isn't one");

        if(!oneThird.add(oneThird).equals(twoThirds))
            System.out.println("one third plus one third isn't two thirds");

        if(!oneThird.add(twoThirds).equals(one))
            System.out.println("one third plus two thirds isn't one");

        if(!oneThird.add(oneHalf).equals(new Rational(5,6)))
            System.out.println("one half plus one third isn't five sixths");

        if(!inf.add(inf).equals(inf))
            System.out.println("inf + inf != inf");

        boolean caught = false;
        try{
            inf.add(negInf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw inf - inf exception");

        caught = false;
        try{
            negInf.add(inf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw inf - inf exception");

        // testing subtract
        if(!twoThirds.subtract(oneThird).equals(oneThird))
            System.out.println("2/3 - 1/3 != 1/3");

        if(!oneThird.subtract(oneThird).equals(new Rational(0)))
            System.out.println("1/3 - 1/3 != 0");

        if(!oneThird.subtract(twoThirds).equals(new Rational(-1, 3)))
            System.out.println("1/3 - 2/3 != -1/3");

        if(!oneThird.subtract(oneHalf).equals(new Rational(-1,6)))
            System.out.println("1/3 - 1/2 != -1/6");

        if(!inf.subtract(negInf).equals(inf))
            System.out.println("inf - -inf != inf");

        caught = false;
        try{
            inf.subtract(inf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw inf - inf exception");

        caught = false;
        try{
            negInf.subtract(negInf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw -inf - -inf exception");

        // test multiply
        if(!twoThirds.multiply(oneThird).equals(new Rational(2,9)))
            System.out.println("1/3 * 2/3 != 2/9");

        if(!oneThird.multiply(oneThird).equals(new Rational(1,9)))
            System.out.println("1/3 * 1/3 != 1/9");

        if(!oneThird.multiply(negOneHalf).equals(new Rational(-1, 6)))
            System.out.println("1/3 * -1/2 != -1/6");

        if(!negOneHalf.multiply(negOneHalf).equals(new Rational(1,4)))
            System.out.println("-1/2 * -1/2 != 1/4");

        if(!inf.multiply(negInf).equals(negInf))
            System.out.println("inf * -inf != -inf");

        caught = false;
        try{
            inf.multiply(new Rational(0));
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw inf * 0 exception");

        caught = false;
        try{
            negInf.subtract(negInf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw -inf * 0 exception");

        // testing division
        if(!twoThirds.divide(oneThird).equals(new Rational(2))) {
            System.out.println(twoThirds.divide(oneThird));
            System.out.println("2/3 / 1/3 != 2");
        }

        if(!oneThird.divide(oneThird).equals(new Rational(1)))
            System.out.println("1/3 / 1/3 != 1/9");

        if(!oneThird.divide(negOneHalf).equals(new Rational(-2, 3)))
            System.out.println("1/3 / -1/2 != -2/3");

        if(!negOneHalf.divide(negOneHalf).equals(new Rational(1)))
            System.out.println("-1/2 / -1/2 != 1");


        caught = false;
        try{
            one.divide(new Rational(0));
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw 1 / 0 exception");

        caught = false;
        try{
            inf.divide(inf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught)
            System.out.println("didn't throw inf / inf exception");

        // now testing compareTo
        if(!(oneThird.compareTo(oneHalf) < 0))
            System.out.println("1/3 !< 1/2");

        if((oneThird.compareTo(oneHalf) > 0))
            System.out.println("1/3 > 1/2");

        if(!(oneThird.compareTo(twoThirds) < 0))
            System.out.println("1/3 !< 2/3");

        if(!(inf.compareTo(negInf) > 0))
            System.out.println("inf !> -inf");

        if(!(inf.compareTo(oneHalf) > 0))
            System.out.println("inf !> 1/2");

        if(!(negInf.compareTo(oneHalf) < 0))
            System.out.println("-inf !< 1/2");

        // now testing increment

        Rational threeHalfs = new Rational(3, 2);
        threeHalfs.increment(new Rational(3,4));
        Rational nineQuarters = new Rational(9, 4);
        if(!(threeHalfs.equals(nineQuarters)))
            System.out.println("(3/2 += 3/4) != 9/4");

        Rational ze = new Rational(0);
        ze.increment(nineQuarters);
        if(!(ze.equals(nineQuarters)))
            System.out.println("(0 += 9/4 != 9/4");

        Rational infi = makePositiveInfinity();
        infi.increment(new Rational(-1000000000,3));
        if(!(infi.equals(makePositiveInfinity())))
            System.out.println("inf += -bigNumber != inf");

        // now testing power

        Rational newOne = one.power(1000);
        if(!(newOne.equals(one)))
            System.out.println("1^k != 1");

        Rational three = oneThird.power(-1);
        if(!(three.equals(new Rational(3)))) {
            System.out.println("1/3^-1 != 3");
            System.out.println(oneThird);
            System.out.println(three);
        }

        Rational threeFifths = new Rational(3,5);
        Rational bigNumbers = threeFifths.power(4);
        if(!(bigNumbers.equals(new Rational(81, 625))))
            System.out.println("(3/5)^4 != 81/625");

        if(!quietSuccess)
            System.out.println("Rational test complete");

    }
}
