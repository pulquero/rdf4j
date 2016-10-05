/*******************************************************************************
 * Copyright (c) 2016 Eclipse RDF4J contributors, Aduna, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *******************************************************************************/
package org.eclipse.rdf4j.sail.lucene;

import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.resultio.text.csv.SPARQLResultsCSVWriter;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.sail.lucene.function.MockFunction;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.spin.SpinSail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test how {@link MockFunction} works in different cases.
 */
public abstract class AbstractMockFunctionTest {

	private Repository repository;

	private RepositoryConnection connection;

	private static Logger log = LoggerFactory.getLogger(AbstractMockFunctionTest.class);

	@Before
	public void setUp()
		throws Exception
	{
		// load data into memory store
		MemoryStore store = new MemoryStore();

		// prepare sLucene wrapper
		// LuceneSail sail = new LuceneSail();
		//configure(sail);
		//sail.setBaseSail(store);

		// add Support for SPIN function
		//SpinSail spin = new SpinSail(sail);
		SpinSail spin = new SpinSail(store);

		repository = new SailRepository(spin);
		repository.initialize();

		connection = repository.getConnection();
		populate(connection);

		// validate population
		int count = countStatements(connection);
		log.info("storage contains {} triples", count);
		assert count > 0;
	}

	@After
	public void tearDown()
		throws IOException, RepositoryException
	{
		connection.close();
		repository.shutDown();
	}

	protected abstract void configure(LuceneSail sail);

	protected abstract void populate(RepositoryConnection connection)
		throws Exception;

	/**
	 * It works and returns a list: Foo, TestMessage
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSimpleCase()
		throws Exception
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("select * where {\n");
		buffer.append("  ?pred <" + MockFunction.TEST1_PROPERTY + "> (\"Foo\" \"Testmessage\")\n");
		buffer.append("}");
		log.info("Request \n{}\n", buffer.toString());
		try {
			connection.begin();

			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, buffer.toString());
			try (TupleQueryResult results = query.evaluate()) {
				int count = Iterations.asList(results).size();
				Assert.assertTrue(count > 0);
				log.info("**** Number of results: {}", count);
			}
			log.debug("query: \n{}", query);
			printTupleResult(query);
		}
		catch (Exception e) {
			connection.rollback();
			throw e;
		}
		finally {
			connection.commit();
		}
	}

	@Test
	public void testMultipleReturn1()
		throws Exception
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("select * where {\n");
		buffer.append("  ?pred <" + MockFunction.TEST2_PROPERTY + "> (\"berry\")\n");
		buffer.append("}");
		log.info("Request \n{}\n", buffer.toString());
		try {
			connection.begin();

			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, buffer.toString());
			printTupleResult(query);
		}
		catch (Exception e) {
			connection.rollback();
			throw e;
		}
		finally {
			connection.commit();
		}
	}

	@Test(expected = QueryEvaluationException.class)
	public void testMultipleReturn1a()
		throws Exception
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("select * where {\n");
		buffer.append("  ?pred <" + MockFunction.TEST2_PROPERTY + "> (\"berry\" \"very\")\n");
		buffer.append("}");
		log.info("Request \n{}\n", buffer.toString());
		try {
			connection.begin();

			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, buffer.toString());
			printTupleResult(query);
		}
		catch (Exception e) {
			connection.rollback();
			throw e;
		}
		finally {
			connection.commit();
		}
	}

	@Test
	public void testMultipleReturn2()
		throws Exception
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("select ?pred ?length where {\n");
		buffer.append("  (?pred ?length) <" + MockFunction.TEST2_PROPERTY + "> (\"berry\")\n");
		buffer.append("}");
		log.info("Request \n{}\n", buffer.toString());
		try {
			connection.begin();

			TupleQuery query = connection.prepareTupleQuery(QueryLanguage.SPARQL, buffer.toString());
			try (TupleQueryResult results = query.evaluate()) {
				int count = Iterations.asList(results).size();
				Assert.assertTrue(count > 0);
				log.info("Number of results: {}", count);
			}
			// logging
			//printTupleResult(query);
		}
		catch (Exception e) {
			connection.rollback();
			throw e;
		}
		finally {
			connection.commit();
		}
	}

	public int countStatements(RepositoryConnection con)
		throws Exception
	{
		try {
			connection.begin();

			RepositoryResult<Statement> sts = connection.getStatements(null, null, null, new Resource[] {});
			return Iterations.asList(sts).size();
		}
		catch (Exception e) {
			connection.rollback();
			throw e;
		}
		finally {
			connection.commit();
		}
	}

	public int countTupleResults(TupleQueryResult results)
		throws Exception
	{
		return Iterations.asList(results).size();
	}

	public int countGraphResults(GraphQueryResult results)
		throws Exception
	{
		return Iterations.asList(results).size();
	}

	protected void printGraphResult(GraphQuery query) {
		ByteArrayOutputStream resultoutput = new ByteArrayOutputStream();
		query.evaluate(new TurtleWriter(resultoutput));
		log.info("graph result:");
		log.info("\n=============\n" + new String(resultoutput.toByteArray()) + "\n=============");
	}

	protected void printTupleResult(TupleQuery query) {
		ByteArrayOutputStream resultoutput = new ByteArrayOutputStream();
		query.evaluate(new SPARQLResultsCSVWriter(resultoutput));
		log.info("tuple result:");
		log.info("\n=============\n" + new String(resultoutput.toByteArray()) + "\n=============");
	}
}
