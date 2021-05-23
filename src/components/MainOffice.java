package components;

import java.util.ArrayList;
import java.util.Random;
/**
 * Manages the entire system, operates a clock, branches and vehicles, 
 * creates packages and transfers them to the appropriate branches.
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class MainOffice {
	private static int clock;
	private Hub hub;
	private ArrayList<Package> packages;
	
	/**
	 * Constructs and initializes a MainOffice by values<br>
	 * Example: MainOffice(6,3)
	 * @param branches A list of all the branches in the system.
	 * @param trucksForBranch - A list of all the trucks in the system.
	 */
	public MainOffice(int branches, int trucksForBranch)
	{
		MainOffice.clock = 0;
		this.packages = new ArrayList<Package>();
		this.hub = new Hub();
		for(int i=0;i<trucksForBranch;i++)
			hub.addTruck(new StandardTruck());
		hub.addTruck(new NonStandardTruck());
		System.out.println();
		for(int i=0;i<branches;i++)
		{
			Branch newBranch = new Branch();
			for(int j=0;j<trucksForBranch;j++)
				newBranch.addTruck(new Van());
			hub.addBranch(newBranch);
			System.out.println();
		}
	}
	
	/**
	 * Receives a number of beats that the system will perform, 
	 * and executes the tick() function several times.
	 * @param playTime Number of beats.
	 */
	public void play(int playTime)
	{
		System.out.println("\n======================= START ========================\n");
		for(int i=0;i<playTime;i++)
			this.tick();
		System.out.println("\n======================= STOP ========================\n");
		printReport();
	}
	
	/**
	 * Prints a follow-up report for all packages in the system.
	 */
	public void printReport()
	{
		for(int i=0;i<this.packages.size();i++)
		{
			System.out.println("TRACKING " + this.packages.get(i));
			this.packages.get(i).printTracking();
			System.out.println();
		}
	}
	
	/**
	 * Get a string of the clock in this current moment.
	 * @return The value of the clock in MM: SS format.
	 */
	public String clockString()
	{
		if(clock<60)
			return String.format("%02d:%02d", 0, MainOffice.clock);
		return String.format("%02d:%02d", MainOffice.clock/60, (MainOffice.clock%60));
	}
	
	/**
	 * Activate one beat in the clock, in every beat the following actions are performed:
	 * <ul>
	 * <li> Printing the time and add 1 to the clock.</li>
	 * <li> All branches, sorting center and vehicles perform one work unit.</li>
	 * <li> Every 5 beats a random new package is created.</li>
	 * <li> At the end of the run, prints a message and all history for the created packages.</li>
	 * </ul>
	 */
	public void tick()
	{
		System.out.println(this.clockString());
		if(MainOffice.clock%5 == 0)
			this.addPackage();
		for(Truck t : hub.getListTrucks()) 	//work for each truck in hub
			t.work();
		this.hub.work();
		for(Branch b : hub.getBranches())		//work for each branch
		{
			for(Truck t : b.getListTrucks()) 	//work for each truck
				t.work();
			b.work();
		}
		MainOffice.clock++;
	}
	
	/**
	 * A package lottery (random type and values)<br>
	 * Creates it and associates it with the appropriate branch.
	 */
	public void addPackage()
	{
		Package newPack = createRandomPackage();
		this.packages.add(newPack);
		newPack.setStatus(Status.COLLECTION);
		if(newPack instanceof NonStandardPackage)
			hub.collectPackage(newPack);
		else
		{
			int zip = newPack.getSenderAddress().getZip();
			for(int i=0;i<hub.getBranches().size();i++)
				if(hub.getBranches().get(i).getBranchId() == zip)
					hub.getBranches().get(i).collectPackage(newPack);
		}
	}
	
	/**
	 * Get the current time
	 * @return The clock time
	 */
	public static int getClock() {
		return clock;
	}
	
	/**
	 * Get an access to the HUB
	 * @return HUB
	 */
	public Hub getHub() {
		return hub;
	}
	
	/**
	 * Get an access to the packages list
	 * @return Packages list
	 */
	public ArrayList<Package> getPackages() {
		return packages;
	}
	
	/**
	 * Change the time
	 * @param ck The new time
	 */
	public static void setClock(int ck) {
		clock = ck;
	}
	
	/**
	 * Change the hub
	 * @param hub new HUB
	 */
	public void setHub(Hub hub) {
		this.hub = hub;
	}
	
	/**
	 * Change the packages list
	 * @param packages new list of packages
	 */
	public void setPackages(ArrayList<Package> packages) {
		this.packages = packages;
	}
	
	/**
	 * Creates an address with random values
	 * @return Random address
	 */
	private Address createRandomAddress()
	{
		Random rand = new Random();
		return new Address(rand.nextInt(hub.numOfBranches()),rand.nextInt(900000)+100000);
	}
	
	/**
	 * Creates random priority
	 * @return Random priority
	 */
	private Priority getRandomPririty()
	{
		Random rand = new Random();
		return Priority.values()[rand.nextInt(Priority.values().length)];
	}
	
	/**
	 * Creates package (random type and values)
	 * @return Random package
	 */
	private Package createRandomPackage()
	{
		Random rand = new Random();
		Package newPack;
		switch(rand.nextInt(3)) 			//random number to choose package type
		{
		case 0:
			newPack = new StandardPackage(
					getRandomPririty(),		// generate priority
					createRandomAddress(),	// create random sender address
					createRandomAddress(),	// create random destination address
					rand.nextDouble()*9+1);	// generate weight
			break;
		case 1:
			newPack = new NonStandardPackage(
					getRandomPririty(),
					createRandomAddress(),	// create random sender address
					createRandomAddress(),	// create random destination address
					rand.nextInt(500)+1,	// create random width
					rand.nextInt(1000)+1,	// create random length
					rand.nextInt(400)+1);	// create random height
			break;	
		default:
			newPack = new SmallPackage(
					getRandomPririty(),		// generate priority
					createRandomAddress(),  // create random sender address
					createRandomAddress(),  // create random destination address
					rand.nextBoolean()); 	// package acknowledge
			break;
		}
		return newPack;
	}
	@Override
	public String toString() {
		return "MainOffice [hub=" + hub + ", packages=" + packages + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MainOffice))
			return false;
		MainOffice other = (MainOffice) obj;
		if (hub == null) {
			if (other.hub != null)
				return false;
		} else if (!hub.equals(other.hub))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		return true;
	}
}
