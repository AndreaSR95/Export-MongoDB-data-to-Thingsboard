package mongo_to_thingsboard;

import java.util.HashSet;

public class MainClass {
	
	public static void main(String[] args) {
//CREAZIONE DB
		Mongodb database = new Mongodb("iof2020db");
		
//GENERAZIONE TOKEN PER USARE API THINGSBOARD ------------> INSERIRE CREDENZIALI
		Thingsboard.generateToken("INSERT_USER","INSERT_PASSWORD");
		if(Thingsboard.getToken()==null) {
			System.out.println("Password o Username sbagliati, o errore nel generare il token.");
			return;
		}
		
//CREAZIONE DI COMPANIES SU THINGSBOARD		
		database.loadCompanies();	
		Thingsboard.addAssets(database.getCompanySet());

//CREAZIONE DI FARMS SU THINGSBOARD		
		database.loadFarms();
		Thingsboard.addAssets(database.getFarmSet());
		
//RELAZIONE COMPANIES-FARMS
		Thingsboard.relate(database.getCompanySet(), database.getFarmSet());
		
//CREAZIONE DI BUILDINGS SU THINGSBOARD
		database.loadBuildings();
		Thingsboard.addDevices(database.getBuildingSet());
//RELAZIONE FARMS-BUILDINGS
		Thingsboard.relate(database.getFarmSet(), database.getBuildingSet());

//CREAZIONE DI COMPARTMENT SU THINGSBOARD
		database.loadCompartments();
		Thingsboard.addDevices(database.getCompartmentSet());
//RELAZIONE BUILDINGS-COMPARTMENTS
		HashSet<ThingsboardComponent> compartmentsYetToRelate = Thingsboard.relate(database.getBuildingSet(), database.getCompartmentSet());
//RELAZIONE FARMS-COMPARTMENTS (QUESTA FUNZIONE E' PER I COMPARTMENT CHE NON STANNO SOTTO I BUILDING, MA SOTTO FARM)
		if(!compartmentsYetToRelate.isEmpty())
			Thingsboard.relate(database.getFarmSet(),  compartmentsYetToRelate);
		
//CREAZIONE DI PEN SU THINGSBOARD
		database.loadPens();
		Thingsboard.addDevices(database.getPenSet());
//RELAZIONE COMPARTMENTS-PENS
		HashSet<ThingsboardComponent> pensYetToRelate = Thingsboard.relate(database.getCompartmentSet(), database.getPenSet());
//RELAZIONE BUILDINGS-PENS (QUESTA FUNZIONE E' PER I PEN CHE NON STANNO SOTTO I COMPARTMENT, MA I BUILDING)
		if(!pensYetToRelate.isEmpty())
			Thingsboard.relate(database.getBuildingSet(),  pensYetToRelate);
		
//CREAZIONE DI PIG SU THINGSBOARD
		database.loadPigs();
		Thingsboard.addDevices(database.getPigSet());
//RELAZIONE PENS-PIGS
		Thingsboard.relate(database.getPenSet(), database.getPigSet());
		
//SALVATAGGIO DATI
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Company", database.getCompanySet());
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Farm", database.getFarmSet());
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Building", database.getBuildingSet());
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Compartment", database.getCompartmentSet());
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Pen", database.getPenSet());
		Thingsboard.saveThingsboardId("C:/Users/Simone/Desktop/save.txt", "Pig", database.getPigSet());	
//		Thingsboard.deleteAllElements("C:/Users/Simone/Desktop/save.txt");
		
//DISTRUZIONE DEL DATABASE        
		database.mongodbClose();
	}
}
