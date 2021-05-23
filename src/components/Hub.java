package components;
import java.util.ArrayList;
import java.util.Random;
/**
 * Represents a sorting center
 * @version 1.0, 9/4/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 * @see Branch
 */
public class Hub extends Branch {
	private ArrayList<Branch> branches;
	//ADDITIONAL
	private int deliverIndex;
	
	/**
	 * Constructs and initializes a Hub by default
	 */
	public Hub()
	{
		super("HUB");
		branches = new ArrayList<Branch>();
		deliverIndex=0;
	}


	
	@Override
	public String toString() {
		return super.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Hub))
			return false;
		Hub other = (Hub) obj;
		if (branches == null) {
			if (other.branches != null)
				return false;
		} else if (!branches.equals(other.branches))
			return false;
		if (deliverIndex != other.deliverIndex)
			return false;
		return true;
	}
	
	/**
	 * Get an access to the branches list
	 * @return The branches list
	 */
	public ArrayList<Branch> getBranches() {
		return branches;
	}
	
	/**
	 * Get the current index of branch deliver order
	 * @return the current index of the next branch destination
	 */
	public int getDeliverIndex() {
		return deliverIndex;
	}
	
	/**
	 * Change the branches list
	 * @param branches The new branches list
	 */
	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}
	
	/**
	 * Change the current deliver index
	 * @param deliverIndex int
	 */
	public void setDeliverIndex(int deliverIndex) {
		this.deliverIndex = deliverIndex;
	}
	/**
	 * This function performs a work unit in each beat according to the following requirements:
	 * <ul>
	 * <li> Sends all available trucks to local branches in the order of the branch numbers.</li>
	 * <li> The truck will load all the packages waiting for it to be transferred to the branch to which it is traveling.</li>
	 * <li> If the non-standard truck is available, and there is a non-standard package in the sorting center that is waiting to </li>
	 * </ul>
	 * be collected and its dimensions fit the truck, the truck will be sent to collect the package.
	 */
	@Override
	public void work() {
		for(Truck t : this.getListTrucks())
		{
			ArrayList<Package> temp = new ArrayList<Package>(this.getListPackages());		//clone to prevent problem with iteration
			if(t.isAvailable())
			{
				if(t instanceof StandardTruck)
				{
					System.out.println(t.getName() + " loaded packages at " + this.getName());
					((StandardTruck) t).setDestination(getBranchByZip(deliverIndex));
					for(Package p : temp)
					{
						if(!(p instanceof NonStandardPackage))
								if(p.getDestinationAddress().getZip()==deliverIndex  && ((StandardTruck) t).isCanFit(p))
									handlePackage(p, this, t, Status.BRANCH_TRANSPORT);						
					}
					t.setAvailable(false);
					t.setTimeLeft(new Random().nextInt(10)+1);
					System.out.println(t.getName() + " is on it's way to " +
							((StandardTruck)t).getDestination().getName() + ", time to arrive: " + t.getTimeLeft());
					deliverIndex++;
					if(deliverIndex == this.branches.size())
						deliverIndex = 0;
				}
				else if(t instanceof NonStandardTruck)
				{
					for(Package p : temp)
					{
						if(p instanceof NonStandardPackage && t.isAvailable())
							{
								if(p.getStatus().equals(Status.COLLECTION) &&
										((NonStandardTruck)t).isCanFit(p))
								{
									handlePackage(p, this, t, Status.COLLECTION);
									t.setAvailable(false);
									t.setTimeLeft(new Random().nextInt(10)+1);
									System.out.println(t.getName() + " is collecting package " + p.getPackageID() + ", time to arrive: " + t.getTimeLeft());
								}
							}
					}
				}
			}
		}	
	}
	
	@Override
	public String getName()
	{
		return "HUB";
	}
	
	/**
	 * Search for the right branch according to the zip
	 * @param zip The zip of the branch we want to find
	 */
	private Branch getBranchByZip(int zip)
	{
		for(Branch b : this.getBranches())
			if(b.getBranchId() == zip)
				return b;
		System.out.println("There is no branch with this id!");
		return null;
	}
	
	/**
	 * Get the number of branches
	 * @return Number of branches
	 */
	public int numOfBranches()
	{
		return this.branches.size();
	}
	
	/**
	 * If branch is not exist on branches, add him
	 * @param branch The branch we want to add
	 */
	public void addBranch(Branch branch)
	{
		if(!(branches.contains(branch)))
		{
			branches.add(branch);
		}
	}
}
