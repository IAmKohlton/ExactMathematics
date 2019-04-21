package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.Pair;

public class RationalPolynomial {
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
        RationalPolynomial scaledPoly = new RationalPolynomial();
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
     * creates a deep clone of the current polynomial
     * @return clone of polynomial
     */
    public RationalPolynomial copy(){
        RationalPolynomial temp = new RationalPolynomial();
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
        RationalPolynomial test = new RationalPolynomial(new Rational(-18,23), new Rational(22, 47), new Rational(-13, 13), new Rational(-35, 14));
        RationalPolynomial test2 = new RationalPolynomial(new Rational(-13, -14), new Rational(26, -26), new Rational(6, 25));
//        RationalPolynomial sum = test.add(test2);
//        RationalPolynomial diff = test.subtract(test2);
//        System.out.println(test);
//        System.out.println(test2);
//        System.out.println(sum);
//        System.out.println(diff);
//
//        System.out.println(test.add(new RationalPolynomial(new Rational(0))));
//        System.out.println(test.scale(-1));
//        System.out.println((new RationalPolynomial(new Rational(0))).subtract(test));

        RationalPolynomial nullPoly = new RationalPolynomial();

//        System.out.println(test.add(nullPoly));

        RationalPolynomial timesTest1 = RationalPolynomial.createFromIntegers(1, 2);
        System.out.println(timesTest1);
        RationalPolynomial timesTest2 = RationalPolynomial.createFromIntegers(3, 4);
        System.out.println(timesTest2);

        System.out.println(timesTest1.multiply(timesTest2)); // should be 3 + 10x + 8x^2



    }
}
