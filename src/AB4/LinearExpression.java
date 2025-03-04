package AB4;

/**
 * This class represents a simple linear expression. A linear expression is a sum of
 * linear terms each containing at most a single variable. For example, 3x - y + 5 is such an
 * expression.
 */
//
// TODO: define further classes, if needed.
//
public class LinearExpression {

    //TODO: declare variables.

    private IntVarConstTreeMap terms;
    private IntConst constant;

    /**
     * Constructs this linear expression from a specified constant.
     *
     * @param c the constant being wrapped as this linear expression, c != null.
     */
    public LinearExpression(IntConst c) {

        //TODO: implement constructor.
        this.terms = new IntVarConstTreeMap();
        this.constant = c;
    }

    /**
     * Constructs this linear expression from a specified variable.
     *
     * @param v the variable being wrapped as this linear expression, v != null.
     */
    public LinearExpression(IntVar v) {

        //TODO: implement constructor.
        this.terms = new IntVarConstTreeMap();
        terms.put(v, new IntConst(1));
        this.constant = new IntConst(0);
    }

    /**
     * Constructs a linear expression as a copy of the specified expression.
     * Calling methods of this expression will not affect the specified expression
     * and vice versa.
     *
     * @param e the expression from which all the terms are copied to this new expression,
     *          e != null.
     */
    public LinearExpression(LinearExpression e) {

        //TODO: implement constructor.
        this.constant = e.constant;
        this.terms = new IntVarConstTreeMap(e.terms);
    }

    /**
     * Returns a new linear expression representing the sum of 'this' and 'v'.
     *
     * @param v the second summand != null.
     * @return the sum of 'this' and 'v'.
     */
    public LinearExpression plus(IntVar v) {

        //TODO: implement method.
        IntVarConstTreeMap newTerms = new IntVarConstTreeMap(this.terms);
        IntConst currentCoefficient = newTerms.get(v);
        if (currentCoefficient != null) {
            newTerms.put(v, currentCoefficient.plus(new IntConst(1)));
        } else {
            newTerms.put(v, new IntConst(1));
        }
        return new LinearExpression(newTerms, this.constant);
    }

    /**
     * Returns a new linear expression representing the sum of 'this' and 'e'.
     *
     * @param e the second summand != null.
     * @return the sum of 'this' and 'e'.
     */
    public LinearExpression plus(LinearExpression e) {

        //TODO: implement method.
        LinearExpression result = new LinearExpression(this.constant.plus(e.constant));
        IntVarConstTreeMap resultMap = new IntVarConstTreeMap();

        IntVarDoublyLinkedList list1 = e.terms.keyList();
        IntVarDoublyLinkedList list2 = this.terms.keyList();
        while (list2.size() > 0) {
            IntVar var = list2.pollFirst();
            resultMap.put(var, this.terms.get(var));
        }
        while (list1.size() > 0) {
            IntVar var = list1.pollFirst();

            if (this.terms.containsKey(var)) {
                resultMap.put(var, e.terms.get(var).plus(this.terms.get(var)));
            } else {
                resultMap.put(var, e.terms.get(var));
            }
        }

        result.terms = resultMap;
        return result;
    }

    /**
     * Returns a new expression representing the negative of 'this', such that for an expression 'e'
     * the following condition holds: e.plus(e.negate()) represents 0.
     *
     * @return the negative of 'this'.
     */
    public LinearExpression negate() {

        //TODO: implement method.
        LinearExpression result = new LinearExpression(this.constant.negate());
        IntVarConstTreeMap map = new IntVarConstTreeMap();
        IntVarDoublyLinkedList list = this.terms.keyList();

        while (list.size() > 0) {
            IntVar var = list.pollFirst();
            map.put(var, this.terms.get(var).negate());
        }

        result.terms = map;
        return result;
    }

    /**
     * Returns a new expression resulting from assigning specific values to certain variables.
     * In this new expression the corresponding variables have been substituted by the constant
     * values, that were assigned to these variables through mappings in 'varValues'. For example,
     * if 'varValues' associates variable x with constant 2 and variable y with constant 3,
     * calling the method on the expression representing 2x + y will result in the expression
     * representing the constant 7. If 'varValues' contains the mapping for y, but no mapping for
     * x, then the result is the expression 2x + 3. If there is neither a mapping for x nor for y,
     * then the result is a new expression representing 2x + y.
     *
     * @param varValues the map containing mappings from variables to their assigned values,
     *                  varValues != null.
     * @return the new expression in which specific variables have been replaced by constant
     * values (as specified by 'varValues').
     */
    public LinearExpression assignValue(IntVarConstTreeMap varValues) {

        //TODO: implement method.
        IntVarDoublyLinkedList termList = this.terms.keyList();

        IntVarConstTreeMap resultMap = new IntVarConstTreeMap();
        IntConst newConst = this.constant;

        while (termList.size() > 0) {
            IntVar var = termList.pollFirst();
            IntConst coeff = this.terms.get(var);

            if (varValues.containsKey(var)) {
                IntConst const1 = this.terms.get(var).times(varValues.get(var));
                newConst = newConst.plus(const1);
            } else {
                resultMap.put(var, coeff);
            }
        }

        LinearExpression returnExp = new LinearExpression(newConst);
        returnExp.terms = resultMap;
        return returnExp;
    }

    /**
     * A readable representation of this expression in which each of its variables appears only
     * once preceded by a coefficient, unless the coefficient is 1 or -1.
     * The variables appear in lexicographic order according to their names.
     * Terms that are zero are omitted. For example, -y + x + x - 4 would be represented by
     * "2x - y - 4" and x + y - x + 0 would be represented by "y".
     * (See Test.java for further examples.)
     *
     * @return readable representation of this expression.
     */
    @Override
    public String toString() {
        String expression = "";
        boolean isFirst = true;

        IntVarDoublyLinkedList variables = terms.keyList();
        for (int i = 0; i < variables.size(); i++) {
            IntVar variable = variables.get(i);
            IntConst coefficient = terms.get(variable);

            if (coefficient == null || coefficient.getValue() == 0) continue;

            String formattedTerm = formatTerm(coefficient, variable, isFirst);

            if (!isFirst) {
                expression += " ";
            }

            expression += formattedTerm;
            isFirst = false;
        }

        if (!constant.isZero()) {
            if (!isFirst) {
                expression += constant.getValue() > 0 ? " + " : " - ";
            } else if (constant.getValue() < 0) {
                expression += "-";
            }
            expression += Math.abs(constant.getValue());
        }

        if (expression.isEmpty()) {
            expression = "0";
        }

        return expression;
    }

    private String formatTerm(IntConst coeff, IntVar variable, boolean isFirst) {
        int absCoeff = Math.abs(coeff.getValue());
        String sign = coeff.getValue() < 0 ? "-" : (isFirst ? "" : "+");
        if (absCoeff == 1 && variable != null) {
            if (isFirst) {
                return (coeff.getValue() < 0 ? "-" : "") + variable.getName();
            } else {
                return sign + variable.getName();
            }
        } else {
            return sign + (absCoeff != 1 ? absCoeff : "") + (variable != null ? variable.getName() : "");
        }
    }


    public LinearExpression(IntVarConstTreeMap terms, IntConst constant) {
        this.terms = new IntVarConstTreeMap(terms);
        this.constant = new IntConst(constant.getValue());
    }
}

