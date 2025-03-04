package AB5;

import java.util.NoSuchElementException;

/**
 * This class represents a free variable which can take on integer values. Each object of
 * this class represents a different variable (regardless of the name). This means that
 * for two 'IntVar' reference values 'a' and 'b', a.equals(b) == true only if 'a' and 'b'
 * refer to the same object (a == b has value 'true').
 */
public class IntVar implements IntVarTerm {

    private final String name;

    /**
     * Initializes this variable with a specified name.
     * @param name, the name != null.
     */
    public IntVar(String name) {
        this.name = name;
    }

    /**
     * Returns the name of this variable.
     * @return the name of this variable.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public LinearExpression plus(LinearExpression e) {
        if (e instanceof IntVarTerm) {
            if (e instanceof IntVar) {
                if (((IntVar)e).getName().equals(this.getName())) {
                    return new ConstVarProduct(this.getCoeff().plus(((IntVar)e).getCoeff()), this);
                } else {
                    return new SumOfTerms(this, (IntVar) e);
                }
            } else {
                return new SumOfTerms(this, (IntVarTerm) e);
            }
        } else if (e instanceof IntConst) {
            return new SumOfTerms(this, (IntConst) e);
        }
        return null;
    }

    @Override
    public LinearExpression negate() {
        return new ConstVarProduct(new IntConst(-1), this);
    }

    @Override
    public LinearExpression times(IntConst c) {
        return new ConstVarProduct(c, this);
    }

    @Override
    public LinearExpression assignValue(IntVarConstMap varValues) {
        if (varValues.containsKey(this)) {
            return varValues.get(this);
        } else {
            return this;
        }
    }

    @Override
    public IntVarIterator iterator() {
        return new IntVarIteratorImpl();
    }

    @Override
    public IntVar getVar() {
        return IntVar.this;
    }

    @Override
    public IntConst getCoeff() {
        return new IntConst(1);
    }

    private class IntVarIteratorImpl implements IntVarIterator {
        private boolean isNextCalled = false;

        @Override
        public boolean hasNext() {
            return !isNextCalled;
        }

        @Override
        public IntVar next() {
            isNextCalled = true;
            return IntVar.this;
        }
    }
}
