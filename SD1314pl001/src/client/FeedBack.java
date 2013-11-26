package client;


public class FeedBack
{
    private final boolean success;
    private final String description;

    public FeedBack(boolean success, String description)
    {
	this.success = success;
	this.description = description;
    }

    public boolean isSuccess()
    {
	return success;
    }

    public String getDescription()
    {
	return description;
    }
    
    
    
}
