/*******************************************************************************
 * Copyright (c) 2016 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.spin.function.sail;

import java.util.List;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;
import org.eclipse.rdf4j.sail.lucene.LuceneSail2Schema;

/**
 * Implements <code><http://rdf4j.org/contrib/LuceneSail2/search></code> property function. TODO: The function
 * should cooperate with {@link LuceneSail} within {@link SpinSail}.
 * 
 * @see LuceneSail2Schema
 * @author github:jgrzebyta
 */
public class LuceneSearch implements InverseMagicProperty {

	@Override
	public String getURI() {
		return LuceneSail2Schema.SEARCH.toString();
	}

	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args)
		throws QueryEvaluationException
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
