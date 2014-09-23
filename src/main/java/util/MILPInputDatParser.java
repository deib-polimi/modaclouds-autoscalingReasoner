 package util;

 
 /*
  * Class representing the data.dat files used by the Lavrentev algorithm. 
  * In this case the data.dat is parsed following it's own mean, 
  * in fact parameter like the number of tier or the number of VM are just indexes used without any names, 
  * while the remaining parameter are vectors or matrix on these indexes. 
  * So there is no reference to the explicit name of a tier or of a VM just like in the data.dat 
  * where this kind of parameter are just enumerated. 
  * The order of the parameter in the data.dat is strictly respected so if we get probabilityToBeInComponent[1][1]
  * we are refering to the class k1 and the component c1 in the data.dat;
  * so the references are just index based. 
  * Another solution with explicit reference to the name used in the data.dat 
  * will be implemented so that the references themselves will be more clear even if the result and the usability will be the same of this class,
  * since also in the data.dat this parameter are just indexes enumerated 
  * and there is no reference to the real name of a VM or of a tier.
  */
public class MILPInputDatParser {
	
	private int nTier;
	
	private int nProvider;
	
	private int nClass;
	
	private int nTypeVM;
	
	private int nTimeInt;
	
	private int nComponent;

	//da instanziare con [nClass][nComponent] e da settare di default interamente a 0.1
	private float [][] probabilityToBeInComponent;
	
	//da instanziare con [nTimeInt] e da settare di default interamente a 100.0
	private float [] arrRate;
	
	//da instanziare con [nClass][nComponent] e da settare di default interamente a 1.0
	private float [][] maximumSR;
	
	//da instanziare con [nComponent][nContainer] e da settare di default interamente a 1.0
	private float [][] componentAllocation;
	
	//da instanziare con [nVM][nProvider][nContainer] qe da settare di default interamente a 1.0
	private float [][][] speedVM;

	//da instanziare con [nVM][nProvider][nContainer] e da settare di default interamente a 0.06
	private float [][][] cost;
	
	//da instanziare con [nClass][nComponent] e da settare di default interamente a 0.04
	private float [][] maxResponseTime;
	
	private int maxVMPerContainer;
	
	//da instanziare con [nClass] e da settare di default interamente a 0.1
	private float[] alpha;
	
	public MILPInputDatParser(){
		
	}

	/**
	 * @return the nTier
	 */
	public int getnTier() {
		return nTier;
	}

	/**
	 * @param nTier the nTier to set
	 */
	public void setnTier(int nContainer) {
		this.nTier = nContainer;
	}

	/**
	 * @return the nProvider
	 */
	public int getnProvider() {
		return nProvider;
	}

	/**
	 * @param nProvider the nProvider to set
	 */
	public void setnProvider(int nProvider) {
		this.nProvider = nProvider;
	}

	/**
	 * @return the nClass
	 */
	public int getnClass() {
		return nClass;
	}

	/**
	 * @param nClass the nClass to set
	 */
	public void setnClass(int nClass) {
		this.nClass = nClass;
	}

	/**
	 * @return the nTypeVM
	 */
	public int getnTypeVM() {
		return nTypeVM;
	}

	/**
	 * @param nVM the nTypeVM to set
	 */
	public void setnTypeVM(int nVM) {
		this.nTypeVM = nVM;
	}

	/**
	 * @return the nTimeInt
	 */
	public int getnTimeInt() {
		return nTimeInt;
	}

	/**
	 * @param nTimeInt the nTimeInt to set
	 */
	public void setnTimeInt(int nTimeInt) {
		this.nTimeInt = nTimeInt;
	}

	/**
	 * @return the nComponent
	 */
	public int getnComponent() {
		return nComponent;
	}

	/**
	 * @param nComponent the nComponent to set
	 */
	public void setnComponent(int nComponent) {
		this.nComponent = nComponent;
	}

	/**
	 * @return the probabilityToBeInComponent
	 */
	public float[][] getProbabilityToBeInComponent() {
		return probabilityToBeInComponent;
	}

	/**
	 * @param probabilityToBeInComponent the probabilityToBeInComponent to set
	 */
	public void setProbabilityToBeInComponent(float[][] probabilityToBeInComponent) {
		this.probabilityToBeInComponent = probabilityToBeInComponent;
	}

	/**
	 * @return the arrRate
	 */
	public float[] getArrRate() {
		return arrRate;
	}

	/**
	 * @param arrRate the arrRate to set
	 */
	public void setArrRate(float[] arrRate) {
		this.arrRate = arrRate;
	}

	/**
	 * @return the maximumSR
	 */
	public float[][] getMaximumSR() {
		return maximumSR;
	}

	/**
	 * @param maximumSR the maximumSR to set
	 */
	public void setMaximumSR(float[][] maximumSR) {
		this.maximumSR = maximumSR;
	}

	/**
	 * @return the componentAllocation
	 */
	public float[][] getComponentAllocation() {
		return componentAllocation;
	}

	/**
	 * @param componentAllocation the componentAllocation to set
	 */
	public void setComponentAllocation(float[][] allocation) {
		this.componentAllocation=allocation;

	}

	/**
	 * @return the speedVM
	 */
	public float[][][] getSpeedVM() {
		return speedVM;
	}

	/**
	 * @param speedVM the speedVM to set
	 */
	public void setSpeedVM(float[][][] speedVM) {
		this.speedVM = speedVM;
	}

	/**
	 * @return the cost
	 */
	public float[][][] getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(float[][][] cost) {
		this.cost = cost;
	}

	/**
	 * @return the maxResponseTime
	 */
	public float[][] getMaxResponseTime() {
		return maxResponseTime;
	}

	/**
	 * @param maxResponseTime the maxResponseTime to set
	 */
	public void setMaxResponseTime(float[][] maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	/**
	 * @return the maxVMPerContainer
	 */
	public int getMaxVMPerContainer() {
		return maxVMPerContainer;
	}

	/**
	 * @param maxVMPerContainer the maxVMPerContainer to set
	 */
	public void setMaxVMPerContainer(int maxVMPerContainer) {
		this.maxVMPerContainer = maxVMPerContainer;
	}

	/**
	 * @return the alpha
	 */
	public float[] getAlpha() {
		return alpha;
	}

	/**
	 * @param alpha the alpha to set
	 */
	public void setAlpha(float[] alpha) {
		this.alpha = alpha;
	}
}
