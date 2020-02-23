package it.ismb.pert.thingsboard;

import org.bson.Document;

public class Building implements Device, ThingsboardComponent{

	private Document document;
	private String thingsboardType = "DEVICE";
	
	private String farmId;
	private String buildingId;
	private String companyId;
	private String name;
	private String empty;
	private String mongoId;
	private String thingsboardId = null;
	private String thingsboardAccessToken = null;
	//TODO: private Document additionalInfo;
	
	private String currentTemperature;
	private String currentHumidity;
	private String currentLuminosity;
	private String currentCO2;
	private String LastUpdateTimestamp;
	
	public Building(Document document) {
		this.document = document;
		setAttributes(this.document.toString());
	}
	
	private void setAttributes(String stringDocument) {
		stringDocument = stringDocument.replace("Document{{", "");
		stringDocument = stringDocument.replaceAll("[}]", "");
		
		String[] stringArray = stringDocument.split(", ");
		for(String x: stringArray) {
			if(x.startsWith("companyId")) {
				this.companyId = x.split("=")[1];
				System.out.println("CompanyId: " + this.companyId);
				continue;
			}
			if(x.startsWith("name")) {
				this.name = x.split("=")[1];
				System.out.println("Building name: " + this.name);
				continue;
			}
			if(x.startsWith("_id")) {
				this.mongoId = x.split("=")[1];
				System.out.println("MongoId: " + this.mongoId);
				continue;
			}
			if(x.startsWith("empty")) {
				this.empty = x.split("=")[1];
				System.out.println("Empty: " + this.empty);
				continue;
			}
			if(x.startsWith("farmId")) {
				this.farmId = x.split("=")[1];
				System.out.println("FarmId: " + this.farmId);
				continue;
			}
			if(x.startsWith("buildingId")) {
				this.buildingId = x.split("=")[1];
				System.out.println("BuildingId: " + this.buildingId);
				continue;
			}
//TELEMETRY DATA
			if(x.startsWith("currentTemperature")) {
				this.currentTemperature = x.split("=")[1];
				System.out.println("CurrentTemperature: " + this.currentTemperature);
				continue;
			}
			if(x.startsWith("currentHumidity")) {
				 this.currentHumidity = x.split("=")[1];
				System.out.println("CurrentHumidity: " + this.currentHumidity);
				continue;
			}
			if(x.startsWith("currentLuminosity")) {
				this.currentLuminosity = x.split("=")[1];
				System.out.println("CurrentLuminosity: " + this.currentLuminosity);
				continue;
			}
			if(x.startsWith("currentCO2")) {
				this.currentCO2 = x.split("=")[1];
				System.out.println("CurrentCO2: " + this.currentCO2);
				continue;
			}
			if(x.startsWith("lastUpdateTimestamp")) {
				this.LastUpdateTimestamp = x.split("=")[1];
				System.out.println("LastUpdateTimestamp: " + this.LastUpdateTimestamp);
				continue;
			}
		}
		System.out.println();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmpty() {
		return empty;
	}

	public void setEmpty(String empty) {
		this.empty = empty;
	}

	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
	}

	public String getThingsboardId() {
		return thingsboardId;
	}

	public void setThingsboardId(String thingsboardId) {
		this.thingsboardId = thingsboardId;
	}

	public String getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(String currentTemperature) {
		this.currentTemperature = currentTemperature;
	}

	public String getCurrentHumidity() {
		return currentHumidity;
	}

	public void setCurrentHumidity(String currentHumidity) {
		this.currentHumidity = currentHumidity;
	}

	public String getCurrentLuminosity() {
		return currentLuminosity;
	}

	public void setCurrentLuminosity(String currentLuminosity) {
		this.currentLuminosity = currentLuminosity;
	}

	public String getCurrentCO2() {
		return currentCO2;
	}

	public void setCurrentCO2(String currentCO2) {
		this.currentCO2 = currentCO2;
	}

	public String getLastUpdateTimestamp() {
		return LastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		LastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getThingsboardAccessToken() {
		return thingsboardAccessToken;
	}

	public void setThingsboardAccessToken(String thingsboardAccessToken) {
		this.thingsboardAccessToken = thingsboardAccessToken;
	}

	public String getThingsboardType() {
		return thingsboardType;
	}

	public void setThingsboardType(String thingsboardType) {
		this.thingsboardType = thingsboardType;
	}
	
	
}
