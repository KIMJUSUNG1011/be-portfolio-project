package com.jw.boardservice.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static com.jw.boardservice.comment.CommentDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest
{
    MockMvc mockMvc;
    StatusResultMatchers statusResultMatchers;

    ObjectMapper objectMapper;

    @Mock
    CommentService commentService;

    @InjectMocks
    CommentController commentController;

    @BeforeEach
    void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        statusResultMatchers = MockMvcResultMatchers.status();
        objectMapper = new ObjectMapper();
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
    void delete() throws Exception
    {
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
