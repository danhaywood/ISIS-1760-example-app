package com.he.common.domain.impl;

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.joda.time.DateTime;



@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "he.AppointmentMenu",
        repositoryFor = Appointment.class
)
@DomainServiceLayout(
        named = "Appointment",
        		menuOrder = "11"
)
public class AppointmentMenu
{
	 @javax.inject.Inject
	 AppointmentRepository appointmentRepository;
	    
	    
	    @Action(semantics = SemanticsOf.SAFE)
	    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	    @MemberOrder(sequence = "1")
	    public List<Appointment> listAll() {
	        return appointmentRepository.listAll();
	    }


	    @Action(semantics = SemanticsOf.SAFE)
	    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	    @MemberOrder(sequence = "2")
	    public List<Appointment> findByName(
	            @ParameterLayout(named="Name")
	            final String name
	    ) {
	        return appointmentRepository.findByName(name);
	    }

	    

	    public static class CreateDomainEvent extends ActionDomainEvent<PractitionerMenu> {}
	    @Action(domainEvent = CreateDomainEvent.class)
	    @MemberOrder(sequence = "3")
	    public Appointment create(
	            @ParameterLayout(named="Appointment Time (Local)")
	            final DateTime time,
	            @ParameterLayout(named="Speciality")
	            final String speciality,
	            @ParameterLayout(named="Practice")
	            final Practice practice,
	            @ParameterLayout(named="Practitioner")
	            final Practitioner practitioner
	    		) {
	        return appointmentRepository.create(time,speciality,practice,practitioner);
	    }





}