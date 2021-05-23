package components;
import java.util.ArrayList;
/**
 * Represents a local branch
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Branch implements Node {
	private static int serialBranchID = -1;	//CONSISTENTS NUMBER FOR branchId
	
	private int branchId;
	private String branchName;
	private ArrayList<Truck> listTrucks;
	private ArrayList<Package> listPackages;
	
	/**
	 * Constructs and initializes a NonStandardTruck by default
	 */
	public Branch()
	{
		this.branchId = serialBranchID++;
		this.branchName = "Branch " + String.format("%d", branchId);
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new ArrayList<Package>();
		System.out.println("Creating " + this.toString());
	}
	
	/**
	 * Constructs and initializes a NonStandardTruck with a value<br>
	 * Example: Branch("SCE")
	 * @param branchName - The name of the branch
	 */
	public Branch(String branchName) {
		this.branchName = new String(branchName);
		this.branchId = serialBranchID++;
		this.listTrucks = new ArrayList<Truck>();
		this.listPackages = new ArrayList<Package>();
		System.out.println("Creating " + this.toString());
	}
	
	@Override
	public String toString() {
		return "Branch " + branchId + ", branch name: " + branchName + ", packages: " 
				+ listPackages.size() + ", trucks: " + listTrucks.size();
	}
	
	/**
	 * Get the branch id
	 * @return branchId
	 */
	public int getBranchId() {
		return branchId;
	}
	
	/**
	 * Get the branch name
	 * @return branchName
	 */
	public String getBranchName() {
		return branchName;
	}
	
	/**
	 * Get an access to the trucks list
	 * @return getListTrucks
	 */
	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}
	
	/**
	 * Get an access to the packages list
	 * @return getListPackages
	 */
	public ArrayList<Package> getListPackages() {
		return listPackages;
	}
	
	/**
	 * Get the next serial number of branch id
	 * @return Current serialBranchID
	 */
	public static int getSerialBranchID() {
		return serialBranchID;
	}
	
	/**
	 * Change the branch id
	 * @param branchId The new branch id
	 */
	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	/**
	 * Change the branch name
	 * @param branchName The new branch name
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	/**
	 * Change the trucks list
	 * @param listTrucks The new trucks list
	 */
	public void setListTrucks(ArrayList<Truck> listTrucks) {
		this.listTrucks = listTrucks;
	}
	
	/**
	 * Change the packages list
	 * @param listPackages The new packages list
	 */
	public void setListPackages(ArrayList<Package> listPackages) {
		this.listPackages = listPackages;
	}
	
	/**
	 * Change the current serialID of branches
	 * @param serialBranchID int
	 */
	public static void setSerialBranchID(int serialBranchID) {
		Branch.serialBranchID = serialBranchID;
	}

	/**
	 * Collect the package to listPackages.
	 * @param p - The package we want to add to package list 
	 */
	@Override
	public void collectPackage(Package p) {
		if(!(this.listPackages.contains(p)))
			this.listPackages.add(p);	
	}
	
	/**
	 * Remove the package from listPackages.
	 * <br>If package not exist in listPackages print massage.
	 */
	@Override
	public void deliverPackage(Package p) {
		if(listPackages.contains(p))
			listPackages.remove(p);
		else
			System.out.println("This branch do not contains this package");
	}
	
	/**
	 * This function performs a work unit in each beat according to the following requirements:
	 * <ul>
	 * <li> For each package whose status is COLLECTION, if there is a vehicle available, he goes out to collect the package.</li>
	 * <li> For each package whose status is DELIVERY, if there is a vehicle available, he is sent to deliver the package.</li>
	 * </ul>
	 */
	@Override
	public void work() {
		ArrayList<Package> temp = new ArrayList<Package>(listPackages);
		for(Package p : temp)
		{//FOR ALL PACKAGES
			if(p.getStatus().equals(Status.COLLECTION))
			{	//Package is ready to collect
				Truck t = getAvailableTruck();
				if(t != null) 
				{//Got an available truck
					handlePackage(p, this, t, Status.COLLECTION);
					t.setTimeLeft(p.getSenderAddress().getStreet()%10 + 1);
					t.setAvailable(false);
					System.out.println(t.getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + t.getTimeLeft());
				}
			}
			else if(p.getStatus().equals(Status.DELIVERY))
			{	//Package is ready to deliver
				Truck t = getAvailableTruck();
				if(t != null) 
				{//Got an available truck
					handlePackage(p, this, t, Status.DISTRIBUTION);
					t.setTimeLeft(p.getDestinationAddress().getStreet()%10 + 1);
					t.setAvailable(false);
					System.out.println(t.getName() + " is delivering package " + p.getPackageID() + ", time left: " + t.getTimeLeft());
				}
			}	
		}
	}
	
	@Override
	public String getName()
	{
		return "Branch "+ this.branchId;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Branch))
			return false;
		Branch other = (Branch) obj;
		if (branchId != other.branchId)
			return false;
		if (branchName == null) {
			if (other.branchName != null)
				return false;
		} else if (!branchName.equals(other.branchName))
			return false;
		if (listPackages == null) {
			if (other.listPackages != null)
				return false;
		} else if (!listPackages.equals(other.listPackages))
			return false;
		if (listTrucks == null) {
			if (other.listTrucks != null)
				return false;
		} else if (!listTrucks.equals(other.listTrucks))
			return false;
		return true;
	}
	
	//ADDITIONAL
	/**
	 * Search for an available truck
	 * <br>If found return Truck, else return <b>null</b>
	 */
	private Truck getAvailableTruck()
	{
		for(Truck t : listTrucks)
			if(t.isAvailable())
				return t;
		return null;
	}
	
	/**
	 * Add truck to listTrucks and set BelongTo.
	 * <br>(If truck already exist on listTruck do nothing).
	 * @param truck A truck to add to the list
	 */
	public void addTruck(Truck truck)
	{
		if(!(this.listTrucks.contains(truck)))
		{
			this.listTrucks.add(truck);
			truck.setBelongTo(this);
		}
	}
}
