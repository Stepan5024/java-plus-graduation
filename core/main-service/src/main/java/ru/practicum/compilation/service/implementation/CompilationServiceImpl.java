package ru.practicum.compilation.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.EventDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.dto.mapper.CompilationMapper;
import ru.practicum.compilation.dto.mapper.EventMapper;
import ru.practicum.compilation.exchange.EventFeignClient;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.Event;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventFeignClient eventFeignClient;
    private final EventMapper eventMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = compilationMapper.fromNewCompilationDto(compilationDto);
        List<Long> ids = compilationDto.getEvents();
        if (!CollectionUtils.isEmpty(ids)) {
            List<EventDto> eventDtos = eventFeignClient.findAllByIdIn(ids);
            // Преобразование EventDto в Event
            List<Event> events = eventDtos.stream()
                    .map(eventMapper::fromDto)
                    .toList();
            compilation.setEvents(events);
        } else {
            compilation.setEvents(Collections.emptyList());
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found"));
        if (!CollectionUtils.isEmpty(request.getEvents())) {
            // Получаем данные о событиях через FeignClient
            List<EventDto> eventDtos = eventFeignClient.findAllByIdIn(request.getEvents());

            // Преобразуем EventDto в Event с помощью EventMapper
            List<Event> events = eventDtos.stream()
                    .map(eventMapper::fromDto) // Используем EventMapper
                    .toList();

            compilation.setEvents(events);
        }

        if (request.getPinned() != null) compilation.setPinned(request.getPinned());
        if (request.getTitle() != null) compilation.setTitle(request.getTitle());
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found"));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        if (pinned == null) {
            return compilationMapper.toCompilationDtoList(compilationRepository.findAll(pageRequest).getContent());
        }

        if (pinned) {
            return compilationMapper.toCompilationDtoList(
                    compilationRepository.findAllByPinnedTrue(pageRequest).getContent());
        } else {
            return compilationMapper.toCompilationDtoList(
                    compilationRepository.findAllByPinnedFalse(pageRequest).getContent());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(long compId) {
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found")));
    }
}
