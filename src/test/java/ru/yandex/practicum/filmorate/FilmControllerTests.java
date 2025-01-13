package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private FilmController filmController;

	@Test
	void contextLoads() {
		assertThat(filmController).isNotNull();
	}

	@Test
	void testCreateFilms() throws Exception {
		// корректный фильм
		Film film = new Film(1L, "film", "desc", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
			post("/films")
			.contentType("application/json")
			.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isOk());

		// корректный фильм с пограничной датой
		film = new Film(2L, "film", "desc", LocalDate.of(1895, 12, 28), 120);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isOk());

		// фильм с некорректной датой
		film = new Film(3L, "film", "desc2", LocalDate.of(1895, 12, 27), 120);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isBadRequest());

		// фильм с нулевой длительностью
		film = new Film(4L, "film", "desc", LocalDate.of(1895, 12, 28), 0);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isBadRequest());

		// фильм с отрицательной длительностью
		film = new Film(5L, "film", "desc", LocalDate.of(1895, 12, 28), -1);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isBadRequest());

		// фильм с описанием больше 200 символов
		film = new Film(6L, "film", "Lorem ipsum dolor sit amet consectetur adipiscing elit rutrum, per et hendrerit placerat pulvinar aliquet potenti metus, nullam pellentesque porta tempor lobortis volutpat ad. Lacus libero semper ince.", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isBadRequest());

		// фильм с пустым наименованием
		film = new Film(7L, "", "desc7", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isBadRequest());

		Collection<Film> films = filmController.getFilms();
		assertEquals(3, films.size());
	}

	@Test
	void testUpdateFilms() throws Exception {
		// корректный фильм
		Film film = new Film(1L, "film", "desc", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
				post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film))
		).andExpect(status().isOk());

		// фильм с пустым id
		Film newFilm = new Film(null, "film", "desc", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
				put("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(newFilm))
		).andExpect(status().isBadRequest());

		// фильм с несуществующим id
		newFilm = new Film(2L, "film", "desc", LocalDate.of(2000, 12, 28), 120);
		mockMvc.perform(
				put("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(newFilm))
		).andExpect(status().isNotFound());

		// корректной обновление
		newFilm = new Film(1L, "film_upd", "desc_upd", LocalDate.of(2001, 12, 28), 100);
		mockMvc.perform(
				put("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(newFilm))
		).andExpect(status().isOk());

		Collection<Film> films = filmController.getFilms();
		assertEquals(1, films.size());
		assertEquals(newFilm, films.iterator().next());
	}
}
