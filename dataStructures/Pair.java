package dataStructures;

public class Pair<I, J> {
    I first;
    J second;

    public Pair(){
        first = null;
        second = null;
    }

    public Pair(I first, J second){
        this.first = first;
        this.second = second;
    }

    public I getFirst() {
        return first;
    }

    public void setFirst(I first) {
        this.first = first;
    }

    public J getSecond() {
        return second;
    }

    public void setSecond(J second) {
        this.second = second;
    }
}
