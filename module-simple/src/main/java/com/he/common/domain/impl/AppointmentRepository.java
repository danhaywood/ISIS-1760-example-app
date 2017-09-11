package com.he.common.domain.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.DateTime;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Appointment.class
)
public class AppointmentRepository 
{

    public List<Appointment> listAll() 
    {
        return repositoryService.allInstances(Appointment.class);
    }

    public List<Appointment> findByName(final String name) 
    {
        return repositoryService.allMatches(
                new QueryDefault<>(
                		Appointment.class,
                        "findByPractitionerName",
                        "name", name));
    }

    public Appointment create(final DateTime time,final String speciality,final Practice practice,final Practitioner practitioner) 
    {
        final Appointment object = new Appointment(time);
        object.setAppointmenttimetz(time);
        object.setPractice(practice);
        object.setPractitioner(practitioner);
        repositoryService.persist(object);
        serviceRegistry.injectServicesInto(object);
        return object;
    }

    @javax.inject.Inject
    RepositoryService repositoryService;
    
    @javax.inject.Inject
    ServiceRegistry2 serviceRegistry;
    
    @Inject
    DomainObjectContainer container;
}