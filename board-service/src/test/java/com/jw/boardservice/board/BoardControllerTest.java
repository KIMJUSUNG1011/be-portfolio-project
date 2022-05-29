package com.jw.boardservice.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.jw.boardservice.board.BoardDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class BoardControllerTest
{
    MockMvc mockMvc;
    StatusResultMatchers statusResultMatchers;

    ObjectMapper objectMapper;

//    @Autowired
//    WebApplicationContext context;

    @Mock
    BoardService boardService;

    @InjectMocks
    BoardController boardController;

    @BeforeEach
    void setup()
    {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
        statusResultMatchers = MockMvcResultMatchers.status();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("글 조회 테스트")
    void write() throws Exception
    {
        // given
        Principal mockPrincipal = mock(Principal.class);
        BoardWriteRequestDto writeRequestDto = new BoardWriteRequestDto("제목", "내용");
        String strRequestDto = objectMapper.writeValueAsString(writeRequestDto);
        MockMultipartFile file1 = new MockMultipartFile("files", "filename.jpg", "image/jpg", "<<file>>".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "filename.jpeg", "image/jpeg", "<<file>>".getBytes());
        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "json", "application/json", strRequestDto.getBytes());

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/write")
                .file(file1)
                .file(file2)
                .file(requestDto)
                .principal(mockPrincipal))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(statusResultMatchers.isCreated());
    }

    @Test
    @DisplayName("글 수정 테스트")
    void edit() throws Exception
    {
        // given
        Principal mockPrincipal = mock(Principal.class);
        BoardEditRequestDto editRequestDto = new BoardEditRequestDto("제목", "내용");
        String strRequestDto = objectMapper.writeValueAsString(editRequestDto);

        // mocking
        when(boardService.edit(any(), eq(1L), any())).thenReturn(Boolean.TRUE);

        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 1L)
                .principal(mockPrincipal)
                .content(strRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(statusResultMatchers.isCreated());

        mockMvc.perform(MockMvcRequestBuilders.put("/{id}", 2L)
                .principal(mockPrincipal)
                .content(strRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(statusResultMatchers.isNotFound());
    }

    @Test
    @DisplayName("글 삭제 테스트")
    void delete() throws Exception
    {
        // given
        // mocking

        // when
        when(boardService.delete(any(), eq(1L))).thenReturn(Boolean.TRUE);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", 1L))
                .andExpect(statusResultMatchers.isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/{id}", 2L))
                .andExpect(statusResultMatchers.isNotFound());
    }

    @Test
    @DisplayName("글 조회 테스트")
    void read() throws Exception
    {
        // given
        // mocking
        Cookie cookieWithView = new Cookie("latestView", "1");
        Cookie cookieWithoutView = new Cookie("latestView", "1");
        BoardReadResponseDto responseDto = new BoardReadResponseDto(1L, "제목", "내용", "이메일", 0, LocalDateTime.now(), null);

        // when
        when(boardService.read(any(), eq(1L))).thenReturn(responseDto);
        when(boardService.read(any(), eq(2L))).thenReturn(null);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
                                                .cookie(cookieWithView))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(statusResultMatchers.isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L)
                                                .cookie(new Cookie("latest", "1")))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(statusResultMatchers.isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 2L)
                                                .cookie(cookieWithoutView))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(statusResultMatchers.isNotFound());
    }

    @Test
    @DisplayName("글 목록 테스트")
    void list() throws Exception
    {
        // given
        // mocking
        List<BoardListResponseDto> responseDtos = new ArrayList<>();
        BoardListResponseDto responseDto1 = new BoardListResponseDto(1L, "제목1", "이메일", 0, LocalDateTime.now());
        BoardListResponseDto responseDto2 = new BoardListResponseDto(2L, "제목2", "이메일", 0, LocalDateTime.now());
        responseDtos.add(responseDto1);
        responseDtos.add(responseDto2);

        // when
        // then
        when(boardService.list()).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(statusResultMatchers.isNotFound());

        when(boardService.list()).thenReturn(responseDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(statusResultMatchers.isOk());
    }
}
