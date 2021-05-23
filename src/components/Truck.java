package components;
import java.util.ArrayList;
import java.util.Random;
/**
 * Represents a vehicle that used for delivering packages.
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public abstract class Truck implements Node{
	private static int truckSerialId = 2000;	//CONSISTENTS NUMBER FOR truckSerialId
	
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private boolean available;
	private int timeLeft;
	private ArrayList<Package> packages;
	
	//ADDITIONAL FIELDS
	private Branch belongTo;		//reference to which branch the Truck belong to
	
	/**
	 * Constructs and initializes a Truck by default<br>
	 * Random License plate and Model
	 */
	public Truck()
	{
		this.truckID = truckSerialId++;
		this.licensePlate = createLicensePlate();
		this.truckModel = createModel();
		this.available = true;
		this.timeLeft = 0; 
		this.packages = new ArrayList<Package>();
	}
	/**
	 * Constructs and initializes a Truck with values<br>
	 * Example: StandardTruck("123-45-678", "M3")
	 * @param licensePlate Vehicle ID number
	 * @param truckModel Vehicle model
	 */
	public Truck(String licensePlate, String truckModel)
	{
		this.truckID = truckSerialId++;
		this.licensePlate = new String(licensePlate);
		this.truckModel = new String(truckModel);
		this.available = true;
		this.timeLeft = 0;
		this.packages = new ArrayList<Package>();
	}

	/**
	 * Get the Truck ID
	 * @return truckID
	 */
	public int getTruckID() {
		return truckID;
	}
	/**
	 * Get the the license plate
	 * @return licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}
	/**
	 * Get the Truck model
	 * @return truckModel
	 */
	public String getTruckModel() {
		return truckModel;
	}
	/**
	 * Checking if the truck is available
	 * @return available
	 */
	public boolean isAvailable() {
		return available;
	}
	/**
	 * Get the time left to reach the destination
	 * @return timeLeft
	 */
	public int getTimeLeft() {
		return timeLeft;
	}
	/**
	 * Get access to the packages list
	 * @return packages 
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}
	/**
	 * Get access to the branch that the vehicle belong to
	 * @return The branch that the truck belong to
	 */
	public Branch getBelongTo() {
		return belongTo;
	}

	/**
	 * 
	 * @return current Trucks serialID starting number
	 */
	public static int getTruckSerialId() {
		return truckSerialId;
	}

	/**
	 * Change the Truck ID
	 * @param truckID New truck serial ID 
	 */
	public void setTruckID(int truckID) {
		this.truckID = truckID;
	}
	/**
	 * Change the license plate
	 * @param licensePlate New vehicle ID number
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	/**
	 * Change the truck model
	 * @param truckModel New vehicle model
	 */
	public void setTruckModel(String truckModel) {
		this.truckModel = truckModel;
	}
	/**
	 * Change the availability of the vehicle
	 * @param available is the vehicle available
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	/**
	 * Update the time left to arrive
	 * @param timeLeft New time left to arrive the destination
	 */
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	/**
	 * Change the packages list
	 * @param packages New Packages list
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}
	/**
	 * Change the branch that the vehicle belong to
	 * @param belongTo New branch that the vehicle belong to
	 */
	public void setBelongTo(Branch belongTo) {
		this.belongTo = belongTo;
	}
	
	/**
	 * Change current serial id starting number
	 * @param truckSerialId - int number
	 */
	public static void setTruckSerialId(int truckSerialId) {
		Truck.truckSerialId = truckSerialId;
	}
	
	@Override 
	public String toString() {
		return "truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel
				+ ", available=" + available;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Truck))
			return false;
		Truck other = (Truck) obj;
		if (available != other.available)
			return false;
		if (licensePlate == null) {
			if (other.licensePlate != null)
				return false;
		} else if (!licensePlate.equals(other.licensePlate))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		if (timeLeft != other.timeLeft)
			return false;
		if (truckID != other.truckID)
			return false;
		if (truckModel == null) {
			if (other.truckModel != null)
				return false;
		} else if (!truckModel.equals(other.truckModel))
			return false;
		return true;
	}

	/**
	 * Remove the package from the truck<br>
	 * Will print a massage if the package is not exists on packages list
	 */
	@Override
	public void deliverPackage(Package p) {
		if(this.getPackages().contains(p))
			this.getPackages().remove(p);
		else
			System.out.println("The van don't contain any packages");
	}
	
	//Additional methods// 
	/**
	 * Create a random Model<br>
	 * @return The model that was created
	 */
	private String createModel()
	{
		String model = "M";
		Random r = new Random();
		model += (char)(r.nextInt(5)+'0');
		return model;
	}
	
	/**
	 * Create a random LicensePlate
	 * @return the license plate that was created
	 */
	private String createLicensePlate()
	{
		String newLicensePlate = "";
		Random r = new Random();
		newLicensePlate += String.format("%d",r.nextInt(900)+100)+
				"-" +String.format("%d",r.nextInt(90)+10)+
				"-" + String.format("%d",r.nextInt(900)+100);
		return newLicensePlate;
	}
	
	/**
	 * Adding a package to the packages list of the vehicle<br>
	 * There are no duplicate packages on the list
	 * @param pack A package to add to the packages list
	 */
	public void addPackage(Package pack)
	{
		if(!(this.packages.contains(pack)))
			(this.packages).add(pack);
	}
}
