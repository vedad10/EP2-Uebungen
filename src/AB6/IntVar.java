package AB6;

import java.util.Objects;

/**
 * This class represents a free variable which can take on integer values. Each object of
 * this class represents a different variable (regardless of the name). This means that
 * for two 'IntVar' reference values 'a' and 'b', a.equals(b) == true only if 'a' and 'b'
 * refer to the same object (a == b has value 'true').
 */
//
// TODO: define further classes, if needed.
//
public class IntVar implements IntVarTerm //TODO: uncomment clause.
{
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
    public LinearExpression plus(LinearExpression e) {
        return e.plus(this);
    }

    @Override
    public LinearExpression plus(IntConst c) {
        if (c.isZero()) {
            return this;
        }
        return new SumOfTerms(this, c);
    }

    @Override
    public LinearExpression plus(IntVarTerm t) {
        if (this.equals(t.getVar())) {
            return new ConstVarProduct(t.getCoeff().plus(ONE), this);
        }
        return new SumOfTerms(this, t);
    }

    @Override
    public LinearExpression negate() {
        return new ConstVarProduct(ONE.negate(), this);
    }

    @Override
    public LinearExpression times(IntConst c) {
        if (c.isZero()) {
            return ZERO;
        }
        if (c.plus(ONE.negate()).isZero()) {
            return this.negate();
        }
        if (c.plus(ONE).isZero()) {
            return this;
        }
        return new ConstVarProduct(c, this);
    }

    @Override
    public LinearExpression assignValue(IntVarConstMap varValues) {
        IntConst value = varValues.get(this);
        if (value != null) {
            return value;
        }
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public IntVar getVar() {
        return this;
    }

    @Override
    public IntConst getCoeff() {
        return ONE;
    }

    @Override
    public IntVarIterator iterator() {
        return new IntVarIterator() {
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public IntVar next() {
                if (hasNext) {
                    hasNext = false;
                    return IntVar.this;
                }
                return null;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntVar intVar = (IntVar) o;
        return this == intVar;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}