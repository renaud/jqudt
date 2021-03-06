/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * License: new BSD
 */
package com.github.jqudt.onto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.openrdf.model.BNode;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import com.github.jqudt.Multiplier;
import com.github.jqudt.Unit;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;

public class UnitFactory {

	private Repository repos;

	private static UnitFactory factory = null;

	private UnitFactory() {
		repos = new SailRepository(new MemoryStore());
		try {
			repos.initialize();
			OntoReader.read(repos, "unit");
			OntoReader.read(repos, "qudt");
			OntoReader.read(repos, "quantity");
			OntoReader.read(repos, "ops.ttl");
			OntoReader.read(repos, "bbp.ttl");
			loadUnits();
		} catch (Exception exception) {
			throw new IllegalStateException(
					"Could not load the QUDT ontologies: "
							+ exception.getMessage(), exception);
		}
	}

	public static UnitFactory getInstance() {
		if (factory == null)
			factory = new UnitFactory();
		return factory;
	}

	public Unit getUnit(String resource) {
		URI uri;
		try {
			uri = new URI(resource);
		} catch (URISyntaxException exception) {
			throw new IllegalStateException("Invalid URI: " + resource,
					exception);
		}
		return getUnit(uri);
	}

	private final List<Unit> ALL_UNITS = new ArrayList<Unit>();

	public List<Unit> getAllUnits() {
		return ALL_UNITS;
	}

	/** Load all units dynamically from the ontlogy */
	private void loadUnits() throws RepositoryException,
			MalformedQueryException, QueryEvaluationException, IOException,
			URISyntaxException {

		RepositoryConnection con = repos.getConnection();

		InputStream sparqlStream = this.getClass().getResourceAsStream(
				"findUnits.sparql");
		Preconditions.checkNotNull(sparqlStream, "sparql query not found");
		String sparqlQ = CharStreams.toString(new InputStreamReader(
				sparqlStream, Charsets.UTF_8));

		TupleQuery tq = con.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQ);
		TupleQueryResult res = tq.evaluate();
		while (res.hasNext()) {
			BindingSet bs = res.next();
			Iterator<Binding> bindIt = bs.iterator();
			Unit unit = new Unit();
			Multiplier multiplier = new Multiplier();
			while (bindIt.hasNext()) {
				Binding binding = bindIt.next();
				String name = binding.getName();
				Value val = binding.getValue();
				// System.out.println(name + "\t" + val);

				if (name.equals("s")) {
					unit.setResource(new URI(val.stringValue()));
				} else if (name.equals("label")) {
					unit.setLabel(val.stringValue());
				} else if (name.equals("symbol")) {
					unit.setSymbol(val.stringValue());
				} else if (name.equals("abbrev")) {
					unit.setAbbreviation(val.stringValue());
				} else if (name.equals("convOffset")) {
					multiplier.setOffset(Double.parseDouble(val.stringValue()));
				} else if (name.equals("convMult")) {
					multiplier.setMultiplier(Double.parseDouble(val
							.stringValue()));
				} else if (name.equals("type")) {
					if (!(val instanceof BNode)) {
						URIImpl typeURI = new URIImpl(val.stringValue());
						if (!shouldBeIgnored(typeURI)) {
							unit.setType(new URI(typeURI.stringValue()));
						}
					}
				} else {
					throw new MalformedQueryException("found bizare binding: "
							+ binding);
				}
			}
			unit.setMultiplier(multiplier);
			ALL_UNITS.add(unit);
		}
	}

	public Unit getUnit(URI resource) {
		if (resource == null)
			throw new IllegalArgumentException("The URI cannot be null");

		ValueFactory f = repos.getValueFactory();
		org.openrdf.model.URI uri = f.createURI(resource.toString());

		Unit unit = new Unit();
		unit.setResource(resource);
		Multiplier multiplier = new Multiplier();

		try {
			RepositoryConnection con = repos.getConnection();
			List<Statement> statements = con.getStatements(uri, null, null,
					true).asList();
			if (statements.size() == 0)
				throw new IllegalStateException("No ontology entry found for: "
						+ resource.toString());

			for (Statement statement : statements) {
				if (statement.getPredicate().equals(QUDT.SYMBOL)) {
					unit.setSymbol(statement.getObject().stringValue());
				} else if (statement.getPredicate().equals(QUDT.ABBREVIATION)) {
					unit.setAbbreviation(statement.getObject().stringValue());
				} else if (statement.getPredicate().equals(
						QUDT.CONVERSION_OFFSET)) {
					multiplier.setOffset(Double.parseDouble(statement
							.getObject().stringValue()));
				} else if (statement.getPredicate().equals(
						QUDT.CONVERSION_MULTIPLIER)) {
					multiplier.setMultiplier(Double.parseDouble(statement
							.getObject().stringValue()));
				} else if (statement.getPredicate().equals(RDFS.LABEL)) {
					unit.setLabel(statement.getObject().stringValue());
				} else if (statement.getPredicate().equals(RDF.TYPE)) {
					Object type = statement.getObject();
					if (type instanceof org.openrdf.model.URI) {
						org.openrdf.model.URI typeURI = (org.openrdf.model.URI) type;
						if (!shouldBeIgnored(typeURI)) {
							unit.setType(new URI(typeURI.stringValue()));
						}
					}
				} else {
					// System.out.println("Ignoring: " + statement);
				}
			}
			unit.setMultiplier(multiplier);
		} catch (Exception exception) {
			throw new IllegalStateException("Could not create the unit: "
					+ exception.getMessage(), exception);
		}

		return unit;
	}

	public List<String> getURIs(String type) {
		URI uri;
		try {
			uri = new URI(type);
		} catch (URISyntaxException exception) {
			throw new IllegalStateException("Invalid URI: " + type, exception);
		}
		return getURIs(uri);
	}

	public List<String> getURIs(URI type) {
		if (type == null)
			throw new IllegalArgumentException("The type cannot be null");

		ValueFactory f = repos.getValueFactory();
		org.openrdf.model.URI uri = f.createURI(type.toString());

		try {
			RepositoryConnection con = repos.getConnection();
			List<Statement> statements = con.getStatements(null, null, uri,
					true).asList();
			if (statements.size() == 0)
				return Collections.emptyList();

			List<String> units = new ArrayList<String>();
			for (Statement statement : statements) {
				units.add(statement.getSubject().toString());
			}
			return units;
		} catch (Exception exception) {
			throw new IllegalStateException("Error while getting the units: "
					+ exception.getMessage(), exception);
		}
	}

	private boolean shouldBeIgnored(org.openrdf.model.URI typeURI) {
		// accept anything outside the QUDT namespace
		if (!typeURI.getNamespace().equals(QUDT.namespace))
			return false;

		if (typeURI.equals(QUDT.SI_DERIVED_UNIT))
			return true;
		if (typeURI.equals(QUDT.SI_BASE_UNIT))
			return true;
		if (typeURI.equals(QUDT.SI_UNIT))
			return true;
		if (typeURI.equals(QUDT.DERIVED_UNIT))
			return true;
		if (typeURI.equals(QUDT.NOT_USED_WITH_SI_UNIT))
			return true;

		// everything else is fine too
		return false;
	}
}
