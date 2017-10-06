package sub.adw.web;

public class Environment {

	public String getVariable(String name) {
		return System.getenv(name);
	}

}
