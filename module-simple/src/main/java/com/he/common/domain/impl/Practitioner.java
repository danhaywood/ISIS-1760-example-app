package com.he.common.domain.impl;

import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.TimeStamp;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "db", table = "practitioner")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "practitionerid")
@javax.jdo.annotations.Version(strategy = VersionStrategy.DATE_TIME, column = "recordedtime")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "findByPractitionerid", value = "SELECT "
		+ "FROM com.he.common.domain.impl.Practitioner " + "WHERE practitionerid=:id ") , @javax.jdo.annotations.Query(name = "findByName", value = "SELECT "
				+ "FROM com.he.common.domain.impl.Practitioner " + "WHERE name.indexOf(:name) >= 0 ") })
@javax.jdo.annotations.Unique(name = "Practitionerclm_name_UNQ", members = { "name" })
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = PractitionerRepository.class)
public class Practitioner implements Comparable<Practitioner> 
{

	public Practitioner(final String name)
{
	setName(name);
}


/**
 * The Practice is listed in the Practitioner its getters and setters are added 
 * https://github.com/johandoornenbal/tutorial_code/blob/master/AsciiDoc/8_petclinic_addowner.adoc
 */

private Practice practice;

@MemberOrder(sequence = "3")
@Column(name = "practiceid", allowsNull = "false")
public Practice getPractice() 
{
    return practice;
}

public void setPractice(final Practice practice) 
{
    this.practice = practice;
}

@javax.jdo.annotations.Column(allowsNull = "false", length = 25)
@Property() // editing disabled by default, see isis.properties
@Getter
@Setter
@Title(prepend = "Doc: ")
private String name;

@javax.jdo.annotations.Column(allowsNull = "false", length = 50)
@Getter
@Setter
@Property(editing = Editing.ENABLED)
private String lastname;

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


@javax.jdo.annotations.Column(allowsNull = "true", length = 15)
@Property(editing = Editing.ENABLED)
@Getter
@Setter
private String role;

@javax.jdo.annotations.Column(allowsNull = "true", length = 30)
@Property(editing = Editing.ENABLED)
@Getter
@Setter
private String city;


/**
 * The Section that describes this default values the Method starts with the word 'choices' for giving a drop down box when it comes to default selections
 * https://isis.apache.org/guides/ugfun/ugfun.html#_ugfun_drop-downs-and-defaults
 * @return
 */
public List<String> choicesCity()
{
	return Arrays.asList("Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide", "Gold Coast", "Canberra",
			"Newcastle", "Wollongong", "Logan City", "Other");
}

// region > updateName (action)
@Action(semantics = SemanticsOf.IDEMPOTENT)
@CollectionLayout
public Practitioner updateName(@Parameter(maxLength = 40) @ParameterLayout(named = "Name") final String name)

{
	setName(name);

	return this;
}

public String default0UpdateName() {
	return getName();
}

public TranslatableString validate0UpdateName(final String name) 
{
	return name != null && name.contains("!") ? TranslatableString.tr("Exclamation mark is not allowed") : null;
}


@Action(semantics = SemanticsOf.IDEMPOTENT)
@CollectionLayout
public Practitioner updatePractitioner(@Parameter(maxLength = 25) @ParameterLayout(named = "Name") final String name,
		@Parameter(maxLength = 40) @ParameterLayout(named = "Last Name") final String lastname,
		@Parameter(maxLength = 255) @ParameterLayout(named = "Address") final String address
		)

{
	setName(name);
	setLastname(lastname);
	setAddress(address);

	return this;
}

public String default0UpdatePractitioner() {return getName();}
public String default1UpdatePractitioner() {return getLastname();}
public String default2UpdatePractitioner() {return getAddress();}


// region > delete (action)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
public void delete() {
	final String title = titleService.titleOf(this);
	messageService.informUser(String.format("'%s' deleted", title));
	repositoryService.remove(this);
}
// endregion

@Override
public String toString() 
{
	return ObjectContracts.toString(this, "name");
}

@Override
public int compareTo(final Practitioner other)
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