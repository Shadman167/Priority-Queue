
import java.util.ArrayList;
import java.util.HashMap;


public class ERPriorityQueue{

	public ArrayList<Patient>  patients;
	public HashMap<String,Integer>  nameToIndex;

	public ERPriorityQueue(){

		//  use a dummy node so that indexing starts at 1, not 0

		patients = new ArrayList<Patient>();
		patients.add( new Patient("dummy", 0.0) );

		nameToIndex  = new HashMap<String,Integer>();
	}

	private int parent(int i){
		return i/2;
	}

	private int leftChild(int i){
	    return 2*i;
	}

	private int rightChild(int i){
	    return 2*i+1;
	}

    /*
    TODO: OPTIONAL
    TODO: Additional helper methods such as isLeaf(int i), isEmpty(), swap(int i, int j) could be useful for this assignment
     */


	public boolean isLeaf(int i){

		return rightChild(i) > (patients.size()) || leftChild(i) > patients.size();
	}

	public boolean isEmpty(){

		return patients.isEmpty();
	}


	public void swap(int i, int j){

		this.nameToIndex.put(this.patients.get(j).getName(), i);
		this.nameToIndex.put(this.patients.get(i).getName(), j);

		Patient tmp2 = this.patients.get(i);
		patients.set(i, this.patients.get(j));
		patients.set(j, tmp2);

		//patients.set()








		//Collections.swap(patients, i, j);
	}

	public void upHeap(int i){

		while (rightChild(i) > 1 && patients.get(i).getPriority() < patients.get(i/2).getPriority()){

			swap(i, i/2);
			i = i/2;
		}



	}

	public void downHeap(int i){




		while (leftChild(i) < this.patients.size()) {

			int child = leftChild(i);

			if (child < this.patients.size()-1) {

				if (patients.get(child + 1).getPriority() < patients.get(child).getPriority()) {

					child = rightChild(i);

				}

			}

			if (patients.get(child).getPriority() < patients.get(i).getPriority()) {

				swap(i, child);
				i = child;
			} else {
				return;
			}
		}







	}

	public boolean contains(String name){
        // TODO: Implement your code here & remove return statement
        return this.nameToIndex.containsKey(name);
	}

	public double getPriority(String name){
        // TODO: Implement your code here & remove return statement
		return this.patients.get(nameToIndex.get(name)).getPriority();
	}

	public double getMinPriority(){
        // TODO: Implement your code here & remove return statement

		if (patients.size() == 1){

			return -1;
		}

		return this.patients.get(1).getPriority();
	}

	public String removeMin(){
        // TODO: Implement your code here & remove return statement

		if (patients.size() <= 1){

			return null;
		}

		else if (patients.size() == 2){

			Patient cur1 = this.patients.remove(1);
			this.nameToIndex.remove(cur1.getName());
			return cur1.getName();
		}

		else {

			Patient cur2 = this.patients.get(1);

			this.nameToIndex.put(this.patients.get(patients.size()-1).getName(), 1);

			this.patients.set(1, this.patients.remove(patients.size() - 1));

			downHeap(1);

			this.nameToIndex.remove(cur2.getName());


			return cur2.getName();
		}


	}

	public String peekMin(){
        // TODO: Implement your code here & remove return statement

		if (patients.size() == 1){

			return null;
		}

		return this.patients.get(1).getName();
	}

	/*
	 * There are two add methods.  The first assumes a specific priority.
	 * The second gives a default priority of Double.POSITIVE_INFINITY
	 *
	 * If the name is already there, then return false.
	 */

	public boolean add(String name, double priority){
        // TODO: Implement your code here & remove return statement


		if (contains(name)){

			return false;
		}

		this.patients.add(new Patient(name, priority));

		this.nameToIndex.put(name, this.patients.size()-1);

		upHeap(this.patients.size()-1);

        return true;
	}

	public boolean add(String name){
        // TODO: Implement your code here
		if (contains(name)){

			return false;
		}

		return add(name, Double.POSITIVE_INFINITY);
	}

	public boolean remove(String name){
        // TODO: Implement your code here


		if (!contains(name)){

			return false;
		}

		else if(this.patients.size() <= 1){

			return false;
		}

		else if(this.patients.size() == 2){

			if (!contains(name)){

				return false;
			}

			this.patients.remove(1);
			this.nameToIndex.remove(name);
			return true;
		}


		int get_index = this.nameToIndex.get(name);

		if (get_index == this.patients.size()-1){

			this.patients.remove(get_index);
			this.nameToIndex.remove(name);
			return true;
		}


		Patient patient1 = this.patients.get(this.patients.size()-1);

		this.patients.set(get_index, patient1);

		this.patients.remove(this.patients.size()-1);
		this.nameToIndex.remove(name);

		if (this.patients.get(1).getPriority() > this.patients.get(get_index).getPriority()){

			upHeap(get_index);
		}

		else{




			if (get_index+1 < this.patients.size() && this.patients.get(get_index-1).getPriority() < this.patients.get(get_index).getPriority() && this.patients.get(get_index+1).getPriority() < this.patients.get(get_index).getPriority()){

				downHeap(get_index);
			}
		}

		return true;


	}

	/*
	 *   If new priority is different from the current priority then change the priority
	 *   (and possibly modify the heap).
	 *   If the name is not there, return false
	 */

	public boolean changePriority(String name, double priority){
        // TODO: Implement your code here & remove return statement


		if (!contains(name)){

			return false;
		}

		else if(this.patients.size() <= 1){

			return false;
		}

		int index = this.nameToIndex.get(name);

		Patient get_patient = this.patients.get(index);

		get_patient.setPriority(priority);

		this.patients.set(index, get_patient);

		if (this.patients.get(1).getPriority() > this.patients.get(index).getPriority()){

			upHeap(index);
		}

		else{




			if (index+1 < this.patients.size() && this.patients.get(index-1).getPriority() < this.patients.get(index).getPriority() && this.patients.get(index+1).getPriority() < this.patients.get(index).getPriority()){

				downHeap(index);
			}
		}
        return true;
	}

	public ArrayList<Patient> removeUrgentPatients(double threshold){
        // TODO: Implement your code here & remove return statement

		if (this.patients.size() <= 1){

			return null;
		}

		ArrayList<Patient> max_prior = new ArrayList<>();

		for (int i = 1; i < this.patients.size(); i++){

			if (this.patients.get(i).getPriority() <= threshold){

				String get_name2 = this.patients.get(i).getName();

				max_prior.add(this.patients.remove(i));
				this.nameToIndex.remove(get_name2);

				if (!(get_name2).equals("dummy")) {
					this.nameToIndex.put(this.patients.get(patients.size() - 1).getName(), i);
				}
				i = i/2;
				//this.patients.remove(i);
				//this.nameToIndex.remove(get_name2);
				//this.nameToIndex.put(this.patients.get(patients.size()-1).getName(), i);

			}

		}
        return max_prior;
	}

	public ArrayList<Patient> removeNonUrgentPatients(double threshold){
        // TODO: Implement your code here & remove return statement

		if (this.patients.size() <= 1){

			return null;
		}


		ArrayList<Patient> min_prior = new ArrayList<>();

		for (int i = 1; i < this.patients.size(); i++) {

			if (this.patients.get(i).getPriority() >= threshold) {

				String get_name3 = this.patients.get(i).getName();

				min_prior.add(this.patients.remove(i));
				this.nameToIndex.remove(get_name3);

				this.nameToIndex.put(this.patients.get(patients.size() - 1).getName(), i);


				//this.nameToIndex.put(this.patients.get(patients.size() - 1).getName(), i);
				i = i / 2;
			}
		}

		if (this.nameToIndex.size() >= 2) {


			for (int j = 1; j <= this.nameToIndex.size(); j++) {

				if (j == 0) {

					continue;
				}

				this.nameToIndex.put(this.patients.get(j).getName(), j);
			}
		}



		if (this.nameToIndex.size() <= 1){

			if (this.nameToIndex.containsKey("dummy")){

				this.nameToIndex.remove("dummy");
			}
		}



        return min_prior;
	}



	static class Patient{
		private String name;
		private double priority;

		Patient(String name,  double priority){
			this.name = name;
			this.priority = priority;
		}

		Patient(Patient otherPatient){
			this.name = otherPatient.name;
			this.priority = otherPatient.priority;
		}

		double getPriority() {
			return this.priority;
		}

		void setPriority(double priority) {
			this.priority = priority;
		}

		String getName() {
			return this.name;
		}

		@Override
		public String toString(){
			return this.name + " - " + this.priority;
		}

		public boolean equals(Object obj){
			if (!(obj instanceof  ERPriorityQueue.Patient)) return false;
			Patient otherPatient = (Patient) obj;
			return this.name.equals(otherPatient.name) && this.priority == otherPatient.priority;
		}

	}
}
