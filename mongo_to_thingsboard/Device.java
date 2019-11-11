package mongo_to_thingsboard;

public interface Device extends ThingsboardComponent{
	public String getThingsboardAccessToken();
	public void setThingsboardAccessToken(String thingsboardAccessToken);
}
