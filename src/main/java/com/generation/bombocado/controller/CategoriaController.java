package com.generation.bombocado.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.bombocado.model.Categoria;
import com.generation.bombocado.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public List<Categoria> getAll() {
		return categoriaRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id) {

		return categoriaRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Categoria>> getByNome(@PathVariable String nome) {

		return ResponseEntity.ok(categoriaRepository.findAllByNomeContainingIgnoreCase(nome));

	}

	@PostMapping
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria) {

		return ResponseEntity.status(201).body(categoriaRepository.save(categoria));

	}

	@PutMapping
	public ResponseEntity<Categoria> put(@Valid @RequestBody Categoria categoria) {

		if (categoriaRepository.existsById(categoria.getId())) {

			return ResponseEntity.ok(categoriaRepository.save(categoria));

		}

		return ResponseEntity.notFound().build();

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {

		if (categoriaRepository.existsById(id)) {

			categoriaRepository.deleteById(id);

			return ResponseEntity.noContent().build();

		}

		return ResponseEntity.notFound().build();

	}

}
