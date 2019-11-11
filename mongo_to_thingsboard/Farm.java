package mongo_to_thingsboard;

import org.bson.Document;

public class Farm implements ThingsboardComponent{
	
	private Document document;
	private String thingsboardType = "ASSET";
	
	private String farmId;
	private String companyId;
	private String name;
	private String empty;
	private String mongoId;
	private String thingsboardId = null;
	//TODO: private Document additionalInfo;
	
	public Farm(Document document) {
		this.document = document;
		this.setAttributes(this.document.toString());
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
				System.out.println("Farm name: " + this.name);
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
		}
		System.out.println();
	}

	public String getFarmId() {
		return farmId;
	}

	public void setFarmId(String farmId) {
		this.farmId = farmId;
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

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getThingsboardType() {
		return thingsboardType;
	}

	public void setThingsboardType(String thingsboardType) {
		this.thingsboardType = thingsboardType;
	}

	
}
