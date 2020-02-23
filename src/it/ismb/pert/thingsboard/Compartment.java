package it.ismb.pert.thingsboard;

import org.bson.Document;

public class Compartment implements Device{
	
	private Document document;
	private String thingsboardType = "DEVICE";
	
	private String mongoId;
	private String buildingId;
	private String companyId;
	private String compartmentId;
	private String farmId;
	private String empty;
	private String name;
	private String sex;
	private String additionalInfo = "";
	
	private String thingsboardId = null;
	private String thingsboardAccessToken = null;

	
	public Compartment(Document document) {
		this.document = document;
		setAttributes(this.document.toString());
	}
	
	private void setAttributes(String stringDocument) {
//IDEA PER ESTRARRE I CAMPI DI ADDITIONAL INFO CON POCO SFORZO (NON HO BISOGNO DI CONTROLLARE LE PARENTESI GRAFFE):
//SEPARO IL DOCUMENTO CON SPLIT(", "). QUANDO TROVO UN ITERAZIONE CHE NON E' ENTRATA IN NESSUN IF, ALLORA FARA' SICURAMENTE PARTE DI ADDITIONAL INFO.
//SI E' ANCHE MESSO UN IF CHE CERCA "ADDITIONAL INFO" PER TOGLIERE LA PAROLA "ADDITIONAL INFO"
		
//		System.out.println(stringDocument);
		stringDocument = stringDocument.replace("Document{{", "");
		stringDocument = stringDocument.replaceAll("[}]", "");
//		System.out.println(stringDocument);
		
		String[] stringArray = stringDocument.split(", ");
		for(String x: stringArray) {
			if(x.startsWith("companyId")) {
				this.companyId = x.split("=")[1];
				System.out.println("CompanyId: " + this.companyId);
				continue;
			}
			if(x.startsWith("name")) {
				if(x.split("=").length>1) {
					this.name = x.split("=")[1];
					System.out.println("Compartment name: " + this.name);
				}
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
			if(x.startsWith("compartmentId")) {
				this.compartmentId = x.split("=")[1];
				System.out.println("CompartmentId: " + this.compartmentId);
				continue;
			}
			if(x.startsWith("sex")) {
				if(x.split("=").length>1)
					this.sex = x.split("=")[1];
				System.out.println("Sex: " + this.sex);
				continue;
			}
			if(x.startsWith("additionalInfo")) {
				x = x.substring(15);
				this.additionalInfo += x;
				continue;
			}
			this.additionalInfo += " " + x;
		}
		if(this.name == null) {
			this.name = stringFromAdditionalInfo() + " Compartment " + this.compartmentId;
			System.out.println("Name: " + this.name);
		}
		System.out.println(this.additionalInfo);
		System.out.println();
	}

	private String stringFromAdditionalInfo() {
		String str = this.additionalInfo.substring(1);
		int i;
		
		for(i=0; i<str.length(); i++) {
			if(Character.isUpperCase(str.charAt(i))) {
				break;
			}
		}
		return this.additionalInfo.charAt(0) + str.substring(0, i);
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getMongoId() {
		return mongoId;
	}

	public void setMongoId(String mongoId) {
		this.mongoId = mongoId;
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

	public String getCompartmentId() {
		return compartmentId;
	}

	public void setCompartmentId(String compartmentId) {
		this.compartmentId = compartmentId;
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	public String getEmpty() {
		return empty;
	}

	public void setEmpty(String empty) {
		this.empty = empty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getThingsboardId() {
		return thingsboardId;
	}

	public void setThingsboardId(String thingsboardId) {
		this.thingsboardId = thingsboardId;
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
