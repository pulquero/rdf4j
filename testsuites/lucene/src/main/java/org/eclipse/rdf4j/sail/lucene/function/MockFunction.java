package org.eclipse.rdf4j.sail.lucene.function;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;
import org.eclipse.rdf4j.spin.function.spif.SingleValueToListTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * Some Mock function. It just prints arguments.
 */
public class MockFunction implements InverseMagicProperty {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String NAMESPACE = "urn:mockfunction/";

	public static IRI REQUEST_PROPERTY;

	public static IRI VALUE_PROPERTY;

	static {
		ValueFactory vf = SimpleValueFactory.getInstance();

		REQUEST_PROPERTY = vf.createIRI(NAMESPACE, "request");
		VALUE_PROPERTY = vf.createIRI(NAMESPACE, "value");
	}

	protected ValueFactory vf = SimpleValueFactory.getInstance();

	@Override
	public String getURI() {
		return REQUEST_PROPERTY.toString();
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
