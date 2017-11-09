package com.he.common.domain.impl;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.DateTime;

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
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.util.ObjectContracts;

import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, schema = "db", table = "appointment")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "appointmentid")
@javax.jdo.annotations.Version(strategy = VersionStrategy.DATE_TIME, column = "recordedtime")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "findByPractitionerName", value = "SELECT "
		+ "FROM com.he.common.domain.impl.Appointment " + "WHERE practitioner.name= :name ") , @javax.jdo.annotations.Query(name = "findByTime", value = "SELECT "
				+ "FROM com.he.common.domain.impl.Appointment " + "WHERE appointmenttime > :time ") })
@javax.jdo.annotations.Unique(name = "AppointmentClm_name_UNQ", members = { "appointmenttime" })
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = AppointmentRepository.class)
public class Appointment implements Comparable<Appointment> 
{

	public Appointment(final DateTime time)
{
	setAppointmenttime(time);
}

	

private Practitioner practitioner;

@MemberOrder(sequence = "4")
@Column(name = "practitionerid", allowsNull = "false")
public Practitioner getPractitioner() 
{
    return practitioner;
}

public void setPractitioner(final Practitioner practitioner) 
{
    this.practitioner = practitioner;
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


@javax.jdo.annotations.Column(allowsNull = "false", /*length = 25,*/name= "appointmenttime")
@Property() // editing disabled by default, see isis.properties
@Getter
@Setter
@Title(prepend = "APT : ")
private DateTime appointmenttime;

@javax.jdo.annotations.Column(allowsNull = "false", /*length = 25,*/name= "appointmenttimetz")
@Getter
@Setter
private DateTime appointmenttimetz;


@javax.jdo.annotations.Column(allowsNull = "true",length = 30,name= "Speciality")
@Property(editing = Editing.ENABLED)
@Getter
@Setter
private String speciality;

/*@javax.jdo.annotations.Column(allowsNull = "true")
@Property(editing = Editing.DISABLED)
@Getter
@Setter
private TimeStamp recordedtime;*/

/**
 * Property doesn't exist in DB - read the derived properties section for More Details.
 * https://isis.apache.org/guides/ugfun/ugfun.html#_ugfun_programming-model_properties
 */
@Property(editing = Editing.DISABLED,notPersisted = true)
private String name;


@NotPersistent
public String getName()
{
	return (this.getPractitioner() !=null && this.getPractitioner().getName() != null) ? this.getPractitioner().getName(): "Default";
}


@javax.jdo.annotations.Column(allowsNull = "true",name ="createdby")
@Property(editing = Editing.ENABLED)
@Getter
@Setter
private String createdBy;


// region > updateName (action)
@Action(semantics = SemanticsOf.IDEMPOTENT)
@CollectionLayout
public Appointment updateAppointment(@Parameter(maxLength = 40) @ParameterLayout(named = "Time Of Appointment") final DateTime time)

{
	setAppointmenttime(time);

	return this;
}

public DateTime default0UpdateAppointment() {
	return getAppointmenttime();
}

@Action(semantics = SemanticsOf.IDEMPOTENT)
@CollectionLayout
public Appointment updateAppointment(@Parameter(maxLength = 25) @ParameterLayout(named = "Appointment Time") final DateTime time,
		@Parameter(maxLength = 40) @ParameterLayout(named = "Global Time ") final DateTime timetz,
		@Parameter(maxLength = 255) @ParameterLayout(named = "Address") final String address
		)

{
	setAppointmenttime(time);
	return this;
}


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
public int compareTo(final Appointment other)
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