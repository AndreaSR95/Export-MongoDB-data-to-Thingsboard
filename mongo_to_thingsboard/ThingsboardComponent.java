package mongo_to_thingsboard;

public interface ThingsboardComponent {
	public String getThingsboardId();
	public void setThingsboardId(String thingsboardId);
	public String getThingsboardType();
	public void setThingsboardType(String thingsboardType);
}
