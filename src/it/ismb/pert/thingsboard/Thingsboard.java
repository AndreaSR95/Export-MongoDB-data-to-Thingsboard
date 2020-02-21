package it.ismb.pert.thingsboard;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;


public class Thingsboard {

	String username;
	String password;
	String thingsboardUrl;
	
	public Thingsboard(String userName, String password, String thingsboardUrl)
	{
		this.username = userName;
		this.password = password;
		this.thingsboardUrl = thingsboardUrl;
	}
	private String token = null;
	
	public void generateToken() {
		
		
		String inputString = "{\"username\":\""+this.username+"\", \"password\":\""+this.password+"\"}";
		String result = makeHttpRequest(this.thingsboardUrl+"/api/auth/login", inputString);
		if(result != null && result.startsWith("{\"status\":40")) {
			token=null;
			return;
		}
		token = result.split(",")[0].replace("{\"token\":", "").replace("\"","");
	}
	
	public String getToken() {
		return token;
	}
	
	public void addAssets(HashSet<ThingsboardComponent> setOfAssets) {
		if(this.getToken() == null) {
			//Thingsboard.generateToken("bianchimarco79@yahoo.it",<InserirePassword>);
			return;
		}
		
		setOfAssets.forEach(asset->{
			if(asset instanceof Company) {
				Company a = (Company)asset;
				String thingsboardid = addThingsboardElement(a.getName(), "company", "asset");
				System.out.println(thingsboardid);
				a.setThingsboardId(thingsboardid);
				return;
			}
			if(asset instanceof Farm) {
				Farm a = (Farm)asset;
				String thingsboardid = addThingsboardElement(a.getName(), "farm", "asset");
				System.out.println(thingsboardid);
				a.setThingsboardId(thingsboardid);
				return;
			}
		});
	}
	
	private String addThingsboardElement(String name, String type, String assetOrDevice ) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(this.thingsboardUrl+"/api/"+assetOrDevice).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "Accept", "application/json" );
			connection.setRequestProperty( "X-Authorization", "Bearer " + token );
			

			String minusD = "{\"name\": \""+ name +"\", \"type\": \"" + type + "\"}";
			connection.getOutputStream().write(minusD.getBytes());
			connection.getOutputStream().close();
		
			connection.getInputStream();
	        StringBuilder responseSB = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        
	        String line;
		    while ((line = br.readLine()) != null)
	          responseSB.append(line);
		    br.close();
	       
	        System.out.println(responseSB.toString());
	        return extractThingsboardId(responseSB.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String extractThingsboardId(String line) {
		if(line.startsWith("{\"status\":4")) {
			return null;
		}
		return line.split(",")[1].split(":")[1].replaceAll("[}\"]", "");
	}
	
	public HashSet<ThingsboardComponent> relate(HashSet<ThingsboardComponent> fathers, HashSet<ThingsboardComponent> children) {
		if(this.getToken() == null) {
			//Thingsboard.generateToken("bianchimarco79@yahoo.it",<InserirePassword>);
			return null;
		}
		
		String IdInFather=null, IdInChild=null;
		Iterator<ThingsboardComponent> iter = fathers.iterator();
		if(!iter.hasNext())
			return null;
		ThingsboardComponent oneFather = iter.next();
		
		//QUESTO SARA' L'HASH-SET DI RITORNO. E' LA ROBA CHE NON RI E' RIUSCITA A RELAZIONARE
		HashSet<ThingsboardComponent> notRelatedThings = new HashSet<ThingsboardComponent>();
		
		//FOR SUI CHILD, FOR SUI FATHER, SE HANNO ID CORRISPONDENTE LI COLLEGO SU THINGSBOARD
		for(ThingsboardComponent child : children) {
			IdInChild = getIdInChild(child, oneFather);
			for(ThingsboardComponent father: fathers) {
				IdInFather = getIdInFather(father);
				if(IdInFather==null || IdInChild==null) {
					System.out.println("Can't relate, missing components");
					notRelatedThings.add(child);
					break;
				}
				if(IdInChild.equals(IdInFather)) {
					addRelationship(father,child);
					break;
				}
			}
		}
		return notRelatedThings;
	}

	private String getIdInFather(ThingsboardComponent father) {
		if(father instanceof Company) {
			return ((Company)father).getCompanyId();
		}
		if(father instanceof Farm) {
			return ((Farm)father).getFarmId();
		}
		if(father instanceof Building) {
			return ((Building)father).getBuildingId();
		}
		if(father instanceof Compartment) {
			return ((Compartment)father).getCompartmentId();
		}
		if(father instanceof Pen) {
			return ((Pen)father).getPenId();
		}
		return null;
	}

	private String getIdInChild(ThingsboardComponent child, ThingsboardComponent father) {
		if(father instanceof Company) {
			if(child instanceof Farm) {
				return ((Farm)child).getCompanyId();
			}
			if(child instanceof Building) {
				return ((Building)child).getCompanyId();
			}
			if(child instanceof Compartment) {
				return ((Compartment)child).getCompanyId();
			}
			if(child instanceof Pen) {
				return ((Pen)child).getCompanyId();
			}
			if(child instanceof Pig) {
				return ((Pig)child).getCompanyId();
			}
		}
		if(father instanceof Farm) {
			if(child instanceof Building) {
				return ((Building)child).getFarmId();
			}
			if(child instanceof Compartment) {
				return ((Compartment)child).getFarmId();
			}
			if(child instanceof Pen) {
				return ((Pen)child).getFarmId();
			}
			if(child instanceof Pig) {
				return ((Pig)child).getFarmId();
			}
		}
		if(father instanceof Building) {
			if(child instanceof Compartment) {
				return ((Compartment)child).getBuildingId();
			}
			if(child instanceof Pen) {
				return ((Pen)child).getBuildingId();
			}
			if(child instanceof Pig) {
				return ((Pig)child).getBuildingId();
			}
		}
		if(father instanceof Compartment) {
			if(child instanceof Pen) {
				return ((Pen)child).getCompartmentId();
			}
			if(child instanceof Pig) {
				return ((Pig)child).getCompartmentId();
			}
		}
		if(father instanceof Pen) {
			if(child instanceof Pig) {
				return ((Pig)child).getPenId();
			}
		}
		return null;
	}

	private void addRelationship(ThingsboardComponent father, ThingsboardComponent child) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(this.thingsboardUrl+"/api/relation").openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "Accept", "application/json" );
			connection.setRequestProperty( "X-Authorization", "Bearer " + token );
			

			String minusD = "{\"from\":{\"entityType\": \""+father.getThingsboardType()+"\",\"id\": \""+father.getThingsboardId()+"\"},\"to\": {\"entityType\": \""+child.getThingsboardType()+"\",\"id\": \""+child.getThingsboardId()+"\"}, \"type\": \"Contains\"}";
			connection.getOutputStream().write(minusD.getBytes());
			connection.getOutputStream().close();
		
			connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addDevices(HashSet<ThingsboardComponent> setOfDevices) {
		if(this.getToken() == null)
			return;
		
		setOfDevices.forEach(device->{
			if(device instanceof Building) {
				Building building = (Building)device;
				String thingsboardIdBuilding = addThingsboardElement(building.getName(), "building", "device");
				System.out.println("thingsboardID: " + thingsboardIdBuilding);
				building.setThingsboardId(thingsboardIdBuilding);
				building.setThingsboardAccessToken(retriveThingsboardAccessToken((Device)device));
				if(building.getThingsboardId() != null) {
					addTelemetryDataBuilding(building);
				}
			}
			if(device instanceof Compartment) {
				Compartment compartment = (Compartment)device;
				String thingsboardIdCompartment = addThingsboardElement(compartment.getName(), "compartment", "device");
				System.out.println("thingsboardID: " + thingsboardIdCompartment);
				compartment.setThingsboardId(thingsboardIdCompartment);
				compartment.setThingsboardAccessToken(retriveThingsboardAccessToken((Device)device));
				if(compartment.getThingsboardId() != null) {
					addTelemetryDataCompartment(compartment);
				}
			}
			if(device instanceof Pen) {
				Pen pen = (Pen)device;
				String thingsboardIdPen = addThingsboardElement(pen.getName(), "pen", "device");
				System.out.println("thingsboardID: " + thingsboardIdPen);
				pen.setThingsboardId(thingsboardIdPen);
				pen.setThingsboardAccessToken(retriveThingsboardAccessToken((Device)device));
				if(pen.getThingsboardId() != null) {
					addTelemetryDataPen(pen);
				}
			}
			if(device instanceof Pig) {
				Pig pig = (Pig)device;
				String thingsboardIdPig = addThingsboardElement(pig.getName(), "pig", "device");
				System.out.println("thingsboardID: " + thingsboardIdPig);
				pig.setThingsboardId(thingsboardIdPig);
				pig.setThingsboardAccessToken(retriveThingsboardAccessToken((Device)device));
				if(pig.getThingsboardId() != null) {
					addTelemetryDataPig(pig);
				}
			}
		});
	}
	
	private String retriveThingsboardAccessToken(Device device) {
		HttpURLConnection connection;
		if(device.getThingsboardAccessToken() == null) {
			try {
				connection = (HttpURLConnection) new URL(this.thingsboardUrl+"/api/device/"+device.getThingsboardId()+"/credentials").openConnection();
				connection.setDoOutput(true); // Triggers POST.
				connection.setRequestMethod("GET");
				connection.setRequestProperty( "Content-Type", "application/json" );
				connection.setRequestProperty( "Accept", "application/json" );
				connection.setRequestProperty( "X-Authorization", "Bearer " + token );
				
				connection.getInputStream();
				
		        StringBuilder responseSB = new StringBuilder();
		        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		       
		        String line;
			    while ((line = br.readLine()) != null)
		          responseSB.append(line);
			    br.close();
		       
		        return responseSB.toString().split(",")[5].replace("\"", "").split(":")[1];
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private void addTelemetryDataBuilding(Building building) {
//		POSSIBILI CAMPI DATI PER UN BUILDING: 
//		currentTemperature; currentHumidity; currentLuminosity; currentCO2; LastUpdateTimestamp;
		
		if(building.getCurrentTemperature() != null) {
			setTelemetryValue(building, "currentTemperature", building.getCurrentTemperature(), building.getThingsboardId());
		}
		if(building.getCurrentHumidity() != null) {
			setTelemetryValue(building, "currentHumidity", building.getCurrentHumidity(), building.getThingsboardId());
		}
		if(building.getCurrentLuminosity() != null) {
			setTelemetryValue(building, "currentLuminosity", building.getCurrentLuminosity(), building.getThingsboardId());
		}
		if(building.getCurrentCO2() != null) {
			setTelemetryValue(building, "currentCO2", building.getCurrentCO2(), building.getThingsboardId());
		}
		if(building.getLastUpdateTimestamp() != null) {
			setTelemetryValue(building, "lastUpdateTimestamp", building.getLastUpdateTimestamp(), building.getThingsboardId());
		}
	}

	private void addTelemetryDataCompartment(Compartment compartment) {
		if(compartment.getAdditionalInfo()!= null) {
			for( String str : compartment.getAdditionalInfo().split(" ")) {
				if(str.split("=").length>1) {
					this.setTelemetryValue(compartment, str.split("=")[0], str.split("=")[1], compartment.getThingsboardId());
				}
			}
		}
	}
	
	private void addTelemetryDataPen(Pen pen) {
		if(pen.getAdditionalInfo()!= null) {
			for( String str : pen.getAdditionalInfo().split(" ")) {
				if(str.split("=").length>1) {
					setTelemetryValue(pen, str.split("=")[0], str.split("=")[1], pen.getThingsboardId());
				}
			}
		}
	}
	
	private void addTelemetryDataPig(Pig pig) {
//		sex; additionalInfo; endTimestampAcquisition; endTimestampMonitoring; startTimestampAcquisition; startTimestampMonitoring;
		if(pig.getAdditionalInfo()!= null) {
			for( String str : pig.getAdditionalInfo().split(" ")) {
				if(str.split("=").length>1) {
					setTelemetryValue(pig, str.split("=")[0], str.split("=")[1], pig.getThingsboardId());
				}
			}
		}
		if(pig.getEndTimestampAcquisition() != null) {
			setTelemetryValue(pig, "endTimestampAcquisition", pig.getEndTimestampAcquisition(), pig.getThingsboardId());
		}
		if(pig.getEndTimestampMonitoring() != null) {
			setTelemetryValue(pig, "endTimestampMonitoring", pig.getEndTimestampMonitoring(), pig.getThingsboardId());
		}
		if(pig.getStartTimestampAcquisition() != null) {
			setTelemetryValue(pig, "startTimestampAcquisition", pig.getStartTimestampAcquisition(), pig.getThingsboardId());
		}
		if(pig.getStartTimestampMonitoring() != null) {
			setTelemetryValue(pig, "startTimestampMonitoring", pig.getStartTimestampMonitoring(), pig.getThingsboardId());
		}
	}

	private void setTelemetryValue(Device device, String dataName, String value, String thingsboardId) {
		//SE NON LO ABBIAMO, OTTENIAMO L'ACCESS TOKEN DEL DEVICE, CHE CI SERVE PER INSERIRE UN DATO NEL DISPOSITIVO
		HttpURLConnection connection;
//		if(device.getThingsboardAccessToken() == null) {
//			try {
//				connection = (HttpURLConnection) new URL("http://localhost:8080/api/device/"+thingsboardId+"/credentials").openConnection();
//				connection.setDoOutput(true); // Triggers POST.
//				connection.setRequestMethod("GET");
//				connection.setRequestProperty( "Content-Type", "application/json" );
//				connection.setRequestProperty( "Accept", "application/json" );
//				connection.setRequestProperty( "X-Authorization", "Bearer " + token );
//				
//				connection.getInputStream();
//				
//		        StringBuilder responseSB = new StringBuilder();
//		        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		       
//		        String line;
//			    while ((line = br.readLine()) != null)
//		          responseSB.append(line);
//			    br.close();
//		       
//		        device.setThingsboardAccessToken(responseSB.toString().split(",")[5].replace("\"", "").split(":")[1]);
//			} catch (IOException e) {
//				e.printStackTrace();
//				return;
//			}
//		}
		//ORA POSSIAMO INSERIRE IL DATO NEL BUILDING
		try {
			connection = (HttpURLConnection) new URL(this.thingsboardUrl+"/api/v1/"+device.getThingsboardAccessToken()+"/telemetry").openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-Type", "application/json" );
			
			String minusD = "{\""+dataName+"\":\""+value+"\"}";
			connection.getOutputStream().write(minusD.getBytes());
			connection.getOutputStream().close();
			
			connection.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveThingsboardId(String path, String type, HashSet<ThingsboardComponent> thingsboardElements) {
        try(FileWriter fw = new FileWriter(path, true)) { 
        	String id;
            for(ThingsboardComponent element : thingsboardElements) {
            	id = getId(element);
            	if(element instanceof Device)
                	fw.write(type + ",ThingsboardId:" + element.getThingsboardId() + ",ID:" + id + ",DEVICE_TOKEN:" + ((Device)element).getThingsboardAccessToken() + System.lineSeparator());
            	else
            		fw.write(type + ",ThingsboardId:" + element.getThingsboardId() + ",ID:" + id +System.lineSeparator());
            }
            
            System.out.println("File writing done.");
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	private String getId(ThingsboardComponent element) {
		if(element instanceof Company) {
				return ((Company)element).getCompanyId();
		}
		if(element instanceof Farm) {
			return ((Farm)element).getFarmId();
		}
		if(element instanceof Building) {
			return ((Building)element).getBuildingId();
		}
		if(element instanceof Compartment) {
			return ((Compartment)element).getCompartmentId();
		}
		if(element instanceof Pen) {
			return ((Pen)element).getPenId();
		}
		if(element instanceof Pig) {
			return ((Pig)element).getPigId();
		}
		return null;
	}

	public void deleteAllElements(String path) {
		String line = null;
		try (BufferedReader buffer = Files.newBufferedReader(Paths.get(path))){;
			while((line = buffer.readLine()) != null){
				if(line.split(" ")[0].equals("asset")) {
					this.delete(line.split(" ")[1], "asset");
				}
				else {
					if(line.split(" ")[0].equals("device")) {
						this.delete(line.split(" ")[1], "device");
					}
				}
			}
			buffer.close();
			FileWriter fileWriter = new FileWriter(path);
			fileWriter.write("");
			fileWriter.close();
		}
        catch (Exception e) {
        	e.printStackTrace();
        }
	}

	public void delete(String thingsboardId, String type) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(this.thingsboardUrl+"/api/"+type+"/"+thingsboardId).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestMethod("DELETE");
			connection.setRequestProperty( "Content-Type", "application/json" );
			connection.setRequestProperty( "Accept", "application/json" );
			connection.setRequestProperty( "X-Authorization", "Bearer " + token );
		
			connection.getInputStream();
	        StringBuilder responseSB = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        
	        if(br.readLine() == null)
	            System.out.println("Cancellazione: Ok");
	        br.close();
	       
	        System.out.println(responseSB);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	

	public String makeHttpRequest(String url, String inputString) {
		String result = "";
		try
		{
			// URLConnection connection = new URL(url).openConnection();
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			// connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-Type", "application/json;");
			int length = inputString.length();
			connection.setRequestProperty("Content-Length", String.valueOf(length));
	
			// Write data
			OutputStream os = connection.getOutputStream();
			os.write(inputString.getBytes());
	
			// Read response
			StringBuilder responseSB = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
			String line;
			while ((line = br.readLine()) != null)
				responseSB.append(line);
			result = responseSB.toString();
			// Close streams
			br.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
