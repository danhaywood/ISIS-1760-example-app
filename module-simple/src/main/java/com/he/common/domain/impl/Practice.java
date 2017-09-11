package com.he.common.domain.impl;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.util.ObjectContracts;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "db", table = "practice")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "practiceid")
@javax.jdo.annotations.Version(strategy = VersionStrategy.DATE_TIME, column = "recordedtime")


@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "findByName", value = "SELECT "
		+ "FROM com.he.common.domain.impl.Practice " + "WHERE name.indexOf(:name) >= 0 ") ,@javax.jdo.annotations.Query(name = "findByUniquePractice", value = "SELECT "
				+ "FROM com.he.common.domain.impl.Practice " + "WHERE practiceid=:name ") })
@javax.jdo.annotations.Unique(name = "Practiceclm_name_UNQ", members = { "name" })
//objectType inferred from @PersistenceCapable#schema
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = PracticeRepository.class) 
/**
 * 
 * @author nikhil
 *The Class represents a <code>org.apache.isis.applib.annotation.DomainObject</code> representing a practice 
 *this maps to a table db.practice in database.
 */
public class Practice implements Comparable<Practice>
{
	// -TODO add practiceid in addition to Name -looks like id is not needed the framework manages it.

	
	public Practice(final String name)
	{
		setName(name);
	}
	
	
	@javax.jdo.annotations.Column(allowsNull = "false", length = 25)
	@Property() // editing disabled by default, see isis.properties
	@Getter
	@Setter
	@Title(prepend = "PMS: ")
	private String name;

	@javax.jdo.annotations.Column(allowsNull = "false", length = 50)
	@Getter
	@Setter
	@Property(editing = Editing.ENABLED)
	private String fullname;

	@javax.jdo.annotations.Column(allowsNull = "true", length = 255)
	@Property(editing = Editing.ENABLED)
	@Getter
	@Setter
	private String address;

	@javax.jdo.annotations.Column(allowsNull = "true", length = 1)
	@Property(editing = Editing.ENABLED)
	@Getter
	@Setter
	private String isactive;
	
	/**
	 * The Section that describes this default values
	 * https://isis.apache.org/guides/ugfun/ugfun.html#_ugfun_drop-downs-and-defaults
	 * @return
	 */
	public List<String> choicesIsactive()
	{
		return Arrays.asList("Y", "N");
	}

	@javax.jdo.annotations.Column(allowsNull = "true", length = 30)
	@Property(editing = Editing.ENABLED)
	@Getter
	@Setter
	private String city;

	/**
	 * The Section that describes this default values
	 * https://isis.apache.org/guides/ugfun/ugfun.html#_ugfun_drop-downs-and-defaults
	 * @return
	 */
	public List<String> choicesCity()
	{
		return Arrays.asList("Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Gold Coast", "Canberra",
				"Newcastle", "Wollongong", "Logan City", "Other");
	}
	
	
	
	/**
	 * This ISIS NOTES: 
	 */
	@Persistent(mappedBy = "practice", dependentElement = "false")
    private SortedSet<Practitioner> practitioners = new TreeSet<Practitioner>();

    @MemberOrder(sequence = "3")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<Practitioner> getPractitioner() 
    {
        return practitioners;
    }

    public void setPractitioner(final SortedSet<Practitioner> pracitioners)
    {
        this.practitioners = pracitioners;
    }

    
    /**
	 * This ISIS NOTES: 
	 */
	@Persistent(mappedBy = "practice", dependentElement = "false")
    private SortedSet<Appointment> appointments = new TreeSet<Appointment>();

    @MemberOrder(sequence = "3")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<Appointment> getAppointment() 
    {
        return appointments;
    }

    public void setAppointment(final SortedSet<Appointment> appointments)
    {
        this.appointments = appointments;
    }
    
	// region > updateName (action)
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@CollectionLayout
	public Practice updateName(@Parameter(maxLength = 40) @ParameterLayout(named = "Name") final String name)

	{
		setName(name);

		return this;
	}

	public String default0UpdateName() 
	{
		return getName();
	}

	public TranslatableString validate0UpdateName(final String name) {
		return name != null && name.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
	}
	
	
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@CollectionLayout
	public Practice updatePractice(@Parameter(maxLength = 25) @ParameterLayout(named = "Name") final String name,
			@Parameter(maxLength = 40) @ParameterLayout(named = "Full Name") final String fullName,
			@Parameter(maxLength = 255) @ParameterLayout(named = "Address") final String address
			)

	{
		setName(name);
		setFullname(fullName);
		setAddress(address);

		return this;
	}

	public String default0UpdatePractice()
	{
		return getName();
	}
	
	public String default1UpdatePractice()
	{
		return getFullname();
	}
	
	public String default2UpdatePractice()
	{
		return getAddress();
	}


	// region > delete (action)
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public void delete() 
	{
		final String title = titleService.titleOf(this);
		messageService.informUser(String.format("'%s' deleted", title));
		repositoryService.remove(this);
	}
	// endregion

	// region > toString, compareTo
	@Override
	public String toString() 
	{
		return ObjectContracts.toString(this, "name");
	}

	@Override
	public int compareTo(final Practice other) 
	{
		return ObjectContracts.compare(this, other, "name");
	}

	// region > injected services
	@javax.inject.Inject
	RepositoryService repositoryService;

	@javax.inject.Inject
	TitleService titleService;

	@javax.inject.Inject
	MessageService messageService;

}