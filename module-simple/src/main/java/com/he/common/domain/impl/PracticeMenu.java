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
        objectType = "he.PracticeMenu",
        repositoryFor = Practice.class
)
@DomainServiceLayout(
        named = "Practice",
        		menuOrder = "05"
)
public class PracticeMenu
{

    @javax.inject.Inject
    PracticeRepository practiceRepository;
    
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "1")
    public List<Practice> listAll() {
        return practiceRepository.listAll();
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    @MemberOrder(sequence = "2")
    public List<Practice> findByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return practiceRepository.findByName(name);
    }

    

    public static class CreateDomainEvent extends ActionDomainEvent<PracticeMenu> {}
    @Action(domainEvent = CreateDomainEvent.class)
    @MemberOrder(sequence = "3")
    public Practice create(
            @ParameterLayout(named="Name")
            final String name,
            @ParameterLayout(named="FullName")
            final String fullName
    		) {
        return practiceRepository.create(name,fullName);
    }




}
