package AB5;

import java.util.Objects;

/**
 * This class represents a linear expression consisting of more than one term.
 * For example, 3x - y + 5 consists of multiple terms. 'SumOfTerms' provides an iterator over all
 * variables occurring in 'this'. The order of the iteration is not specified.
 * In this example it iterates over elements 'x' and 'y'. This class implements 'LinearExpression'.
 */
public class SumOfTerms implements LinearExpression {

    private final IntVarConstHashMap terms;
    private final IntConst constant;

    /**
     * Initializes 'this' as a sum of two terms, each with a variable.
     * @param t1 the first term in this sum.
     * @param t2 the second term in this sum.
     * The following conditions hold: t1.getVar().equals(t2.getVar()) == false.
     */
    public SumOfTerms(IntVarTerm t1, IntVarTerm t2) {
        terms = new IntVarConstHashMap();
        terms.put(t1.getVar(), t1.getCoeff());
        terms.put(t2.getVar(), t2.getCoeff());
        this.constant = new IntConst(0);
    }

    /**
     * Initializes 'this' as a sum of a term with a variable and a constant.
     * @param t the term != null.
     * @param c a constant != null, for which c.isZero() == false.
     */
    public SumOfTerms(IntVarTerm t, IntConst c) {
        terms = new IntVarConstHashMap();
        terms.put(t.getVar(), t.getCoeff());
        this.constant = c;
    }

    private SumOfTerms(IntVarConstHashMap terms, IntConst constant) {
        this.terms = terms;
        this.constant = constant;
    }

    @Override
    public IntVarIterator iterator() {
        IntVarQueue queue = terms.keyQueue();
        return new IntVarIterator() {
            @Override
            public boolean hasNext() {
                return queue.size() > 0;
            }

            @Override
            public IntVar next() {
                if (!hasNext()) {
                    return null;
                }
                return queue.poll();
            }
        };
    }

    @Override
    public LinearExpression plus(LinearExpression e) {
        IntVarConstHashMap newTerms = new IntVarConstHashMap(terms);
        IntConst newConstant = this.constant;

        if (e instanceof SumOfTerms) {
            SumOfTerms sum = (SumOfTerms) e;
            // iterator over the terms of the given SumOfTerms
            IntVarIterator iter = sum.iterator();
            // Iterate over all terms in the given SumOfTerms
            while (iter.hasNext()) {
                IntVar var = iter.next();
                IntConst coeff = sum.terms.get(var);  // Get the coefficient for the current variable in the given SumOfTerms
                if (newTerms.containsKey(var)) {
                    // add the coefficients together and update the map
                    newTerms.put(var, newTerms.get(var).plus(coeff));
                } else {
                    //add the new term to the map
                    newTerms.put(var, coeff);
                }
            }
            newConstant = newConstant.plus(sum.constant);
        } else if (e instanceof IntVarTerm) {
            // If the given expression is an instance of IntVarTerm, call the overloaded plus method
            return this.plus((IntVarTerm) e);
        } else if (e instanceof IntConst) {
            // If the given expression is an instance of IntConst, call the overloaded plus method
            return this.plus((IntConst) e);
        }
        // Return a new SumOfTerms with the combined terms and constant
        return new SumOfTerms(newTerms, newConstant);
    }

    @Override
    public LinearExpression plus(IntConst c) {
        return new SumOfTerms(terms, constant.plus(c));
    }

    @Override
    public LinearExpression plus(IntVarTerm t) {
        IntVarConstHashMap newTerms = new IntVarConstHashMap(terms);
        if (newTerms.containsKey(t.getVar())) {
            newTerms.put(t.getVar(), newTerms.get(t.getVar()).plus(t.getCoeff()));
        } else {
            newTerms.put(t.getVar(), t.getCoeff());
        }
        return new SumOfTerms(newTerms, constant);
    }

    @Override
    public LinearExpression negate() {
        IntVarConstHashMap negatedTerms = new IntVarConstHashMap();
        IntVarIterator iter = this.iterator();
        while (iter.hasNext()) {
            IntVar var = iter.next();
            negatedTerms.put(var, terms.get(var).negate());
        }
        return new SumOfTerms(negatedTerms, constant.negate());
    }

    @Override
    public LinearExpression times(IntConst c) {
        IntVarConstHashMap multipliedTerms = new IntVarConstHashMap();
        IntVarIterator iter = this.iterator();
        while (iter.hasNext()) {
            IntVar var = iter.next();
            multipliedTerms.put(var, terms.get(var).times(c));
        }
        return new SumOfTerms(multipliedTerms, constant.times(c));
    }

    @Override
    public LinearExpression assignValue(IntVarConstMap varValues) {
        IntVarConstHashMap newTerms = new IntVarConstHashMap();
        IntConst newConstant = constant;
        IntVarIterator iter = this.iterator();
        while (iter.hasNext()) {
            IntVar var = iter.next();
            if (varValues.containsKey(var)) {
                newConstant = newConstant.plus(terms.get(var).times(varValues.get(var)));
            } else {
                newTerms.put(var, terms.get(var));
            }
        }
        return newTerms.size() == 0 ? newConstant : new SumOfTerms(newTerms, newConstant);
    }

    @Override
    public String toString() {
        String result = "";

        IntVarQueue queue = terms.keyQueue(); // queue of IntVar elements from the terms map

        // A flag to indicate whether this is the first term being processed
        boolean first_term = true;

        while (queue.size() > 0) {
            IntVar variable = queue.poll();

            IntConst coefficient = terms.get(variable);

            // If the coefficient is zero, skip this term
            if (coefficient.isZero()) {
                continue;
            }

            // Start building the term string with the variable's name
            String termString = variable.toString();

            // If the coefficient is neither 1 nor -1, include it in the term string
            if (!coefficient.equals(LinearExpression.ONE) && !coefficient.equals(LinearExpression.ONE.negate())) {
                termString = coefficient + variable.toString();
            }
            // If the coefficient is -1, prepend a minus sign to the variable's name
            else if (coefficient.equals(LinearExpression.ONE.negate())) {
                termString = "-" + variable.toString();
            }

            // If this is the first term, add it to the result directly
            if (first_term) {
                result += termString;
                first_term = false; // Set the flag to false after processing the first term
            }
            // For subsequent terms, add a plus or minus sign based on the coefficient
            else {
                // If the coefficient is negative, add the term string directly
                if (coefficient.lessThan(new IntConst(0))) {
                    result += termString;
                }
                // If the coefficient is positive, prepend a plus sign
                else {
                    result += "+" + termString;
                }
            }
        }
        // If there is a constant term and it is not zero, add it to the result
        if (!constant.isZero()) {
            // If the constant is negative, add it directly
            if (constant.lessThan(new IntConst(0))) {
                result += constant;
            }
            // If the constant is positive
            else {
                // If no terms have been added yet, add the constant directly
                if (first_term) {
                    result += constant;
                }
                // Otherwise, prepend a plus sign
                else {
                    result += "+" + constant;
                }
            }
        }
        // If there were no terms and the constant is zero, return "0"
        else if (first_term) {
            return "0";
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SumOfTerms that = (SumOfTerms) o;
        return terms.equals(that.terms) && constant.equals(that.constant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms, constant);
    }
}
