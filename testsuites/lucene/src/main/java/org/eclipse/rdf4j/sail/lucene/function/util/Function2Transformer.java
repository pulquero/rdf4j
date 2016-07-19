package org.eclipse.rdf4j.sail.lucene.function.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

public class Function2Transformer implements Function<Value, List<Value>> {

	private static SimpleValueFactory vf = SimpleValueFactory.getInstance();

	private static final Function2Transformer INSTANCE = new Function2Transformer();

	public static Iterator<List<Value>> transform(Iterator<Value> ins) {
		return Iterators.transform(ins, INSTANCE);
	}

	@Override
	public List<Value> apply(Value input) {
		List<Value> toReturn = new ArrayList<>();
		toReturn.add(vf.createLiteral(input.stringValue().toUpperCase()));
		toReturn.add(vf.createLiteral(input.stringValue().length()));
		return toReturn;
	}

}
