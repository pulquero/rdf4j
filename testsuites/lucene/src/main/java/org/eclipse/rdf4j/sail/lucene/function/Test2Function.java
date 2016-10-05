package org.eclipse.rdf4j.sail.lucene.function;

import static org.eclipse.rdf4j.sail.lucene.function.MockFunction.TEST2_PROPERTY;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.sail.lucene.function.util.Function2Transformer;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;

/**
 * That class returns a list where first argument is the input word with all capitalised letters and second
 * argument is length of the word.
 * 
 * @author github.com:jgrzebyta
 */
public class Test2Function implements InverseMagicProperty {

	@Override
	public String getURI() {
		return TEST2_PROPERTY.toString();
	}

	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args)
		throws QueryEvaluationException
	{

		if (args.length != 1) {
			throw new QueryEvaluationException("Only 1 argument is accepted");
		}

		List<Value> argList = new ArrayList<Value>();
		argList.add(args[0]);

		return new CloseableIteratorIteration<List<? extends Value>, QueryEvaluationException>(
				Function2Transformer.transform(argList.iterator()));

	}

}
