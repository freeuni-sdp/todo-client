package ge.edu.freeuni.sdp.todo.client;

import java.util.*;

public class OptionList<T> {

	private final HashMap<String, T> map;

	public OptionList() {
		this.map = new HashMap<String, T>();
	}

	public OptionList<T> title(String title) {
		System.out.println(title);
		return this;
	}

	public OptionList<T> add(String key, String text, T value) {
		System.out.printf("%-10s [%s]: %s%n", " ", key, text);
		map.put(key.toUpperCase(), value);
		return this;
	}
	
	public OptionList<T> add(String text, T value) {
		return this.add(Integer.toString(size()+1), text, value);
	}

	public T read(Scanner scanner) {
		String input;
		while (true) {
			System.out.println();
			System.out.print("Please type command and press enter:");
			input = scanner.nextLine().toUpperCase();
			if (input != null && map.containsKey(input)) {
				return map.get(input);
			}
			System.out.printf("ERROR: [%S] is unknown.");
		}
	}
	
	protected int size() {
		return map.size();
	};
}
