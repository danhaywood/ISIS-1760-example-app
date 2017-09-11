package com.he.common.domain.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Practice.class
)
public class PracticeRepository {

    public List<Practice> listAll() {
        return repositoryService.allInstances(Practice.class);
    }

    public List<Practice> findByName(final String name) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        Practice.class,
                        "findByName",
                        "name", name));
    }

    public Practice create(final String name,final String fullname) {
        final Practice object = new Practice(name);
        object.setFullname(fullname);
        object.setAddress("Default Address");
        serviceRegistry.injectServicesInto(object);
        repositoryService.persist(object);
        return object;
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;
    
    @Inject
    DomainObjectContainer container;
}
