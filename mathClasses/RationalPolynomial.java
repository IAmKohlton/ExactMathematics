package mathClasses;

import dataStructures.DoublyLinkedList;

import static mathClasses.Rational.*;

/**
 * Represents a polynomial with rational coefficients. Supports addition, scaling, and multiplication of polynomials
 */
public class RationalPolynomial {
    // TODO check and handle overflow more elegantly basically everywhere
    /**
     * Rational polynomial where items further along in the list have higher degree
     */
    protected DoublyLinkedList<Rational> poly;

    /**
     * create a rational polynomial of arbitrary degree
     * @param args Rational numbers to go in the polynomial where further right elements have higher degree
     */
    public RationalPolynomial(Rational ... args){
        poly = new DoublyLinkedList<>();
        for(Rational x: args){
            if(x.isInfinity()){
                throw new ArithmeticException("infinite values not allowed in polynomial");
            }
            poly.insert(x);
        }
    }

    /**
     * create a rational polynomial with integer coefficients. Psuedo-constructor
     * @param args integers to go in the polynomial
     * @return rational polynomial with integer coefficients
     */
    public static RationalPolynomial createFromIntegers(int ... args){
        RationalPolynomial creation = new RationalPolynomial();
        for(int x: args){
            creation.poly.insert(new Rational(x));
        }
        return creation;
    }

    /**
     * Get's the rational number currently pointed to by cursor in 'poly'
     * @return rational number in polynomial
     */
    public Rational currentRational(){
        return this.poly.item().item();
    }

    /**
     * Figures out whether there's no rationals in the current rational polynomial
     * @return whether there's rationals in the polynomial
     */
    public boolean isNull(){
        return this.poly.getSize() == 0;
    }

    /**
     * obtains the degree of the current polynomial
     * @return degree of polynomial
     */
    public int getDegree(){
        return this.poly.getSize() - 1;
    }

    /**
     * scales this polynomial by a Rational coefficient
     * @param scaler rational to be scaled by
     * @return scaled polynomial
     */
    public RationalPolynomial scale(Rational scaler){
        if(scaler.isInfinity()){
            throw new ArithmeticException("Rational polynomials cannot have coefficients equal to infinity");
        }

        RationalPolynomial scaledPoly = new RationalPolynomial();
        if(this.isNull()){
            return scaledPoly;
        }

        this.poly.goFirst();
        while(!this.poly.isAfter()){
            scaledPoly.poly.insert(currentRational().multiply(scaler));
            this.poly.goForth();
        }
        return scaledPoly;
    }

    /**
     * scales this polynomial by an integer coefficient
     * @param scaler rational to be scaled by
     * @return scaled polynomial
     */
    public RationalPolynomial scale(int scaler){
        return scale(new Rational(scaler));
    }
    
    
    /**
     * adds two polynomials together
     * @param other another rational polynomial
     * @return sum of two polynomials
     */
    public RationalPolynomial add(RationalPolynomial other){

        // special case for if one is the null polynomial
        if(this.isNull() || other.isNull()){
            throw new ArithmeticException("Cannot add by a null polynomial");
        }

        // special case for if one is the zero polynomial
        RationalPolynomial zeroPoly = new RationalPolynomial(new Rational(0));
        if(this.equals(zeroPoly)){
            return other;
        }
        if(other.equals(zeroPoly)){
            return this.copy();
        }

        // need to pad polynomial of smaller degree with zero terms so we can iterate through both without trouble
        padPoly(this, other);

        RationalPolynomial sum = new RationalPolynomial();

        // iterate through every term of the polynomial
        // adding every term to the sum polynomial along the way
        Rational otherRat;
        Rational thisRat;
        Rational currSum;
        this.poly.goFirst();
        other.poly.goFirst();
        while(!this.poly.isAfter()){
            thisRat = currentRational();
            otherRat = other.poly.item().item();
            currSum = thisRat.add(otherRat);
            System.out.println(currSum);
            sum.poly.insert(currSum);

            this.poly.goForth();
            other.poly.goForth();
        }

        // we now must undo the padded zero terms we did previously
        unPadPoly(this, other);

        return sum;
    }

    /**
     * subtracts two polynomials with form of 'this - other'
     * @param other polynomial we subtract this by
     * @return difference of two polynomials
     */
    public RationalPolynomial subtract(RationalPolynomial other){
        if(this.isNull() || other.isNull()){
            throw new ArithmeticException("Cannot subtract by a null polynomial");
        }
        return this.add(other.scale(-1));
    }


    /**
     * multiplies two polynomials
     * @param other polynomial we multiply by
     * @return product of two polynomials
     */
    public RationalPolynomial multiply(RationalPolynomial other){
        // special case for if one of the polynomials is the null polynomial
        if(this.isNull() || other.isNull()){
            throw new ArithmeticException("Cannot multiply by a null polynomial");
        }

        // special case for the zero polynomial
        RationalPolynomial zero = new RationalPolynomial(new Rational(0));
        if(this.equals(zero) || other.equals(zero)){
            return zero;
        }

        int thisDegree = this.getDegree();
        int otherDegree = other.getDegree();

        // create an array of rationals. This will be converted to a RationalPolynomial at the end
        // needs to be an array for in place modification of elements, and constant time access
        Rational[] productArray = new Rational[thisDegree + otherDegree + 1];
        for (int i = 0; i <= thisDegree + otherDegree; i++) {
            productArray[i] = new Rational(0);
        }

        this.poly.goFirst();
        int i = 0; // keep track of i, and j to find the degree of the intermediate product terms
        int j = 0;
        Rational thisCurrent;
        Rational otherCurrent;
        // loop through all elements of 'this'
        while(!this.poly.isAfter()){
            thisCurrent = this.currentRational();
            other.poly.goFirst();
            j = 0;
            while(!other.poly.isAfter()){
                otherCurrent = other.currentRational();

                // thisCurr * otherCurr is of degree i+j so we must increment the i+jth term by thisCurr * otherCurr
                productArray[i+j].increment(thisCurrent.multiply(otherCurrent));

                other.poly.goForth();
                j++;
            }

            this.poly.goForth();
            i++;
        }

        // convert the array to a RationalPolynomial
        RationalPolynomial product = new RationalPolynomial();
        for (int k = 0; k <= thisDegree + otherDegree; k++) {
            product.poly.insert(productArray[k]);
        }

        return product;
    }

    private static void padPoly(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        Rational zero = new Rational(0);
        if(firstPoly.poly.getSize() < secondPoly.poly.getSize()){ // if this is of lesser degree
            firstPoly.poly.goLast();
            while(firstPoly.poly.getSize() < secondPoly.poly.getSize()){
                firstPoly.poly.insert(zero);
            }
        }else if (firstPoly.poly.getSize() > secondPoly.poly.getSize()){ // if other is of lesser degree
            secondPoly.poly.goLast();
            while(firstPoly.poly.getSize() > secondPoly.poly.getSize()){
                secondPoly.poly.insert(zero);
            }
        }
    }

    private static void unPadPoly(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        Rational currentRational;
        Rational zero = new Rational(0);
        firstPoly.poly.goLast();
        Rational lastRational = firstPoly.currentRational();
        if(lastRational.equals(zero)){
            firstPoly.poly.goLast();
            currentRational = firstPoly.currentRational();
            while(currentRational.equals(zero)){
                firstPoly.poly.delete();
                firstPoly.poly.goLast();
                currentRational = firstPoly.currentRational();
            }
        }
        secondPoly.poly.goLast();
        lastRational = secondPoly.currentRational();
        if(lastRational.equals(zero)){
            secondPoly.poly.goLast();
            currentRational = secondPoly.currentRational();
            while(currentRational.equals(zero)){
                secondPoly.poly.delete();
                secondPoly.poly.goLast();
                currentRational = secondPoly.currentRational();
            }
        }
    }

    /**
     * solve a polynmoial by plugging in a given x
     * @param xVal x^i = xVal^i
     * @return solution to polynomial
     */
    public Rational solve(Rational xVal){
        Rational runningTotal = new Rational(0);
        Rational coeff; // represents the coefficient in any given term
        Rational term; // represents the x in any given term

        if(this.isNull()){
            throw new IllegalStateException("Cannot solve for a null polynomial");
        }

        if(xVal.isInfinity()){
            // the last term will dominate as x->inf so we only care about it's sign
            this.poly.goLast();

            // raise infinity to the kth power and then look at it's sign
            long infCoeff = xVal.getSign() ? -1 : 1;
            infCoeff = pow(infCoeff, this.getDegree());
            boolean infSign = infCoeff != 1;

            // if one of the sign of the infinity, or the sign of the highest term coefficient is negative then it should be negative infinity
            // if both or neither are negative then it should give positive infinity
            if(infSign ^ this.currentRational().getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }

        this.poly.goFirst();
        for (int i = 0; i <= this.getDegree(); i++) {
            coeff = this.currentRational();
            term = xVal.power(i);
            runningTotal.increment(coeff.multiply(term));
        }

        return runningTotal;
    }

    public Rational solve(int xVal){
        return solve(new Rational(xVal));
    }

    /**
     * creates a deep clone of the current polynomial
     * @return clone of polynomial
     */
    public RationalPolynomial copy(){
        RationalPolynomial temp = new RationalPolynomial();
        if(this.isNull()){
            return temp;
        }
        poly.goFirst();
        while(!poly.isAfter()){
            temp.poly.insert(poly.item().item());
            poly.goForth();
        }
        return temp;
    }

    /**
     * checks whether two polynomials are equal
     * @param other other rational polynomial
     * @return whether the two polynomials are equal
     */
    public boolean equals(RationalPolynomial other){
        if(this.isNull()){
            return other.isNull();
        }

        // if they're not the same length, they're not equal
        if(other.poly.getSize() != this.poly.getSize()){
            return false;
        }
        other.poly.goFirst();
        this.poly.goFirst();
        while(!this.poly.isAfter()){
            // if every individual element isn't the same then they're not equal
            if(!currentRational().equals(other.poly.item().item())){
                return false;
            }

            other.poly.goForth();
            this.poly.goForth();
        }
        return true;
    }

    /**
     * creates a string representation of the polynomial
     * @return string representation of polynomial
     */
    public String toString(){
        // this string will be 3 lines which we must construct simultaneously
        String topLine = "";
        String middleLine = "";
        String bottomLine = "";
        if(poly.getSize() == 0){
            return "0";
        }
        poly.goFirst();
        Rational currentRat;
        int numDigits;
        int numLen;
        int denLen;
        int expLen;
        String nextSign = "";
        for (int i = 0; i < poly.getSize(); i++) {
            currentRat = poly.item().item();
            denLen = Long.toString(currentRat.getDenom()).length(); // string length of the current denominator
            numLen = Long.toString(currentRat.getNumer()).length(); // string length of the current numerator
            numDigits = numLen < denLen ? denLen : numLen;
            if(i != poly.getSize() - 1){
                nextSign = poly.item().getNext().item().getSign() ? "-" : "+";
            }

            expLen = Integer.toString(i).length();

            // this is garbage code, but all string processing is really garbage code
            // lets break this down a little bit, each condition has the same structure, with slight differences
            // most of the differences come from padding being really weird
            // topLine += currentRat.getNumer() + getNChars(3 + numDigits - numLen, " ");
            // currentRat.getNumer() is clear
            // getNChars is a bit more complicated
            // the 3 is based around the base padding needed on a rational with no "x" component.
            // the numDigits - numLen part adjusts the base padding for when the numerator has more (or less) digits than the denominator
            // This pads from the end of the current numerator to the begining of the next numerator
            // padding must change from case to case due to "x" or "x^2" mucking things up
            // bottomLine code is very similar to top line in all cases
            // now onto middleLine
            // middleLine += getNChars(numDigits, "\u2014") + " x^" + i + " " + currentSign + " ";
            // getNChars here makes it so the division symbol is as long as the longest digit
            // "x^+ " i gives "x" and the exponent
            //  " " + currentSign + " " gives the sign of the *next* rational number in the polynomial

            // case for when x is to power 0
            if(i == 0){
                // makes it so the sign is added to the constant term if it's negative
                if(currentRat.getSign()){
                    topLine += "  ";
                    middleLine += "- ";
                    bottomLine += "  ";
                }

                topLine += currentRat.getNumer() + getNChars(3 + numDigits - numLen, " ");
                middleLine += getNChars(numDigits, "\u2014") + " " + nextSign + " ";
                bottomLine += currentRat.getDenom() + getNChars(3 + numDigits - denLen, " ");

            }else if(i == poly.getSize() - 1){
                // case for when x is the second last one in polynomial

                // this case is almost exactly the same as the "else" case but it doesn't add the sign of the next rational to the middle line
                topLine += currentRat.getNumer() + getNChars(6 + expLen + numDigits - numLen, " ");
                if(i == 1){
                    middleLine += getNChars(numDigits, "\u2014") + " x";
                }else{
                    middleLine += getNChars(numDigits, "\u2014") + " x^" + i;
                }

                bottomLine += currentRat.getDenom() + getNChars(6 + expLen + numDigits - denLen, " ");

            }else if(i == 1){
                // case for when x is to power 1

                topLine += currentRat.getNumer() + getNChars(5 + numDigits - numLen, " ");
                middleLine += getNChars(numDigits, "\u2014") + " x " + nextSign + " ";
                bottomLine += currentRat.getDenom() + getNChars(5 + numDigits - denLen, " ");

            }else{

                topLine += currentRat.getNumer() + getNChars(6 + expLen + numDigits - numLen, " ");
                middleLine += getNChars(numDigits, "\u2014") + " x^" + i + " " + nextSign + " ";
                bottomLine += currentRat.getDenom() + getNChars(6 + expLen + numDigits - denLen, " ");

            }


            if(!poly.isLast(poly.item())){
                poly.goForth();
            }

        }
        return topLine + "\n" + middleLine + "\n" + bottomLine;
    }

    /**
     * creates a simplified string representation of the polynomial
     * @return string representation of polynomial
     */
    public String oneLineToString(){
        String outString = "";
        poly.goFirst();

        // poly.item().item().abs().toString(); pops up a lot here.
        // This is to get the string representation of the absolute value of the rational
        // this is so we can don't have a string like 1/2 - -2/3x

        for (int i = 0; i < poly.getSize(); i++) {
            if(i == 0){
                outString += poly.item().item().abs().toString(); //
            }else if(i == 1){
                outString += poly.item().item().abs().toString() + "*x";
            }else{
                outString += poly.item().item().abs().toString() + "*x^"+i;
            }

            if(!poly.isLast(poly.item())){
                if(poly.item().getNext().item().getSign()){
                    outString += " - ";
                }else{
                    outString += " + ";
                }
                poly.goForth();
            }
        }


        return outString;
    }

    private static String getNChars(int n, String c){
        return new String(new char[n]).replace("\0", c);
    }

    public static void main(String[] args){
        // assumes the constructors, equals, and private methods work
        // this assumption can be made since if they were wrong then everything else would also go wrong
        // toString was also tested fairly thoroughly beforehand

        boolean caught;
        Rational inf = makePositiveInfinity();
        Rational negInf = makeNegativeInfinity();

        // now testing copy
        RationalPolynomial test1 = createFromIntegers(1,2,3);
        RationalPolynomial test1_1 = test1.copy();
        if(!test1.equals(test1_1) || test1 == test1_1)
            System.out.println("copy does not copy am integer polynomial properly");

        RationalPolynomial nullPoly = new RationalPolynomial();
        RationalPolynomial nullPoly_1 = nullPoly.copy();
        if(!nullPoly.equals(nullPoly_1) || nullPoly == nullPoly_1)
            System.out.println("copy doesn't copy null polynomial properly");

        RationalPolynomial test2 = new RationalPolynomial(new Rational(1,2), new Rational(2,3), new Rational(3,4), new Rational(4, 5));
        RationalPolynomial test2_1 = test2.copy();
        if(!test2.equals(test2_1) || test2 == test2_1)
            System.out.println("copy doesn't properly copy a rational polynomial");

        // now testing scale
        RationalPolynomial zero = new RationalPolynomial(new Rational(0));
        if(!(zero.scale(100).equals(zero)))
            System.out.println("0 * 100 != 0");

        if(!(test1.scale(3).equals(createFromIntegers(3,6,9))))
            System.out.println("(1+2x+3x^2) * 3 != 3+6x+9x^2");

        if(!(nullPoly.scale(100000).equals(nullPoly)))
            System.out.println("null * 0 != null");

        RationalPolynomial test2_5 = test2.scale(4);
        RationalPolynomial test2_5_expected = new RationalPolynomial(R(2, 1), R(8,3), R(3, 1), R(16, 5));
        if(!(test2_5.equals(test2_5_expected)))
            System.out.println("Didn't scale rational polynomial correctly");

        RationalPolynomial test3 = new RationalPolynomial(R(-1,3), R(4, 1), R(-3,2));
        RationalPolynomial test3_1 = test3.scale(R(-3,4));
        RationalPolynomial test3_1_expected = new RationalPolynomial(R(3,12), R(-3, 1), R(9, 8));
        if(!(test3_1.equals(test3_1_expected)))
            System.out.println("Didn't scale rational by a rational correctly");

        caught = false;
        RationalPolynomial test0 = new RationalPolynomial(R(0,1));
        try{
            test0.scale(inf);
        }catch(ArithmeticException e){
            caught = true;
        }
        if(!caught){
            System.out.println("Didn't throw exception on 0 poly * inf");
        }

        // now testing solve
        Rational test4 = test1.solve(0); // test one is 1+2x+3x^2
        if(!(test4.equals(R(1,1))))
            System.out.println("polynomial at x=0 isn't the value of the constant");

        Rational test5 = test1.solve(1);
        if(!(test5.equals(R(6,1))))
            System.out.println("polynomials at x=1 isn't sum of coefficients");

        Rational test6 = test1.solve(inf);
        if(!test6.equals(inf))
            System.out.println("polynomial at x=inf isn't inf when highest coefficient is positive");



    }
}
