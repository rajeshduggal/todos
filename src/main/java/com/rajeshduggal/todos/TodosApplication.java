package com.rajeshduggal.todos;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@GetMapping
	String todos(Model model) {
		model.addAttribute("todos", this.todos);
		return "todos";
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
