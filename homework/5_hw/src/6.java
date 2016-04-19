import java.io.IOException;

import data_mining.p3.miners.Kmeans;

public class HW6Driver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Kmeans clusterer = new Kmeans();

		clusterer.load("in/hw6");
		//5323, 34634
		clusterer.setParameters(2, 23464);
		clusterer.cluster();

		clusterer.display("out/hw6");
	}

}
