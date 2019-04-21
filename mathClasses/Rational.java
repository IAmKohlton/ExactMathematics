package mathClasses;

public class Rational implements Comparable<Rational> {
    /**
     * numerator for the given rational number
     */
    private long numer;

    /**
     * denominator for the given rational number
     */
    private long denom;

    /**
     * sign for the given rational number. 1 indicates a negative rational, and 0 indicates a positive rational
     */
    private boolean sign;

    /**
     * whether or not the rational is +- infinity
     */
    private boolean infinity;

    /**
     * create a finite rational number
     * @param numerator top of the fraction
     * @param denominator bottom of the fraction
     */
    public Rational(long numerator, long denominator){
        if (denominator == 0) { // denominator can't be zero even though infinity exists
            throw new IllegalStateException("Denominator can't be 0");
        }
        boolean numerSign = 0 > numerator;
        boolean denomSign = 0 > denominator;

        if(numerator == 0){
            sign = false;
        }else{
            sign = numerSign^denomSign; // if exactly one is negative then sign should be one
        }

        long newGcd = gcd(numerator, denominator);

        this.numer = numerator > -numerator ? numerator/newGcd : -numerator/newGcd; // abs of numerator
        this.denom = denominator > -denominator ? denominator/newGcd : -denominator/newGcd; // abs of denominator


        infinity = false;

    }

    /**
     * create a finite rational number
     * @param numerator top of the fraction
     * @param denominator bottom of the fraction
     */
    public Rational(int numerator, int denominator){
        this((long) numerator, (long) denominator);
    }

    /**
     * Constructor to create an integer of type rational
     * @param numerator given integer
     */
    public Rational(int numerator){
        this(numerator, 1);
    }

    /**
     * Creates a positive infinity rational number
     * @return positive infinity
     */
    public static Rational makePositiveInfinity(){
        Rational temp = new Rational(0,1);
        temp.infinity = true;
        temp.sign = false;
        return temp;
    }

    /**
     * Creates a negative infinity rational number
     * @return negative infinity
     */
    public static Rational makeNegativeInfinity(){
        Rational temp = new Rational(0,1);
        temp.infinity = true;
        temp.sign = true;
        return temp;
    }

    /**
     * get the numerator of this rational
     * @return numerator
     */
    public long getNumer(){
        return numer;
    }

    /**
     * get the denominator of this rational
     * @return denominator
     */
    public long getDenom(){
        return denom;
    }

    /**
     * get the sign of the rational
     * @return true if negative, false if positive
     */
    public boolean getSign(){
        return sign;
    }

    /**
     * gets whether the rational is infinite or not
     * @return true if infinite, false if finite
     */
    public boolean isInfinity(){
        return infinity;
    }

    /**
     * get the absolute value of this
     * @return absolute value of this
     */
    public Rational abs(){
        return new Rational(this.getNumer(), this.getDenom());
    }

    /**
     * get the multiplicitave inverse of this
     * @return inverse of this
     */
    public Rational getInverse(){
        int thisSign = this.getSign() ? -1 : 1;
        return new Rational(thisSign * this.getDenom(), this.getNumer());
    }

    /**
     * get the additive inverse of this
     * @return additive inverse of this
     */
    public Rational getNegatation(){
        int oppSign = this.getSign() ? 1 : -1;
        return new Rational(oppSign * this.getNumer(), this.getDenom());
    }

    /**
     * gets the greatest common denominator of two numbers (up to +-1)
     * @param num1 the first integer
     * @param num2 the second integer
     * @return greatest common denominator of the two numbers
     */
    private static long gcd(long num1, long num2){
        // makes it so gcdRecusrsive is called with larger number being the first parameter, and the smaller one being the second
        long newGcd;
        if(num1 > num2){
            newGcd = gcdRecursive(num1, num2);
        }else{
            newGcd = gcdRecursive(num2, num1);
        }
        return newGcd > -newGcd ? newGcd : -newGcd;
    }

    /**
     * finds the gcd of two integers
     * @param larger larger of the two integers
     * @param smaller smaller of the two integers
     * @return the gcd
     */
    private static long gcdRecursive(long larger, long smaller){
        // use the euclidean algorithm to find the greatest common denominator
        if(smaller == 0){
            return larger;
        }else{
            return gcdRecursive(smaller, larger % smaller);
        }
    }

    /**
     * find the lowest common multiple of two numbers
     * @param num1 the first integer
     * @param num2 the second integer
     * @return lcm of the two given integers
     */
    private static long lcm(long num1, long num2){
        // uses the fact that l * m = gcd(l,m) * lcm(l,m) => lcm(l,m) = l * m / gcd(l,m)
        return num1 * num2 / gcd(num1, num2);
    }

    /**
     * multiply the current rational number by a different one
     * @param other a rational number other than the current one
     * @return the product of the two rational numbers
     */
    public Rational multiply(Rational other) {
        // test to see if we're multiplying 0 by infinity
        if (this.equals(new Rational(0)) && other.isInfinity()) {
            throw new ArithmeticException("cannot multiply 0 by infinity");
        }
        if (other.equals(new Rational(0)) && this.isInfinity()) {
            throw new ArithmeticException("cannot multiply 0 by infinity");
        }

        // if one or both of the numbers are infinite
        if (this.isInfinity() || other.isInfinity()) {

            // if different signs
            if (other.getSign() != this.getSign()) {
                return makeNegativeInfinity();
            } else { // if same signs
                return makePositiveInfinity();
            }
        }

        // if both numbers are finite

        // get new sign
        boolean newSign = this.getSign() ^ other.getSign();

        // multiply the numerator and denominators
        long newNum = this.getNumer() * other.getNumer();
        long newDen = this.getDenom() * other.getDenom();

        // reduce the numerator and denominator
        long newGcd = gcd(newNum, newDen);
        newNum = newNum / newGcd;
        newDen = newDen / newGcd;
        if (newSign) {
            return new Rational(-newNum, newDen);
        } else {
            return new Rational(newNum, newDen);
        }
    }

    public Rational power(int k){
        if(this.equals(new Rational(0)) && k == 0){
            throw new ArithmeticException("Cannot raise 0 to the 0th power");
        }
        if(this.isInfinity()){
            if(k == 0){ // inf ^ 0 = NaN
                throw new ArithmeticException("Cannot raise infinity to the 0th power");
            }else if(k < 0){ // (1/inf)^k = 0
                return new Rational(0);
            }else{
                if(!this.getSign()){ // +inf^k = inf
                    return makePositiveInfinity();
                }else{
                    if(k % 2 == 0){ // -inf^k = -inf if k is odd else -inf^k = inf
                        return makePositiveInfinity();
                    }else{
                        return makeNegativeInfinity();
                    }
                }
            }
        }

        // now for finite case
        //( a ) k   a^k
        //( - )^  =  -
        //( b )     b^k
        // if k is positive. If k is negative then flip the signs

        if(k == 0){
            return new Rational(0);
        }else if (k > 0){
            return new Rational(pow(numer, k), pow(denom, k));
        }else{
            int j = k > -k ? k : -k;
            return new Rational(pow(denom, j), pow(numer, j));
        }
    }

    /**
     * raise a given base to the exp power
     * @param base long, usually a numerator or denominator
     * @param exp int, power
     * @return long which is the result of base^exp
     */
    private long pow(long base, int exp){
        long tempBase = 1;
        for (int i = 0; i < exp; i++) {
            tempBase = tempBase * base;
        }
        return tempBase;
    }

    /**
     * divide the current rational number by a different one
     * @param other a rational number other than the current one
     * @return the dividend of the two rational numbers
     */
    public Rational divide(Rational other){
        // ensure we're not trying to divide by infinity
        if(other.isInfinity() && this.isInfinity()){
            throw new ArithmeticException("Cannot divide by infinity by infinity");
        }
        // ensure we're not dividing by zero
        if(other.getNumer() == 0){
            throw new ArithmeticException("Cannot divide by 0");
        }
        // if numerator is infinity then set the result to be +-inf
        if(this.isInfinity()){
            if(this.getSign() != other.getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }
        // if denominator is infinity then return 0
        if(other.isInfinity()){
            return new Rational(0,1);
        }

        // if both are finite

        int thisSign = this.getSign() ? -1 : 1;
        int otherSign = other.getSign() ? -1 : 1;

        // inverse of multiplication
        long newNum = thisSign * this.getNumer() * other.getDenom();
        long newDen = otherSign * this.getDenom() * other.getNumer();

        // reduce by gcd and return
        long newGcd = gcd(newNum, newDen);
        newNum = newNum/newGcd;
        newDen = newDen/newGcd;
        return new Rational(newNum, newDen);
    }

    /**
     * add the current rational number by a different one
     * @param other a rational number other than the current one
     * @return the sum of the two rational numbers
     */
    public Rational add(Rational other){
        // if both are infinite
        if(this.isInfinity() && other.isInfinity()){
            // if trying: inf - inf or -inf + inf throw exception
            if(this.getSign() != other.getSign()){
                throw new ArithmeticException("Cannot add positive and negative infinity");
            }else if(this.getSign()){ // if trying -inf + -inf
                return makeNegativeInfinity();
            }else{ // if trying inf + inf
                return makePositiveInfinity();
            }
        }
        // if just this is infinite
        if(this.isInfinity()){
            // return infinity of the same sign as this
            if(this.getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }
        // if other is infinite
        if(other.isInfinity()){
            // return infinity of the same sign as other
            if(other.getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }


        // case for finite numbers

        // a   c   a * d + c * b
        // - + - = -------------
        // b   d       b * d

        int thisSign = this.getSign() ? -1 : 1;
        int otherSign = other.getSign() ? -1 : 1;

        // add like above
        long newNum = thisSign * this.getNumer() * other.getDenom() + otherSign * this.getDenom() * other.getNumer();
        long newDen = this.getDenom() * other.getDenom();

        // reduce and return
        long newGcd = gcd(newNum, newDen);
        newNum = newNum/newGcd;
        newDen = newDen/newGcd;
        return new Rational(newNum, newDen);
    }

    public void increment(Rational other){
        // if both are infinite
        if(this.isInfinity() && other.isInfinity()){
            // if trying: inf - inf or -inf + inf throw exception
            if(this.getSign() != other.getSign()){
                throw new ArithmeticException("Cannot add positive and negative infinity");
            }else if(this.getSign()){ // if trying -inf + -inf
                // return makeNegativeInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = true;
                this.infinity = true;
            }else{ // if trying inf + inf
                // return makePositiveInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = false;
                this.infinity = true;

            }
        }
        // if just this is infinite
        if(this.isInfinity()){
            // return infinity of the same sign as this
            if(this.getSign()){
                // return makeNegativeInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = true;
                this.infinity = true;
            }else{
                // return makePositiveInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = false;
                this.infinity = true;
            }
        }
        // if other is infinite
        if(other.isInfinity()){
            // return infinity of the same sign as other
            if(other.getSign()){
                // return makeNegativeInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = true;
                this.infinity = true;
            }else{
                // return makePositiveInfinity();
                this.numer = 0;
                this.denom = 1;
                this.sign = false;
                this.infinity = true;
            }
        }


        // case for finite numbers

        // a   c   a * d + c * b
        // - + - = -------------
        // b   d       b * d

        int thisSign = this.getSign() ? -1 : 1;
        int otherSign = other.getSign() ? -1 : 1;

        // add like above
        long newNum = thisSign * this.getNumer() * other.getDenom() + otherSign * this.getDenom() * other.getNumer();
        long newDen = this.getDenom() * other.getDenom();

        // reduce and return
        long newGcd = gcd(newNum, newDen);
        this.numer =  newNum/newGcd;
        this.denom = newDen/newGcd;
    }

    /**
     * subtract the current rational number by a different one
     * @param other a rational number other than the current one
     * @return the difference of the two rational numbers
     */
    public Rational subtract(Rational other){
        // if both are infinity
        if(this.isInfinity() && other.isInfinity()){
            // if trying -inf - -inf or inf - inf throw exception
            if(this.getSign() == other.getSign()){
                throw new ArithmeticException("cannot subtract infinity from infinity");
            }else if(this.getSign()){ // if trying -inf - inf
                return makeNegativeInfinity();
            }else{ // inf - -inf
                return makePositiveInfinity();
            }
        }

        // if this is infinite
        if(this.isInfinity()){
            // return infinity of same type as this
            if(this.getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }

        // if other is infinite
        if(other.isInfinity()){
            // return infinity of opposite type as other
            if(this.getSign()){ // x - -inf
                return makePositiveInfinity();
            }else{ // x - +inf
                return makeNegativeInfinity();
            }
        }

        // case for finite numbers

        // a   c   a * d - c * b
        // - - - = -------------
        // b   d       b * d


        // subtract like above

        int thisSign = this.getSign() ? -1 : 1;
        int otherSign = other.getSign() ? -1 : 1;

        long newNum = thisSign * this.getNumer() * other.getDenom() - otherSign * this.getDenom() * other.getNumer();
        long newDen = this.getDenom() * other.getDenom();

        // reduce and return
        long newGcd = gcd(newNum, newDen);
        newNum = newNum/newGcd;
        newDen = newDen/newGcd;
        return new Rational(newNum, newDen);
    }

    /**
     * compares this to another rational number
     * @param other another rational number
     * @return -1 if this < other; 0 if this = other; 1 if this > other
     */
    public int compareTo(Rational other){
        if(this.equals(other)){
            return 0;
        }
        if(this.isInfinity() && other.isInfinity()){
            if(this.getSign() == other.getSign()){
                throw new ArithmeticException("Cannot compare infinities of the same sign");
            }
        }
        if(this.isInfinity()){
            if(this.getSign()) { // if this is negative then this < other
                return -1;
            }else{
                return 1;
            }
        }
        if(other.isInfinity()){
            if(other.getSign()){ // if this is negative then this > other
                return 1;
            }else{
                return 0;
            }
        }

        // case for if they're both finite

        Rational difference = this.subtract(other);
        // if this - other is negative then this < other
        if(difference.getSign()){
            return -1;
        }else{
            return 1;
        }

    }

    /**
     * figures out whether this = other
     * @param other another rational number
     * @return true if this = other, otherwise false
     */
    public boolean equals(Rational other){
        // if one of them is infinity
        if(this.isInfinity() || other.isInfinity()){
            // return true if they're both infinite, and of the same sign; else return false
            return other.isInfinity() && this.isInfinity() && this.getSign() == other.getSign();
        }
        // return true if both the numerators and denominators match
        return (this.getDenom() == other.getDenom()) && (this.getNumer() == other.getNumer()) && (this.getSign() == other.getSign());
    }

    /**
     * creates a string representation of the rational number
     * @return string representation of this
     */
    public String toString(){
        if(this.isInfinity()){
            if(this.getSign()){
                return "-inf";
            }else{
                return  "+inf";
            }
        }
        String returnString = this.getNumer() + "/" + this.getDenom();
        if(sign){
            return "-" + returnString;
        }else{
            return returnString;
        }

    }

    public static void main(String[] args){

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


        System.out.println("Testing complete");

    }
}
