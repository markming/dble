package io.mycat.plan.common.item.function.operator.cmpfunc;

import java.math.BigInteger;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;

import io.mycat.plan.common.field.Field;
import io.mycat.plan.common.item.Item;
import io.mycat.plan.common.item.function.operator.ItemBoolFunc2;


public class ItemFuncStrictEqual extends ItemBoolFunc2 {

	public ItemFuncStrictEqual(Item a, Item b) {
		super(a, b);
	}

	@Override
	public final String funcName() {
		return "<=>";
	}

	@Override
	public Functype functype() {
		return Functype.EQUAL_FUNC;
	}

	@Override
	public BigInteger valInt() {
		int value = cmp.compare();
		return value == 0 ? BigInteger.ONE : BigInteger.ZERO;
	}

	@Override
	public void fixLengthAndDec() {
		super.fixLengthAndDec();
		maybeNull = nullValue = false;
	}

	@Override
	public SQLExpr toExpression() {
		SQLExpr left = args.get(0).toExpression();
		SQLExpr right = args.get(1).toExpression();
		return new SQLBinaryOpExpr(left, SQLBinaryOperator.LessThanOrEqualOrGreaterThan, right);
	}

	@Override
	protected Item cloneStruct(boolean forCalculate, List<Item> calArgs, boolean isPushDown, List<Field> fields) {
		List<Item> newArgs = null;
		if (!forCalculate)
			newArgs = cloneStructList(args);
		else
			newArgs = calArgs;
		return new ItemFuncStrictEqual(newArgs.get(0), newArgs.get(1));
	}

}