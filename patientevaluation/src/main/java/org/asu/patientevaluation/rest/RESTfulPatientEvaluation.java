package org.asu.patientevaluation.rest;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.asu.patientevaluation.util.CSVParser;

 
/**
 * 
 * Description : Main service class that provides decision based on patient csv and db icd10cm code values
 * 
 * @author ripudamansingh
 */

@Path("rest")
public class RESTfulPatientEvaluation 
{
	private List<String> responseIDs;
	private Map<String,String> patientdata;
	private Map<String,String> icd10cmdata;
	@Context
	private ServletContext context; 
	
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@POST
	public String getPatientIDs(@FormParam("icd10cm") String icd10cm, @FormParam("patientcsv") String patientcsv) {
		
		// error handling for missing request params
		if(icd10cm == null || icd10cm.isEmpty()) {
			return "missing request params..";
		}	
		if(patientcsv == null || patientcsv.isEmpty()) {
			return "missing request params..";
		}
		
		// get patient data from csv
		CSVParser csvparser =  new CSVParser();
		csvparser.setPath(patientcsv);
		try {
			patientdata = csvparser.parseCSV();
		} catch (FileNotFoundException e1) {			
			e1.printStackTrace();
			return "invalid csv path..";
		}
		
		// get icd10cm data from db
		icd10cmdata = new TreeMap<>();		
       	Connection con = (Connection) context.getAttribute("DbConnection");
    	PreparedStatement ps = null;
    	ResultSet rs = null;
        try {
            ps = con.prepareStatement("select icd10cmid,desc_short,desc_long from icd10cm where icd10cmid like ?");
            ps.setString(1, icd10cm+"%");
            rs = ps.executeQuery();   
            if(!rs.next()) {
            	return "invalid icd10cm code..";
            }
            while(rs.next()) {
            	icd10cmdata.put(rs.getString("icd10cmid"), rs.getString("desc_short"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database connection problem";
        } finally {
            try {
                rs.close();
                ps.close();
            } catch (SQLException e) {
                return "SQLException in closing PreparedStatement or ResultSet";
            }  
        }
        
        // find patients with diseases specific to icd10cm code
        responseIDs = new LinkedList<>();
        for(Map.Entry<String, String> patiententry : patientdata.entrySet()) {
        	if(icd10cmdata.containsValue(patiententry.getValue())) {
        		responseIDs.add(patiententry.getKey());
        	}
        }
        
		return "Patient IDs : "+responseIDs.toString();
	}
}

