package mongo_to_thingsboard;

import org.bson.Document;

public class Pig implements Device{
	
	private Document document;
	private String thingsboardType = "DEVICE";
	
	private String mongoId;//
	private String pigId;//
	private String buildingId;//
	private String companyId;//
	private String compartmentId;//
	private String penId;//
	private String name;
	private String farmId;//
	private String empty;//
	private String sex;//
	private String additionalInfo = "";//
	private String endTimestampAcquisition;
	private String endTimestampMonitoring;
	private String startTimestampAcquisition;
	private String startTimestampMonitoring;


	private String thingsboardId = null;
	private String thingsboardAccessToken = null;
	
	public Pig(Document document) {
		this.document = document;
		setAttributes(this.document.toString());
	}
	
	private void setAttributes(String stringDocument) {
		//IDEA PER ESTRARRE I CAMPI DI ADDITIONAL INFO CON POCO SFORZO (NON HO BISOGNO DI CONTROLLARE LE PARENTESI GRAFFE):
		//SEPARO IL DOCUMENTO CON SPLIT(", "). QUANDO TROVO UN ITERAZIONE CHE NON E' ENTRATA IN NESSUN IF, ALLORA FARA' SICURAMENTE PARTE DI ADDITIONAL INFO.
		//SI E' ANCHE MESSO UN IF CHE CERCA "ADDITIONAL INFO" PER TOGLIERE LA PAROLA "ADDITIONAL INFO"
				
				System.out.println(stringDocument);
				stringDocument = stringDocument.replace("Document{{", "");
				stringDocument = stringDocument.replaceAll("[}]", "");
				System.out.println(stringDocument);
				
				String[] stringArray = stringDocument.split(", ");
				for(String x: stringArray) {
					if(x.startsWith("pigId")) {
						this.pigId = x.split("=")[1];
						System.out.println("PigId: " + this.pigId);
						continue;
					}
					if(x.startsWith("penId")) {
						this.penId = x.split("=")[1];
						System.out.println("PenId: " + this.penId);
						continue;
					}
					if(x.startsWith("companyId")) {
						this.companyId = x.split("=")[1];
						System.out.println("CompanyId: " + this.companyId);
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
					if(x.startsWith("endTimestampAcquisition")) {
						if(x.split("=").length>1)
							this.endTimestampAcquisition = x.split("=")[1];
						System.out.println("endTimestampAcquisition: " + this.endTimestampAcquisition);
						continue;
					}
					if(x.startsWith("endTimestampMonitoring")) {
						if(x.split("=").length>1)
							this.endTimestampMonitoring = x.split("=")[1];
						System.out.println("endTimestampMonitoring: " + this.endTimestampMonitoring);
						continue;
					}
					if(x.startsWith("startTimestampAcquisition")) {
						if(x.split("=").length>1)
							this.startTimestampAcquisition = x.split("=")[1];
						System.out.println("startTimestampAcquisition: " + this.startTimestampAcquisition);
						continue;
					}
					if(x.startsWith("startTimestampMonitoring")) {
						if(x.split("=").length>1)
							this.startTimestampMonitoring = x.split("=")[1];
						System.out.println("startTimestampMonitoring: " + this.startTimestampMonitoring);
						continue;
					}
					if(x.startsWith("name")) {
						if(x.split("=").length>1)
							this.name = x.split("=")[1];
						System.out.println("name: " + this.name);
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
					this.name = "Pig " + this.pigId;
					System.out.println("Name: " + this.name);
				}
				System.out.println(this.additionalInfo);
				System.out.println();
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

	public String getPenId() {
		return penId;
	}

	public void setPenId(String penId) {
		this.penId = penId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getEndTimestampAcquisition() {
		return endTimestampAcquisition;
	}

	public void setEndTimestampAcquisition(String endTimestampAcquisition) {
		this.endTimestampAcquisition = endTimestampAcquisition;
	}

	public String getEndTimestampMonitoring() {
		return endTimestampMonitoring;
	}

	public void setEndTimestampMonitoring(String endTimestampMonitoring) {
		this.endTimestampMonitoring = endTimestampMonitoring;
	}

	public String getStartTimestampAcquisition() {
		return startTimestampAcquisition;
	}

	public void setStartTimestampAcquisition(String startTimestampAcquisition) {
		this.startTimestampAcquisition = startTimestampAcquisition;
	}

	public String getStartTimestampMonitoring() {
		return startTimestampMonitoring;
	}

	public void setStartTimestampMonitoring(String startTimestampMonitoring) {
		this.startTimestampMonitoring = startTimestampMonitoring;
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

	public String getPigId() {
		return pigId;
	}

	public void setPigId(String pigId) {
		this.pigId = pigId;
	}
	
	
}
