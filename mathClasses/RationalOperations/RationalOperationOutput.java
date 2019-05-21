package mathClasses.RationalOperations;

/**
 * Marker interface to mark whether a class can be the return type of a polynomial operation
 */
public interface RationalOperationOutput {
    // the purpose of this class is to allow 'operation' to have an output be any class that implements this interface
    // the only catch is that it basically just needs to say that it can, and doesn't need any special methods
}
