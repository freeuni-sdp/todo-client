package ge.edu.freeuni.sdp.todo.client;

import java.util.Scanner;

public class App {

	private static TaskServiceProxy proxy;
	public static Scanner scanner;

	public static void main(String[] args) {

		scanner = new Scanner(System.in);

		String uri = new OptionList<String>()
				.title("Select service URI:")
				.add("HTTP: local , DB: in-memory",
						"http://localhost:8080/todo-core/webapi/memtodos")
				.add("HTTP: local , DB: cloud",
						"http://localhost:8080/todo-core/webapi/todos")
				.add("HTTP: cloud , DB: in-memory",
						"http://freeuni-sdp-todo.herokuapp.com/webapi/memtodos")
				.add("HTTP: cloud , DB: cloud",
						"http://freeuni-sdp-todo.herokuapp.com/webapi/todos")
				.read(scanner);

		proxy = new TaskServiceProxy(uri);

		while (true) {
			System.out.println("-------------------------------------------------------");
			int command = new OptionList<Integer>().title("Actions:")
					.add("C", "Create a new task", 1)
					.add("R", "List all tasks", 2).add("U", "Update a task", 3)
					.add("D", "Delete a task", 4).add("Q", "Quit the app", 0)
					.read(scanner);

			switch (command) {
			case 1:
				create();
				break;
			case 2:
				read();
				break;
			case 3:
				update();
				break;
			case 4:
				delete();
				break;
			case 0:
				return;
			}
		}
	}

	private static void delete() {
		System.out.println("-----------------DELETE--------------------------");
		String id = readTaskId();
		proxy.delete(id);
	}

	private static void update() {
		System.out.println("-----------------UPDATE--------------------------");
		String id = readTaskId();
		System.out.print("Enter new text:");
		TaskDo task = readTask();
		proxy.update(id, task);
	}

	private static void create() {
		System.out.println("-----------------CREATE--------------------------");
		System.out.print("Enter new task:");
		TaskDo task = readTask();
		proxy.create(task);
	}

	private static void read() {
		System.out.println("-----------------READ--------------------------");
		OptionList<String> options = new OptionList<String>();
		options.title("Tasks:");
		printTasks(options);
	}

	private static String readTaskId() {
		OptionList<String> options = new OptionList<String>();
		options.title("Select task:");
		return printTasks(options).read(scanner);
	}

	private static TaskDo readTask() {
		String text = scanner.nextLine();

		TaskDo task = new TaskDo();
		task.setText(text);
		return task;
	}

	private static OptionList<String> printTasks(OptionList<String> output) {
		for (TaskDo task : proxy.getAll()) {
			output.add(task.getText(), task.getId());
		}
		return output;
	}
}
