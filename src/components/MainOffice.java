package components;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTable;

import gui.Simulator;

/**
 * Manages the entire system, operates a clock, branches and vehicles, 
 * creates packages and transfers them to the appropriate branches.
 * @version 2.0, 8/5/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class MainOffice implements Runnable {
	
	private static int clock;
	private static Hub hub;
	private ArrayList<Package> packages;
	
	private int num_of_packs;
	private int pack_x_cord = 100;
	private int pack_spaces;
	final private int FRAME_SIZE = 600;
	final private int X_CORD = 15;
	private int spaces,exit_spaces;
	private boolean live = false;
	
	private ArrayList<Thread> allThreads;
	/**
	 * Constructs and initializes a MainOffice by values<br>
	 * Example: MainOffice(6,3)
	 * @param branches A list of all the branches in the system.
	 * @param trucksForBranch - A list of all the trucks in the system.
	 */
	public MainOffice(int branches, int trucksForBranch)
	{
		int current_cord;
		MainOffice.clock = 0;
		spaces = (FRAME_SIZE - 30*branches)/(branches+1);
		this.packages = new ArrayList<Package>();	// Save exit point from hub to each branch
		MainOffice.hub = new Hub();
		for(int i=0;i<trucksForBranch;i++)
			hub.addTruck(new StandardTruck());
		hub.addTruck(new NonStandardTruck());
		
		allThreads = new ArrayList<Thread>();

		System.out.println();
		
		current_cord = spaces+20;	// Start index of branch
		exit_spaces = 200 / (branches+1);
		int end_y = MainOffice.getHub().getY_cord() + exit_spaces;
		for(int i=0;i<branches;i++)
		{
			hub.getExitYPoints().add(end_y);
			Branch newBranch = new Branch();
			newBranch.setX_cord(X_CORD);
			newBranch.setY_cord(current_cord);
			for(int j=0;j<trucksForBranch;j++)
				newBranch.addTruck(new Van());
			hub.addBranch(newBranch);
			System.out.println();
			current_cord += 30+spaces;
			end_y += exit_spaces;
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
	 * Starts all the system Threads
	 */
	public void doStart()
	{
		for(Truck t : hub.getListTrucks()) 	
			allThreads.add(new Thread(t));		
		allThreads.add(new Thread(hub));		
		for(Branch b : hub.getBranches())		
		{
			for(Truck t : b.getListTrucks()) 	
				allThreads.add(new Thread(t));		
			allThreads.add(new Thread(b));					
		}
		for(Thread t : allThreads)
			t.start();
	}
	
	/**
	 * Prints a follow-up report for all packages in the system.
	 */
	public void printReport()
	{
		System.out.println("\n======================= STOP ========================\n");
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
		if(!Simulator.isSuspended())
		{
			System.out.println(this.clockString());
			if(MainOffice.clock%5 == 0 && packages.size()<num_of_packs)
				this.addPackage();		
		}
	}
	
	/**
	 * A package lottery (random type and values)<br>
	 * Creates it and associates it with the appropriate branch.
	 */
	public void addPackage()
	{
		pack_spaces = (1000 - num_of_packs*30)/(num_of_packs+1);
		if(packages.size()==0)
			pack_x_cord += pack_spaces;
		Package newPack = createRandomPackage();
		newPack.setX_cord(pack_x_cord);
		pack_x_cord+=30+pack_spaces;
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
	public static Hub getHub() {
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
	 * @param new_hub new HUB
	 */
	public static void setHub(Hub new_hub) {
		hub = new_hub;
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
			if (MainOffice.hub != null)
				return false;
		} else if (!hub.equals(MainOffice.hub))
			return false;
		if (packages == null) {
			if (other.packages != null)
				return false;
		} else if (!packages.equals(other.packages))
			return false;
		return true;
	}
	
	/**
	 * @return Number of packages on system
	 */
	public int getNum_of_packs() {
		return num_of_packs;
	}

	/**
	 * Sets the number of packages on the system
	 * @param num_of_packs Number of packages
	 */
	public void setNum_of_packs(int num_of_packs) {
		this.num_of_packs = num_of_packs;
	}

	@Override
	public void run() {
		while(live)
		{
			tick();
			Simulator.getSimuPanel().repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!Simulator.isSuspended())
				MainOffice.clock++;	
		}
	}
	
	/**
	 * Create all packages info table
	 * @return AllPackagesInfo table
	 */
	public JTable createAllPacksTable()
	{
		return createTabelByArray(this.getPackages());
	}
	
	/**
	 * Create a packages list of all packages that the specific branch working with
	 * @param id Branch id
	 * @return Packages list of packages that the specific branch working with
	 */
	public ArrayList<Package> getCurrentPacksByBranch(int id)
	{
		ArrayList<Package> temp = new ArrayList<Package>();
		for(Package p : this.getPackages())
		{	// Check if package zip is the id and the package is not NonStandardPackage
			if(p.getSenderAddress().getZip() == id && !(p instanceof NonStandardPackage))
				temp.add(p);
		}
		return temp;
	}
	
	/**
	 * Create packages list of all NonStandardPackages that the hub working with
	 * @return Packages list of NonStandardPackages that the hub working with
	 */
	public ArrayList<Package> getCurrentHubPacks()
	{
		ArrayList<Package> temp = new ArrayList<Package>();
		for(Package p : this.getPackages())
		{
			if(p instanceof NonStandardPackage)
				temp.add(p);
		}
		return temp;
	}
	
	/**
	 * Create JTable of packages list data
	 * @param arr list of packages we want to show on table
	 * @return JTable of packages
	 */
	public JTable createTabelByArray(ArrayList<Package> arr)
	{
		String [] columns = {"Package ID", "Sender", "Destination", "Prority", "Status"};
		String[][] data = new String[arr.size()][5];
		for(int i=0;i<arr.size();i++)
		{
			data[i][0] =  String.valueOf(arr.get(i).getPackageID());
			data[i][1] =  String.valueOf(arr.get(i).getSenderAddress().getZip()+1)
					+ "-" + String.valueOf(arr.get(i).getSenderAddress().getStreet()) ;
			data[i][2] =  String.valueOf(arr.get(i).getDestinationAddress().getZip()+1)
					+ "-" + String.valueOf(arr.get(i).getDestinationAddress().getStreet());
			data[i][3] =  String.valueOf(arr.get(i).getPriority());
			data[i][4] =  String.valueOf(arr.get(i).getStatus());
		}
		return new JTable(data,columns); 	
	}

	/**
	 * @return All threads
	 */
	public ArrayList<Thread> getAllThreads() {
		return allThreads;
	}

	/**
	 * Sets all treads
	 * @param allThreads All treads
	 */
	public void setAllThreads(ArrayList<Thread> allThreads) {
		this.allThreads = allThreads;
	}
	
	/**
	 * Check if thread is alive
	 * @return True if thread is alive
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * Sets true/false on isAlive value
	 * @param live The thread is alive
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
}
