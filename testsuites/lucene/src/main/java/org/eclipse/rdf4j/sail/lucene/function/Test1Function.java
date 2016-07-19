package org.eclipse.rdf4j.sail.lucene.function;

import static org.eclipse.rdf4j.sail.lucene.function.MockFunction.TEST1_PROPERTY;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;
import org.eclipse.rdf4j.spin.function.spif.SingleValueToListTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * That function returns back all passed arguments.
 * 
 * @author jacek
 */
public class Test1Function implements InverseMagicProperty {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String getURI() {
		return TEST1_PROPERTY.toString();
	}

	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args)
		throws QueryEvaluationException
	{

		List<String> argList = Lists.newCopyOnWriteArrayList(
				Iterables.transform(Arrays.asList(args), new Function<Value, String>()
		{

					@Override
					public String apply(Value input) {
						return input.stringValue();
					}
				}));

		log.info("**************************");

		return new CloseableIteratorIteration<List<? extends Value>, QueryEvaluationException>(
				SingleValueToListTransformer.transform(new Iterator<Value>()
		{

					int pos = 0;

					@Override
					public boolean hasNext() {
						return (pos < argList.size());
					}

					@Override
					public Value next() {
						return valueFactory.createLiteral(argList.get(pos++));
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				}));
	}

}