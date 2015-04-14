package ge.edu.freeuni.sdp.todo.client;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.*;
import org.glassfish.jersey.jackson.JacksonFeature;

public class TaskServiceProxy {

	private final String uri;
	private final Client client;

	public TaskServiceProxy(String uri) {
		this.uri = uri;
		ClientConfig config = new ClientConfig().register(JacksonFeature.class);
		client = ClientBuilder.newClient(config);
	}
	
	public TaskDo[] getAll() {
		return client.
				target(uri)
				.request()
				.get(TaskDo[].class);
	}

	public TaskDo get(String id) {
		Response response = 
				client
					.target(uri + "/" + id)
					.request()
					.get();

		if (is404(response)) {
			return null;
		}
		
		return response.readEntity(TaskDo.class);
	}

	public String create(TaskDo task) {
		Response response = client 
				.target(uri)
				.request()
				.post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

		if (is201(response)) {
			String[] segments = response.getLocation().getPath().split("/");
			String id = segments[segments.length - 1];
			return id;
		}
		return null;
	}
	
	public TaskDo update(String id, TaskDo task) {
		Response response = client
				.target(uri + "/" + id)
				.request()
				.put(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

		if (is404(response)) {
			return null;
		}
		
		return response.readEntity(TaskDo.class);
	}
	
	public Boolean delete(String id) {
		Response response = client
				.target(uri + "/" + id)
				.request()
				.delete();
		return !is404(response);
	}
	
	private boolean is404(Response response) {
		return isStatus(response, Response.Status.NOT_FOUND);
	}
	
	private boolean is201(Response response) {
		return isStatus(response, Response.Status.CREATED);
	}
	
	private boolean isStatus(Response response, Status status ) {
		return response.getStatus() ==	status.getStatusCode();
	}
}