package AB5;

import java.util.Objects;

/**
 * This class represents a product of a constant coefficient and a variable (i.e. a linear term).
 */
//
// TODO: define further classes, if needed.
//
public class ConstVarProduct implements IntVarTerm {

    // TODO: declare variables.
    private final IntConst coefficient;
    private final IntVar variable;

    /**
     * Initialized this product of 'coeff' and 'var' (for example 3x is such a product).
     *
     * @param coeff the coefficient of the term which is not 0 or 1,
     *              coeff != null && coeff.isZero() == false &&
     *              coeff.plus(new IntConst(-1)).isZero == false.
     * @param var   the variable in the term, var != null.
     */
    public ConstVarProduct(IntConst coeff, IntVar var) {

        // TODO: implement constructor.
        this.coefficient = coeff;
        this.variable = var;
    }

    //TODO: define missing parts of this class.

    @Override
    public IntVar getVar() {
        return variable;
    }

    @Override
    public IntConst getCoeff() {
        return coefficient;
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
        // Check if the variable of the current term is the same as the variable of the given term
        if (variable.equals(t.getVar())) {
            // If they are the same, add their coefficients together
            IntConst c = t.getCoeff().plus(coefficient);

            // If the sum of the coefficients is zero, return the zero linear expression
            if (c.isZero()) {
                return LinearExpression.ZERO;
            }

            // If the sum of the coefficients is one, return the variable itself (case where the term becomes a single variable without a coefficient)
            if (c.plus(LinearExpression.ONE.negate()).isZero()) {
                return variable;
            }

            // Otherwise, return a new ConstVarProduct with the summed coefficient and the variable
            return new ConstVarProduct(c, variable);
        }

        // If the variables are different, return a new SumOfTerms with the current term and the given term
        return new SumOfTerms(this, t);
    }


    @Override
    public LinearExpression negate() {
        return new ConstVarProduct(coefficient.negate(), variable);
    }

    @Override
    public LinearExpression times(IntConst c) {
        if (c.isZero()) {
            return LinearExpression.ZERO;
        }
        return new ConstVarProduct(coefficient.times(c), variable);
    }

    @Override
    public LinearExpression assignValue(IntVarConstMap varValues) {
        IntConst value = varValues.get(variable);
        if (value != null) {
            return coefficient.times(value);
        }
        return this;
    }

    @Override
    public IntVarIterator iterator() {
        return new SingleIntVarIterator(variable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstVarProduct that = (ConstVarProduct) o;
        return Objects.equals(coefficient, that.coefficient) &&
                Objects.equals(variable, that.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficient, variable);
    }

    @Override
    public String toString() {
        if (coefficient.equals(ONE.negate())) {
            return "-" + variable;
        }
        if (coefficient.equals(ONE)) {
            return variable.toString();
        }
        return coefficient + variable.toString();
    }
}

class SingleIntVarIterator implements IntVarIterator {
    private final IntVar var;
    private boolean hasNext = true;

    SingleIntVarIterator(IntVar var) {
        this.var = var;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public IntVar next() {
        if (hasNext) {
            hasNext = false;
            return var;
        }
        return null;
    }
}