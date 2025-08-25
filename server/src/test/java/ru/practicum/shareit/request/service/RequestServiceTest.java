package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestWithItems;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest() {
        Long userId = 1L;
        RequestDto requestDto = new RequestDto(null, "Описание", 1L, Timestamp.from(Instant.MIN));
        User user = new User(userId, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        UserDto userDto = UserMapper.mapToUserDto(user);
        Request savedRequest = new Request(1L, "Описание", user, null);

        when(userService.getUser(userId)).thenReturn(userDto);
        when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);

        RequestDto result = requestService.createRequest(userId, requestDto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.description()).isEqualTo("Описание");
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void getRequests() {
        Long userId = 1L;
        User user = new User(userId, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Request request = new Request(1L, "Описание", user, null);

        when(requestRepository.findRequestsByUserId(userId)).thenReturn(List.of(request));
        when(itemService.getItemsByRequestId(request.getId())).thenReturn(List.of());

        List<RequestWithItems> requests = requestService.getRequests(userId);

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).id()).isEqualTo(1L);
        assertThat(requests.get(0).items()).isEmpty();
    }

    @Test
    void getAllRequests() {
        User user = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Request request = new Request(1L, "Описание", user, null);
        when(requestRepository.findAll()).thenReturn(List.of(request));

        List<RequestDto> requests = requestService.getAllRequests();

        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).id()).isEqualTo(1L);
    }

    @Test
    void getRequestByIdFound() {
        Long requestId = 1L;
        User user = new User(1L, "Aleksandr", "Dolsa.broadstaff@gmail.com");
        Request request = new Request(requestId, "Описание", user, null);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemService.getItemsByRequestId(requestId)).thenReturn(List.of());

        RequestWithItems result = requestService.getRequestById(requestId);

        assertThat(result.id()).isEqualTo(requestId);
        assertThat(result.items()).isEmpty();
    }

    @Test
    void getRequestByIdNotFound() {
        Long requestId = 1L;
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> requestService.getRequestById(requestId));
    }
}
