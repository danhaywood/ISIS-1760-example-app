package com.he.common.domain.impl;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.repository.RepositoryService;
/**
 * 
 * @author nikhil
 * A class that represents the collection of practice which is populated by queries that are run over objects or 
 *fetched via database to get matching one of more practice.
 */
public class Practices 
{

	    public List<Practice> findByName(
	    		@ParameterLayout(named = "Name")
	            final String name) 
	    {
	        final String nameArg = String.format(".*%s.*", name);
	        final List<Practice> practices = container.allMatches(
	                new QueryDefault<>(
	                		Practice.class,
	                        "findByName",
	                        "name", nameArg));
	        return practices;
	    }
	    
	public Practice findById(Integer practiceId) {
		//Query
		repoService.uniqueMatch(new QueryDefault<>(
        		Practice.class,
                "findByUniquePractice",
                "name", practiceId));
		
		return null;
	}

@javax.inject.Inject
DomainObjectContainer container;

@javax.inject.Inject
RepositoryService repoService;
}
