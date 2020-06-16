package intersky.function.entity;


import intersky.apputils.CharacterParser;

public class BusinessCardModel
{
	public String pingyin;
	public int type = 0;
	public boolean isselect = false;
	public int count = 0;
	public String address = "";
	public String audit = "";
	public String carduuid = "";
	public String cname = "";
	public String createtime = "";
	public String duty = "";
	public String email = "";
	public String fax = "";
	public String fields = "";
	public String flag = "";
	public String logo = "";
	public String mobile1 = "";
	public String mobile2 = "";
	public String name = "";
	public String tel1 = "";
	public String tel2 = "";
	public String updatetime = "";
	public String website = "";
	public String sex = "";
	public String department = "";

	public BusinessCardModel()
	{

	}
	
	public BusinessCardModel(String lname, int type)
	{
		this.name = lname;
		if(this.name != null)
		{
			this.pingyin=CharacterParser.getInstance().getSelling(this.name);
			this.pingyin=this.pingyin.toLowerCase();
		}
		else
		{
			this.pingyin = "";
		}
		this.type = type;
	}
	
	public BusinessCardModel(String lname)
	{
		this.name = lname;
		if(this.name != null)
		{
			this.pingyin= CharacterParser.getInstance().getSelling(this.name);
			this.pingyin=this.pingyin.toLowerCase();
		}
		else
		{
			this.pingyin = "";
		}
		this.type = 1;
	}
	
	public void setneame(String iname)
	{
		this.name = iname;
		this.pingyin=CharacterParser.getInstance().getSelling(this.name);
		this.pingyin=this.pingyin.toLowerCase();
	}
	
	public String getPingyin()
	{
		return pingyin;
	}

	public void setPingyin(String pingyin)
	{
		this.pingyin = pingyin;
	}

	public boolean isIsselect()
	{
		return isselect;
	}

	public void setIsselect(boolean isselect)
	{
		this.isselect = isselect;
	}
	
	
}
