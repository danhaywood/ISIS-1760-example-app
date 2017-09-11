package com.he.common.domain.impl;

import java.util.List;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Practitioner.class
)
public class PractitionerRepository {

    public List<Practitioner> listAll() {
        return repositoryService.allInstances(Practitioner.class);
    }

    public List<Practitioner> findByName(final String name) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                		Practitioner.class,
                        "findByName",
                        "name", name));
    }

    public Practitioner create(final String name,final String lastname,final String role,final Practice practice)
    {
        final Practitioner practitioner = new Practitioner(name);
        practitioner.setLastname(lastname);
        practitioner.setAddress("Default Address");
        practitioner.setRole(role);
        practitioner.setPractice(practice);
        serviceRegistry.injectServicesInto(practitioner);
        repositoryService.persist(practitioner);
        return practitioner;
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;
    
}
 