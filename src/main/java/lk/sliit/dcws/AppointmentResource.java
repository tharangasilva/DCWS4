package lk.sliit.dcws;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * Root resource (exposed at "appointments" path)
 */

@Path("appointments/")
@Singleton
public class AppointmentResource {

    private ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    // TODO: Add annotations to make this class match the path /ichannel/appointment/ and be a Singleton.
    // 5% credit

    // TODO: implement getAppointments() returning all available appointments as both text and JSON.
    // 5% credit


 @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAppointments() {
        System.out.println("GET all Appointments (text)");
        String result = "";
        if(this.appointments.size() == 0)
            result = "none";
        for(Appointment appointment: this.appointments)
        {
            result += "ID = " + appointment.id + ", Patient Name = " + appointment.patientName + ", Doctor ID = " + appointment.doctorId + ", Hospital ID = " + appointment.hospitalId + ", Appointment Date = " + appointment.appointmentDate + ", Appointment Number = " + appointment.appointmentNumber +System.getProperty("line.separator");
        }
        return result;
    }

 @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Appointment[] getAppointmentsJson() {
        System.out.println("GET all Appointments (JSON)");
        Appointment[] result = new Appointment[1];
        return this.appointments.toArray(result);
    }





    // TODO: implement getAppointmentsByPatientName(), matching path /ichannel/appointment/patientname/{name}
    // and returning all appointments where {name} is a *substring* match against Appointment.patientName, not a startsWith match.
    // 5% credit


@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("patientName/{name}")
    public String getAppointmentsByPatientName(@PathParam("name") String patientName) {
        System.out.println("GET Appointments by patient name (text): " + patientName);
        String result = "";
        
        if(this.appointments.size() == 0)
            result = "none";
        
        for(Appointment appointment: this.appointments)
        {
            if(appointment.patientName.toUpperCase().contains(patientName.toUpperCase()))
                result += "ID = " + appointment.id + ", Patient Name = " + appointment.patientName + System.getProperty("line.separator");
        }
        return result;
    }



 @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("patientName/{name}")
    public Appointment[] getAppointmentsByPatientNameJson(@PathParam("name") String patientName) {
        System.out.println("GET Appointments by patient name (JSON): " + patientName);
        ArrayList<Appointment> matches = new ArrayList<Appointment>();
        Appointment[] result = new Appointment[1];

        for(Appointment appointment: this.appointments)
        {
            if(appointment.patientName.toUpperCase().contains(patientName.toUpperCase()))
                matches.add(appointment);
        }

        return matches.toArray(result);
    }

 /** 
 */





    // TODO: implement getAppointmentsByDoctor(), matching path /ichannel/appointment/doctor/{id}
    // and returning all appointments where {id} is an exact match against Appointment.doctor.
    // 5% credit


@GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("doctor/{doc}")
    public String getAppointmentsByDoctor(@PathParam("doc") String doctorId) {
        System.out.println("GET Appointments by doctor (text): " + doctorId);
        String result = "";
        
        if(this.appointments.size() == 0)
            result = "none";
        
        for(Appointment appointment: this.appointments)
        {
            if(appointment.doctorId.toUpperCase().contains(doctorId.toUpperCase()))
                result += "ID = " + appointment.id + ", Doctor = " + appointment.doctorId + System.getProperty("line.separator");
        }
        return result;
    }



 @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("doctor/{doc}")
    public Appointment[] getAppointmentsByDoctorJson(@PathParam("doc") String doctorId) {
        System.out.println("GET Appointments by doctor (JSON): " + doctorId);
        ArrayList<Appointment> matches = new ArrayList<Appointment>();
        Appointment[] result = new Appointment[1];

        for(Appointment appointment: this.appointments)
        {
            if(appointment.doctorId.toUpperCase().equals(doctorId.toUpperCase()))
                matches.add(appointment);
        }

        return matches.toArray(result);
    }





    // TODO: implement createAppointment() for the HTTP POST action - 5% credit


 @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAppointment(Appointment appointment) { 
        appointment.id = this.getNextAppointmentId();
        
        String message = "POST Appointment: " + appointment.patientName + " with new ID: " + appointment.id + " doctor ID: " + appointment.doctorId  + " hospital ID: " + appointment.hospitalId + " appointment date: " + appointment.appointmentDate + " appointment number: " + appointment.appointmentNumber;
        System.out.println(message);

        this.appointments.add(appointment);
        return Response.status(201).entity(message).build();
    }




    // TODO: implement updateAppointment() for the HTTP PUT action - 5% credit 


@Path("/{id}/")
 @PUT
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_JSON)
   public Response updateAppointment(Appointment appointment, @PathParam("id") String id) {
        System.out.println("PUT Appointment: " + id + " with " + appointment.patientName);
        Appointment stored = this.findAppointment(id);
        if(stored != null)
        {
            String oldName = stored.patientName;
            stored.patientName = appointment.patientName;
            String message = oldName + " renamed to " + appointment.patientName;

            System.out.println(message);
            return Response.status(200).entity(message).build();
        }
        else
        {
            return Response.status(404).entity(appointment.id + " is not found. Use PUT with a correct ID to modify or use POST to create new entry.").build();
        }
    }




    // TODO: implement deleteAppointment() for the HTTP DELETE action - 5% credit

   @Path("/{id}/")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteAppointment(@PathParam("id") String id) {
        //return "POST name: " + appointment.patientName; 
        System.out.println("DELETE Appointment: " + id);
        Appointment appointment = this.findAppointment(id);
        if(appointment != null)
        {
            this.appointments.remove(appointment);
            String message = "Deleted Appointment " + appointment.patientName;
            System.out.println(message);
            return Response.status(200).entity(message).build();
        }
        else
        {
            return Response.status(404).entity(id + " is not found. Use DELETE with a correct ID to delete.").build();     
        }
    }






    private Appointment findAppointment(String id)
    {
        for(Appointment appointment : this.appointments)
        {
            if(id.equalsIgnoreCase(appointment.id))
            {
                return appointment; 
            }
        }
        return null;
    }

    private String getNextAppointmentId()
    {
        DecimalFormat formatter = new DecimalFormat("app000"); // we generate ID numbers and send it through this formatter to generate ID's

        int test = this.appointments.size() + 1; // start with 1 when we're empty, or with the most likely next value in sequence
        
        // loop until we find the next unused ID to return
        while(true)
        {
            String testId = formatter.format(test);
            if(this.findAppointment(testId) == null)
                return testId; // this ID is not in use so it's available!
            else
                test++; // it's in use, test the next value
        }
    }

}
