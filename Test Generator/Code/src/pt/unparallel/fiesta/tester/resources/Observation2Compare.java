package pt.unparallel.fiesta.tester.resources;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;

import pt.unparallel.fiesta.tester.constants.Constants;

public class Observation2Compare {

	private class item {
		@SuppressWarnings("unused")
		private String value;
		public item(Double value) {

			NumberFormat nf = new DecimalFormat("0.#######################E0");
			this.value = Constants.VALUETEMPLATE.replace("&VALUE&", nf.format(value));
		}
	}

	private LinkedList<String> vars;
	private LinkedList<item> items;

	public Observation2Compare() {
		this.vars = new LinkedList<String>();
		this.items = new LinkedList<item>();

		this.vars.add("value");
	}

	public LinkedList<String> getVars() {
		return vars;
	}

	public void setVars(LinkedList<String> vars) {
		this.vars = vars;
	}

	public LinkedList<item> getItems() {
		return items;
	}

	public void addVar(String var) {
		this.addVar(var);
	}

	public void addItem(Double value) {
		this.items.add(new item(value));
	}
}
