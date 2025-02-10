package ru.practicum.core.event.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.core.api.dto.compilation.CompilationDto;
import ru.practicum.core.api.dto.compilation.NewCompilationDto;
import ru.practicum.core.api.dto.compilation.UpdateCompilationRequestDto;
import ru.practicum.core.event.service.CompilationService;

@Slf4j
@RestController
@RequestMapping(AdminCompilationController.ADMIN_COMPILATIONS_PATH)
@RequiredArgsConstructor
public class AdminCompilationController {
    public static final String ADMIN_COMPILATIONS_PATH = "/admin/compilations";
    public static final String ID_PATH = "/{id}";

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto dto) {
        log.info("==> POST. Adding new Compilation: {}", dto);
        return compilationService.createCompilation(dto);
    }

    @DeleteMapping(ID_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long id) {
        log.info("==> DELETE. Deleting Compilation: {}", id);
        compilationService.delete(id);
    }

    @PatchMapping(ID_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable long id, @Valid @RequestBody UpdateCompilationRequestDto compilationDto) {
        log.info("==> PATCH. Updating Compilation: {}", compilationDto);
        return compilationService.updateCompilation(id, compilationDto);
    }
}