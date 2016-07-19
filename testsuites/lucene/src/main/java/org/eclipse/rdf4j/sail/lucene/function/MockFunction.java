package org.eclipse.rdf4j.sail.lucene.function;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * Some Mock function. It just prints arguments.
 */
public class MockFunction {

	public static final String NAMESPACE = "urn:mockfunction/";

	public static IRI TEST1_PROPERTY;

	public static IRI TEST2_PROPERTY;

	static {
		ValueFactory vf = SimpleValueFactory.getInstance();

		TEST1_PROPERTY = vf.createIRI(NAMESPACE, "test1");
		TEST2_PROPERTY = vf.createIRI(NAMESPACE, "test2");
	}
}
