package server02;

public class Main {

	public static void main(String[] args) {

		Server server = new Server(25526, 1);
		
		server.start();
	}

}
