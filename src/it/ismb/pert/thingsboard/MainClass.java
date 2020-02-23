package it.ismb.pert.thingsboard;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;


public class MainClass {
	
	public static void main(String[] args) { 

		try {
			String configFilePath = "./main.properties";
			Properties prop=new Properties();
			FileInputStream ip= new FileInputStream(configFilePath);
			prop.load(ip);
			
			String thingsboardUser = prop.getProperty("thingsboardUser");
			String thingsboardPass = prop.getProperty("thingsboardPass");
			String thingsboardUrl = prop.getProperty("thingsboardUrl");
			String mongoDbName = prop.getProperty("mongoDbName");
			String pathToSaveFiles = prop.getProperty("pathToSaveFiles");
			String mongoUrl = prop.getProperty("mongoUrl");
			String mongoPortString = prop.getProperty("mongoPort");
			int mongoPort = Integer.parseInt(mongoPortString);
			
			//CREAZIONE DB
			Mongodb database = new Mongodb(mongoUrl, mongoPort, mongoDbName);
			
			//GENERAZIONE TOKEN PER USARE API THINGSBOARD ------------> INSERIRE CREDENZIALI nel config file
			Thingsboard thingsboard = new Thingsboard(thingsboardUser,thingsboardPass, thingsboardUrl);
			thingsboard.generateToken();
			if(thingsboard.getToken()==null) {
				System.out.println("Password o Username sbagliati, o errore nel generare il token.");
				return;
			}
			
			//CREAZIONE DI COMPANIES SU THINGSBOARD		
			database.loadCompanies();	
			thingsboard.addAssets(database.getCompanySet());
	
			//CREAZIONE DI FARMS SU THINGSBOARD		
			database.loadFarms();
			thingsboard.addAssets(database.getFarmSet());
			
			//RELAZIONE COMPANIES-FARMS
			thingsboard.relate(database.getCompanySet(), database.getFarmSet());
			
			//CREAZIONE DI BUILDINGS SU THINGSBOARD
			database.loadBuildings();
			thingsboard.addDevices(database.getBuildingSet());
			//RELAZIONE FARMS-BUILDINGS
			thingsboard.relate(database.getFarmSet(), database.getBuildingSet());
	
			//CREAZIONE DI COMPARTMENT SU THINGSBOARD
			database.loadCompartments();
			thingsboard.addDevices(database.getCompartmentSet());
			//RELAZIONE BUILDINGS-COMPARTMENTS
			HashSet<ThingsboardComponent> compartmentsYetToRelate = thingsboard.relate(database.getBuildingSet(), database.getCompartmentSet());
			//RELAZIONE FARMS-COMPARTMENTS (QUESTA FUNZIONE E' PER I COMPARTMENT CHE NON STANNO SOTTO I BUILDING, MA SOTTO FARM)
			if(!compartmentsYetToRelate.isEmpty())
				thingsboard.relate(database.getFarmSet(),  compartmentsYetToRelate);
	
			//CREAZIONE DI PEN SU THINGSBOARD
			database.loadPens();
			thingsboard.addDevices(database.getPenSet());
			//RELAZIONE COMPARTMENTS-PENS
			HashSet<ThingsboardComponent> pensYetToRelate = thingsboard.relate(database.getCompartmentSet(), database.getPenSet());
			//RELAZIONE BUILDINGS-PENS (QUESTA FUNZIONE E' PER I PEN CHE NON STANNO SOTTO I COMPARTMENT, MA I BUILDING)
			if(!pensYetToRelate.isEmpty())
				thingsboard.relate(database.getBuildingSet(),  pensYetToRelate);
	
			//CREAZIONE DI PIG SU THINGSBOARD
			database.loadPigs();
			thingsboard.addDevices(database.getPigSet());
			//RELAZIONE PENS-PIGS
			thingsboard.relate(database.getPenSet(), database.getPigSet());
	
			//SALVATAGGIO DATI
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Company", database.getCompanySet());
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Farm", database.getFarmSet());
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Building", database.getBuildingSet());
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Compartment", database.getCompartmentSet());
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Pen", database.getPenSet());
			thingsboard.saveThingsboardId(pathToSaveFiles+"save.txt", "Pig", database.getPigSet());	
			//		thingsboard.deleteAllElements("C:/Users/Simone/Desktop/save.txt");
	
			//DISTRUZIONE DEL DATABASE        
			database.mongodbClose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
