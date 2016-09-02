/*******************************************************************************
 * Copyright (c) 2016 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.lucene;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

/**
 * New namespace for new <code>Lucene</code> functions in the LuceneSail repository. <br/>
 * <p>
 * <code>search:search(query, subject, propertyPredicate, property, scorePredicate, snippetPredicate) [functional,
 * not spin syntax]</code><br/>
 * where:
 * <ol>
 * <li><i>query</i> is the query string
 * <li><i>subject</i> is the query subject or the constant <i>search:allMatches</i>
 * <li><i>propertyPredicate</i> is the constant <i>search:property</i> or not present
 * <li><i>property</i> is present if only propertyPredicate is present and is the property to query or the
 * constant <i>search:allProperties<i>
 * <li><i>scorePredicate</i> is the constant <i>search:score</i> or not present
 * <li><i>snippetPredicate</i> is the constant <i>search:snippet</i> or not present
 * </ol>
 * </p>
 * <br/>
 * <p>
 * The output should be in this order: <code>subject, property, score, snippet</code><br/>
 * where:
 * <ol>
 * <li><i>subject</i> is included if the subject parameter is <i>search:allMatches</i> else omitted
 * <li><i>property</i> included if the propertyPredicate parameter is present and the property parameter is
 * <i>search:allProperties</i> else omitted
 * <li><i>score</i> is included if the scorePredicate is <i>search:score</i> else omitted
 * <li><i>snippet</i> is included if the snippetPredicate is <i>search:snippet</i> else omitted
 * </ol>
 * </p>
 * 
 * @author Mark Hale
 * @author github.com/jgrzebyta
 */
public class LuceneSail2Schema {

	public static final String NAMESPACE = "http://rdf4j.org/contrib/LuceneSail2/";

	public static final IRI SEARCH;

	public static final IRI ALL_MATCHES;

	public static final IRI PROPERTY;

	public static final IRI ALL_PROPERTIES;

	public static final IRI SCORE;

	public static final IRI SNIPPET;

	static {
		ValueFactory vf = SimpleValueFactory.getInstance();

		SEARCH = vf.createIRI(NAMESPACE + "search");
		ALL_MATCHES = vf.createIRI(NAMESPACE + "allMatches");
		PROPERTY = vf.createIRI(NAMESPACE + "property");
		ALL_PROPERTIES = vf.createIRI(NAMESPACE + "allProperties");
		SCORE = vf.createIRI(NAMESPACE + "score");
		SNIPPET = vf.createIRI(NAMESPACE + "snippet");
	}
}
