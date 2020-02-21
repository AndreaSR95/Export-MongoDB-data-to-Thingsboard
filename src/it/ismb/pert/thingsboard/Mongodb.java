package it.ismb.pert.thingsboard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class Mongodb {
	
	private MongoClient mongoClient;
	private MongoDatabase database;
	private Logger log;
	private String dbName;
	
	private HashSet<ThingsboardComponent> buildingSet;
	private HashSet<ThingsboardComponent> companySet;
	private HashSet<ThingsboardComponent> compartmentSet;
	private HashSet<ThingsboardComponent> farmSet;
	private HashSet<ThingsboardComponent> penSet;
	private HashSet<ThingsboardComponent> pigSet;
//IN QUANTO SONO TUTTE OPERAZIONI DI LETTURA, E MAI DI INSERZIONE, SI E' DECISO DI SEMPLIFICARE IL CODICE E LASCIARE PERDERE L'USO DI SEZIONI SYNCHRONIZED.
//SE DOVESSERO ESSERE USATE PIU' INSTANZE DI QUESTA CLASSE, SEMPLICEMENTE "SI AVREBBERO DEI DOPPIONI", CIOE' DUE OGGETTI SU CUI CI SONO GLI STESSI DATI DA LEGGERE.
//NEL CASO SI DOVESSE AGGIUNGERE L'INSERZIONE, E' CONSIGLIATO DI AGGIUNGERE OPPORTUNAMENTE SEZIONI SYNCHRONIZED E DI MODIFICARE GLI ATTRIBUITI IN STATIC.

	public Mongodb(String dbURL, int dbPort, String dbName) {
		this.dbName = dbName;
		
        log = Logger.getLogger( "org.mongodb.driver" );
		log.setLevel(Level.SEVERE);
		
        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Arrays.asList(new ServerAddress(dbURL, dbPort))))
                        .build());
        database = mongoClient.getDatabase(this.dbName);
	}
	
//	SI SAREBBE POTUTO UNIRE TUTTE QUESTE FUNZIONI CHE HANNO PRATICAMENTE IL CODICE UGUALE, AGGIUNGENDO QUALCHE PARAMETRO,
//	MA VIENE PIU' SEMPLICE PER CHI LE CHIAMA AVERE NOMI DIVERSI PER COLLEZIONI DIVERSE
	public void loadCompanies() {
		if(this.companySet!=null)
			return;
		MongoCollection<Document> companyCollection = database.getCollection("company");
        
		HashSet<ThingsboardComponent> companies = new HashSet<ThingsboardComponent>();
		for(Document document: companyCollection.find()) {
			companies.add(new Company(document));
		}
		this.companySet = companies;
	}
	
	public void loadBuildings() {
		if(this.buildingSet!=null)
			return;
		MongoCollection<Document> buildingCollection = database.getCollection("building");
		HashSet<ThingsboardComponent> buildings = new HashSet<ThingsboardComponent>();
		for(Document document: buildingCollection.find()) {
			buildings.add(new Building(document));
		}
		this.buildingSet = buildings;
	}
	
	public void loadCompartments() {
//		int i=0;
		if(this.compartmentSet!=null)
			return;
		MongoCollection<Document> compartmentCollection = database.getCollection("compartment");
		HashSet<ThingsboardComponent> compartments = new HashSet<ThingsboardComponent>();
		for(Document document: compartmentCollection.find()) {
//			if(i++>=5)
//				break;
			compartments.add(new Compartment(document));
		}
		this.compartmentSet = compartments;	
	}
	
	public void loadFarms() {
		if(this.farmSet!=null)
			return;
		MongoCollection<Document> farmCollection = database.getCollection("farm");
		HashSet<ThingsboardComponent> farms = new HashSet<ThingsboardComponent>();
		for(Document document: farmCollection.find()) {
			farms.add(new Farm(document));
		}
		this.farmSet = farms;		
	}
	
	public void loadPens() {
//		int i=0;
		if(this.penSet!=null)
			return;
		MongoCollection<Document> penCollection = database.getCollection("pen");
		HashSet<ThingsboardComponent> pens = new HashSet<ThingsboardComponent>();
		for(Document document: penCollection.find()) {
//			if(i++>=5)
//				break;
			pens.add(new Pen(document));
		}
		this.penSet = pens;			
	}
	
	public void loadPigs() {
//		int i=0;
		if(this.pigSet!=null)
			return;
		MongoCollection<Document> pigCollection = database.getCollection("pig");
		HashSet<ThingsboardComponent> pigs = new HashSet<ThingsboardComponent>();
		for(Document document: pigCollection.find()) {
			pigs.add(new Pig(document));
//			if(i++>=5)
//				break;
		}
		this.pigSet = pigs;		
	}
	
	public void mongodbClose() {
		mongoClient.close();
	}

	
//GETTERS SETTERS
	public HashSet<ThingsboardComponent> getBuildingSet() {
		return buildingSet;
	}

	public void setBuildingSet(HashSet<ThingsboardComponent> buildingSet) {
		this.buildingSet = buildingSet;
	}

	public HashSet<ThingsboardComponent> getCompanySet() {
		return companySet;
	}

	public void setCompanySet(HashSet<ThingsboardComponent> companySet) {
		this.companySet = companySet;
	}

	public HashSet<ThingsboardComponent> getCompartmentSet() {
		return compartmentSet;
	}

	public void setCompartmentSet(HashSet<ThingsboardComponent> compartmentSet) {
		this.compartmentSet = compartmentSet;
	}

	public HashSet<ThingsboardComponent> getFarmSet() {
		return farmSet;
	}

	public void setFarmSet(HashSet<ThingsboardComponent> farmSet) {
		this.farmSet = farmSet;
	}

	public HashSet<ThingsboardComponent> getPenSet() {
		return penSet;
	}

	public void setPenSet(HashSet<ThingsboardComponent> penSet) {
		this.penSet = penSet;
	}

	public HashSet<ThingsboardComponent> getPigSet() {
		return pigSet;
	}

	public void setPigSet(HashSet<ThingsboardComponent> pigSet) {
		this.pigSet = pigSet;
	}
	
	
}


