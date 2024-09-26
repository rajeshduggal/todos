package com.rajeshduggal.todos;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;

@SpringBootApplication
public class TodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}

}

@Controller
@RequestMapping("/todos")
class TodoController {
	private final Set<Todo> todos = new ConcurrentSkipListSet<>(Comparator.comparingInt(Todo::id));

	TodoController() {
		for (var t : "read a book, go to the gym, have a nap".split(","))
			this.todos.add(Todos.todo(t));
	}

	@GetMapping
	String todos(Model model) {
		model.addAttribute("todos", this.todos);
		return "todos";
	}

	@PostMapping
	HtmxResponse add(@RequestParam("new-todo") String newTodo,
			Model model) {
		this.todos.add(Todos.todo(newTodo));
		model.addAttribute("todos", this.todos);
		return HtmxResponse
				.builder()
				.view("todos :: todos")
				.view("todos :: todos-form")
				.build();
	}

	@ResponseBody
	@DeleteMapping(produces = MediaType.TEXT_HTML_VALUE, path = "/{todoId}")
	String delete(@PathVariable Integer todoId) {
		this.todos
				.stream()
				.filter(t -> t.id().equals(todoId))
				.forEach(this.todos::remove);
		return "";
	}
}

record Todo(Integer id, String title) {
}

class Todos {
	private static final AtomicInteger id = new AtomicInteger(0);

	static Todo todo(String title) {
		return new Todo(id.incrementAndGet(), title);
	}
}
