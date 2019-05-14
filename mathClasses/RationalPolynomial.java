package mathClasses;

import dataStructures.DoublyLinkedList;
import dataStructures.DoublyLinkedListIterator;
import dataStructures.DoublyLinkedNode;
import dataStructures.Pair;

import static mathClasses.Rational.*;

/**
 * Represents a polynomial with rational coefficients. Supports addition, scaling, and multiplication of polynomials
 */
public class RationalPolynomial {
    // TODO check and handle overflow more elegantly basically everywhere
    // TODO have anything that iterates over poly use an iterator, not a cursor
    /**
     * Rational polynomial where items further along in the list have higher degree
     */
    protected DoublyLinkedList<Rational> poly;


    /////////////////////////////////////////
    // constructors
    /////////////////////////////////////////

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
        if(this.poly.getSize() != 0){
            this.unPadPoly();
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



    /////////////////////////////////////////
    // arithmetic
    /////////////////////////////////////////

    /**
     * scales this polynomial by a Rational coefficient
     * @param scaler rational to be scaled by
     * @return scaled polynomial
     */
    public RationalPolynomial scale(Rational scaler){
        return scale(scaler, 0);
    }

    /**
     * scales polynomial by term of scaler * x^power
     * @param scaler rational we scale polynomial by
     * @param power exponent on the x term
     * @return rescaled polynomial
     */
    private RationalPolynomial scale(Rational scaler, int power){
        if(power < 0){
            throw new ArithmeticException("Cannot scale polynomial by a negative power");
        }

        if(scaler.isInfinity()){
            throw new ArithmeticException("Rational polynomials cannot have coefficients equal to infinity");
        }

        RationalPolynomial scaledPoly = new RationalPolynomial();
        if(this.isNull()){
            return scaledPoly;
        }

        // inserts a 'power' number of zero to front of polynomial
        // same as multiplying by x^power
        for (int i = 0; i < power; i++) {
            scaledPoly.poly.insert(new Rational(0));
        }

        DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();

        iterator.goFirst();
        while(!iterator.isAfter()){
            scaledPoly.poly.insert(iterator.item().multiply(scaler));
            iterator.goForth();
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
        RationalPolynomial zeroPoly = new RationalPolynomial(new Rational(0)).copy();

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

        DoublyLinkedListIterator<Rational> thisIterator = this.poly.getIterator();
        DoublyLinkedListIterator<Rational> otherIterator = other.poly.getIterator();

        thisIterator.goFirst();
        otherIterator.goFirst();
        while(!thisIterator.isAfter()){
            thisRat = thisIterator.getCurrentNode().item();
            otherRat = otherIterator.getCurrentNode().item();
            currSum = thisRat.add(otherRat);
            sum.poly.insert(currSum);

            thisIterator.goForth();
            otherIterator.goForth();
        }

        // we now must undo the padded zero terms we did previously
        this.unPadPoly();
        other.unPadPoly();
        sum.unPadPoly();

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
        RationalPolynomial zero = new RationalPolynomial(new Rational(0)).copy();

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

        DoublyLinkedListIterator<Rational> thisIterator = this.poly.getIterator();
        DoublyLinkedListIterator<Rational> otherIterator = other.poly.getIterator();

        thisIterator.goFirst();
        int i = 0; // keep track of i, and j to find the degree of the intermediate product terms
        int j = 0;
        Rational thisCurrent;
        Rational otherCurrent;
        // loop through all elements of 'this'
        while(!thisIterator.isAfter()){
            thisCurrent = thisIterator.item();
            otherIterator.goFirst();
            j = 0;
            while(!otherIterator.isAfter()){
                otherCurrent = otherIterator.item();

                // thisCurr * otherCurr is of degree i+j so we must increment the i+jth term by thisCurr * otherCurr
                productArray[i+j].increment(thisCurrent.multiply(otherCurrent));

                otherIterator.goForth();
                j++;
            }

            thisIterator.goForth();
            i++;
        }

        // convert the array to a RationalPolynomial
        RationalPolynomial product = new RationalPolynomial();
        for (int k = 0; k <= thisDegree + otherDegree; k++) {
            product.poly.insert(productArray[k]);
        }

        return product;
    }

    /**
     * divide one polynomial by the other
     * @param other divisor polynomial
     * @return Pair where the first item is the quotient, and the second is the remainder
     */
    public Pair<RationalPolynomial, RationalPolynomial> quotientRemainder(RationalPolynomial other){
        if(this.isNull() || other.isNull())
            throw new ArithmeticException("Cannot divide by empty polynomial");

        RationalPolynomial zero = new RationalPolynomial(new Rational(0));
        if(other.equals(zero))
            throw new ArithmeticException("Cannot divide by zero");

        Pair<RationalPolynomial, RationalPolynomial> quotientRemainder = new Pair<>();

        if(this.equals(zero)){ // if the numerator is zero then both the quotient and the remainder is zero
            quotientRemainder.setFirst(zero);
            quotientRemainder.setSecond(zero);
        }else if(this.getDegree() < other.getDegree()) { // if the numerator is a higher degree than the denominator then remainder = numerator and quotient = zero
            quotientRemainder.setFirst(zero);
            quotientRemainder.setSecond(this.copy());
        }else if(other.getDegree() == 0){ // if the denominator is a constant then just scale the numerator
            quotientRemainder.setFirst(this.scale(other.currentRational().getInverse()));
            quotientRemainder.setSecond(zero);
        }else{
            RationalPolynomial thisCopy = this.copy();
            int quotientDegree = this.getDegree() - other.getDegree();

            Rational thisLeadingTerm;
            Rational otherLeadingTerm = other.poly.getTail().item();
            Rational scaler;

            RationalPolynomial quotient = new RationalPolynomial();
            RationalPolynomial scaledPoly;
            int oldThisLength = thisCopy.poly.getSize();
            for (int i = 0; i <= quotientDegree; i++) {
                // this algorithm is effectively what would be done to divide polynomials by hand

                // attempt to calculate the scaler based on the last item of thisCopy
                try{
                    // if what should be the last item is removed by unpadPoly then throw an exception then subsequently catch it
                    thisCopy.poly.goToIth(oldThisLength - 1 - i);
                    thisLeadingTerm = thisCopy.currentRational();
                    scaler = thisLeadingTerm.divide(otherLeadingTerm);
                }catch(IllegalStateException e){
                    scaler = new Rational(0,1);
                }

                // scale the denominator by lastTermOther/lastTermThis
                // after subtracting this is guaranteed to reduce the degree of thisCopy
                // done repeatedly this will reduce thisCopy until it's of lesser degree than the denominator
                scaledPoly = other.scale(scaler, quotientDegree - i);
                thisCopy = thisCopy.subtract(scaledPoly);

                thisCopy.unPadPoly(); // take away the last leading zero
                // the quotient polynomial is constructed with the the scalers
                quotient.poly.insertFirst(scaler);
            }
            quotientRemainder.setFirst(quotient);
            quotientRemainder.setSecond(thisCopy);
        }
        return quotientRemainder;
    }

    /**
     * get only the quotient when dividing polynomials
     * @param other divisor polynomial
     * @return quotient when dividing by other
     */
    public RationalPolynomial divide(RationalPolynomial other){
        return this.quotientRemainder(other).getFirst();
    }

    /**
     * get only the remainder when dividing polynomials
     * @param other divisor polynomial
     * @return remainder when dividing by other
     */
    public RationalPolynomial remainder(RationalPolynomial other){
        return this.quotientRemainder(other).getSecond();
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
            Rational lastTerm = this.poly.getTail().item();

            // raise infinity to the kth power and then look at it's sign
            long infCoeff = xVal.getSign() ? -1 : 1;

            infCoeff = (this.getDegree() % 2 == 0) ? 1 : -1;

            infCoeff = pow(infCoeff, this.getDegree());
            boolean infSign = infCoeff != 1;

            // if one of the sign of the infinity, or the sign of the highest term coefficient is negative then it should be negative infinity
            // if both or neither are negative then it should give positive infinity
            if(infSign ^ lastTerm.getSign()){
                return makeNegativeInfinity();
            }else{
                return makePositiveInfinity();
            }
        }else{
            DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();
            iterator.goFirst();

            for (int i = 0; i <= this.getDegree(); i++) {
                coeff = iterator.item();
                if(i == 0){
                    runningTotal.increment(coeff);
                }else{
                    term = xVal.power(i);
                    runningTotal.increment(coeff.multiply(term));
                }
                iterator.goForth();
            }

            return runningTotal;
        }

    }

    public Rational solve(int xVal){
        return solve(new Rational(xVal));
    }

    public Pair<RationalPolynomial, Long> integerize(){
        if(this.isNull())
            throw new IllegalStateException("Can't integerize an empty polynomial");

        DoublyLinkedList<Long> denominators = new DoublyLinkedList<>();
        DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();
        iterator.goFirst();
        while(!iterator.isAfter()){
            denominators.insert(iterator.item().getDenom());
            iterator.goForth();
        }
        Rational totalLcm = new Rational(lcmOfAll(denominators),1);

        iterator.goFirst();
        RationalPolynomial intergerized = new RationalPolynomial();
        Rational current;
        while(!iterator.isAfter()){
            current = iterator.item().multiply(totalLcm);
            intergerized.poly.insert(current);
            iterator.goForth();
        }
        return new Pair<>(intergerized, totalLcm.getNumer());
    }


    /////////////////////////////////////////
    // helper methods
    /////////////////////////////////////////

    private static void padPoly(RationalPolynomial firstPoly, RationalPolynomial secondPoly){
        Rational zero = new Rational(0);
        if(firstPoly.poly.getSize() < secondPoly.poly.getSize()){ // if this is of lesser degree then pad it
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

    public void unPadPoly(){
        Rational currentRational;
        Rational zero = new Rational(0);
        this.poly.goLast();
        Rational lastRational = this.currentRational();
        if(lastRational.equals(zero)){
            this.poly.goLast();
            currentRational = this.currentRational();
            while(currentRational.equals(zero) && this.poly.getSize() > 1){
                this.poly.delete();
                this.poly.goLast();
                currentRational = this.currentRational();
            }
        }
    }

    private static long lcmOfAll(DoublyLinkedList<Long> denoms){
        DoublyLinkedList<Long> lessDenoms = new DoublyLinkedList<>();
        Long first;
        Long second;
        int beforeDeletionLength = denoms.getSize();

        // loop this until we've done enough passes through denoms until we've reduced it to one
        while(beforeDeletionLength > 1){
            while(denoms.getSize() > 1){
                denoms.goFirst();
                first = denoms.item().item();
                denoms.delete();
                second = denoms.item().item();
                denoms.delete();
                lessDenoms.insert(lcm(first,second));
            }
            denoms = lessDenoms;
            beforeDeletionLength = denoms.getSize();
        }
        return denoms.getHead().item();
    }

    private static String getNChars(int n, String c){
        return new String(new char[n]).replace("\0", c);
    }


    /////////////////////////////////////////
    // convenience, utility, and other
    /////////////////////////////////////////

    public void goFirst(){
        poly.goFirst();
    }

    public void goLast(){
        poly.goLast();
    }

    public void goForth(){
        poly.goForth();
    }

    public void goBack(){
        poly.goBack();
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
     * @precond 'this' must not have trailing zero terms
     * @return degree of polynomial
     */
    public int getDegree(){
        DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();

        RationalPolynomial zero = new RationalPolynomial(R(0,1));
        if(this.equals(zero))
            throw new ArithmeticException("zero polynomial doesn't have a degree");

        iterator.goLast();
        if(iterator.item().equals(new Rational(0,1)))
            throw new IllegalStateException("degree is ambiguous when polynomial has trailing zeroes");

        if(this.equals(zero))
            throw new ArithmeticException("zero polynomial doesn't have a degree");

        return this.poly.getSize() - 1;
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

        DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();
       iterator.goFirst();
        while(!iterator.isAfter()){
            temp.poly.insert(iterator.item());
            iterator.goForth();
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

        DoublyLinkedListIterator<Rational> thisIterator = this.poly.getIterator();
        DoublyLinkedListIterator<Rational> otherIterator = other.poly.getIterator();

        otherIterator.goFirst();
        thisIterator.goFirst();
        while(!thisIterator.isAfter()){
            // if every individual element isn't the same then they're not equal
            if(!thisIterator.item().equals(otherIterator.item())){
                return false;
            }

            otherIterator.goForth();
            thisIterator.goForth();
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
        DoublyLinkedListIterator<Rational> iterator = poly.getIterator();
        iterator.goFirst();
        Rational currentRat;
        int numDigits;
        int numLen;
        int denLen;
        int expLen;
        String nextSign = "";
        for (int i = 0; i < poly.getSize(); i++) {
            currentRat = iterator.item();
            denLen = Long.toString(currentRat.getDenom()).length(); // string length of the current denominator
            numLen = Long.toString(currentRat.getNumer()).length(); // string length of the current numerator
            numDigits = numLen < denLen ? denLen : numLen;
            if(i != poly.getSize() - 1){
                // get the sign of the next term of the polynomial
                nextSign = iterator.getCurrentNode().getNext().item().getSign() ? "-" : "+";
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
            // \u2014 is unicode for an emdash which makes the divisor sign look nice
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


            if(!iterator.isLast()){
                iterator.goForth();
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

        DoublyLinkedListIterator<Rational> iterator = this.poly.getIterator();
        iterator.goFirst();

        // poly.item().item().abs().toString(); pops up a lot here.
        // This is to get the string representation of the absolute value of the rational
        // this is so we can don't have a string like 1/2 - -2/3x

        for (int i = 0; i < poly.getSize(); i++) {
            if(i == 0){
                outString += iterator.item().abs().toString(); //
            }else if(i == 1){
                outString += iterator.item().abs().toString() + "*x";
            }else{
                outString += iterator.item().abs().toString() + "*x^"+i;
            }

            if(!iterator.isLast()){
                if(iterator.getCurrentNode().getNext().item().getSign()){
                    outString += " - ";
                }else{
                    outString += " + ";
                }
                iterator.goForth();
            }
        }


        return outString;
    }


}
