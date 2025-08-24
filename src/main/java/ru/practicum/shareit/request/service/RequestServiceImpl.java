package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItems;
import ru.practicum.shareit.request.dto.mapper.RequestMaper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public RequestDto createRequest(Long userId, RequestDto requestDto) {
        Request request = RequestMaper.mapToItemRequest(requestDto);
        request.setRequestor(UserMapper.mapToUser(userService.getUser(userId)));

        return RequestMaper.mapToItemRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestWithItems> getRequests(Long userId) {
        return requestRepository.findRequestsByUserId(userId).stream()
                .map(request -> {
                    List<ItemRequestDto> items = itemService.getItemsByRequestId(request.getId()).stream()
                            .map(ItemMapper::mapToItemRequestDto)
                            .toList();
                    return RequestMaper.mapToItemRequestWithItems(request, items);
                })
                .toList();
    }

    @Override
    public List<RequestDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(RequestMaper::mapToItemRequestDto)
                .toList();
    }

    @Override
    public RequestWithItems getRequestById(Long requestId) {
        var items = itemService.getItemsByRequestId(requestId).stream()
                .map(ItemMapper::mapToItemRequestDto)
                .toList();
        return RequestMaper.mapToItemRequestWithItems(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос отсутствует")), items);
    }
}
