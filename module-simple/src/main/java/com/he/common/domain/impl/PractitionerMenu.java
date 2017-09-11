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

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY,
        objectType = "he.PractitionerMenu",
        repositoryFor = Practice.class
)
@DomainServiceLayout(
        named = "Practitioner",
        		menuOrder = "10"
)
public class PractitionerMenu
{
	 @javax.inject.Inject
	    PractitionerRepository practitionerRepository;
	    
	    
	    @Action(semantics = SemanticsOf.SAFE)
	    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	    @MemberOrder(sequence = "1")
	    public List<Practitioner> listAll() {
	        return practitionerRepository.listAll();
	    }


	    @Action(semantics = SemanticsOf.SAFE)
	    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	    @MemberOrder(sequence = "2")
	    public List<Practitioner> findByName(
	            @ParameterLayout(named="Name")
	            final String name
	    ) {
	        return practitionerRepository.findByName(name);
	    }

	    

	    public static class CreateDomainEvent extends ActionDomainEvent<PractitionerMenu> {}
	    @Action(domainEvent = CreateDomainEvent.class)
	    @MemberOrder(sequence = "3")
	    public Practitioner create(
	            @ParameterLayout(named="Name")
	            final String name,
	            @ParameterLayout(named="Last Name")
	            final String lastName,
	            @ParameterLayout(named="Role")
	            final String role,
	            @ParameterLayout(named="Practice")
	            final Practice practice
	    		) 
	    {
	        return practitionerRepository.create(name,lastName,role,practice);
	    }





}
