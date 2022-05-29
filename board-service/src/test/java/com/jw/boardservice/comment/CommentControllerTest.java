package com.jw.boardservice.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import static com.jw.boardservice.comment.CommentDto.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest
{
    MockMvc mockMvc;
    StatusResultMatchers statusResultMatchers;
    ObjectMapper objectMapper;

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentService commentService;

    @BeforeEach
    void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        statusResultMatchers = MockMvcResultMatchers.status();
        objectMapper = new ObjectMapper();
    }

    void write_댓글() throws Exception
    {
        // given
        Principal mockPrincipal = mock(Principal.class);
        CommentWriteRequestDto requestDto = new CommentWriteRequestDto("안녕하세요. 와리가리조쿠 김우석입니다.");
        String content = objectMapper.writeValueAsString(requestDto);

        // mocking
        given(commentService.write(any(), any(), any())).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/1/comment")
                        .principal(mockPrincipal)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    void write_대댓글() throws Exception
    {
        // given
        Principal mockPrincipal = mock(Principal.class);
        CommentWriteRequestDto requestDto = new CommentWriteRequestDto("네 저는 정상인 김주성입니다. 댓글 삭제해주세요");
        String content = objectMapper.writeValueAsString(requestDto);

        // mocking
        given(commentService.write(any(), any(), any(), any())).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/1/1/comment")
                        .principal(mockPrincipal)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void edit() throws Exception
    {
        // given
        Principal mockPrincipal = mock(Principal.class);
        CommentEditRequestDto editRequestDto = new CommentEditRequestDto("내용");
        String strRequestDto = objectMapper.writeValueAsString(editRequestDto);

        // when
        when(commentService.edit(eq(mockPrincipal.getName()), eq(1L), any())).thenReturn(true);
        when(commentService.edit(eq(mockPrincipal.getName()), eq(2L), any())).thenReturn(false);

        // then
        mockMvc.perform(MockMvcRequestBuilders.put("/{id}/comment", 1L)
                                                .principal(mockPrincipal)
                                                .content(strRequestDto)
                                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(statusResultMatchers.isCreated());

        mockMvc.perform(MockMvcRequestBuilders.put("/{id}/comment", 2L)
                                                .principal(mockPrincipal)
                                                .content(strRequestDto)
                                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(statusResultMatchers.isNotModified());
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete() throws Exception {
        // given
        Principal mockPrincipal = mock(Principal.class);

        // when
        when(commentService.delete(mockPrincipal.getName(), 1L)).thenReturn(true);
        when(commentService.delete(mockPrincipal.getName(), 2L)).thenReturn(false);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/{id}/comment", 1L)
                        .principal(mockPrincipal))
                .andExpect(statusResultMatchers.isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/{id}/comment", 2L)
                        .principal(mockPrincipal))
                .andExpect(statusResultMatchers.isNotFound());
    }
}
