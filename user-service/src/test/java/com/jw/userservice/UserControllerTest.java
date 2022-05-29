package com.jw.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.jw.userservice.UserDto.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest
{
    MockMvc mockMvc;
    StatusResultMatchers statusResultMatchers;

    ObjectMapper objectMapper;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    void setup()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        statusResultMatchers = MockMvcResultMatchers.status();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register() throws Exception
    {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto("dlrtls", "123", "kim", "01052927836");
        String content = objectMapper.writeValueAsString(requestDto);

        // mocking
        given(userService.register(any())).willReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    void update() throws Exception
    {
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto("123", "kim", "01052927836");
        String content = objectMapper.writeValueAsString(requestDto);
        Principal mockPrincipal = mock(Principal.class);

        // mocking
        given(userService.update(any(), any())).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/update")
                        .principal(mockPrincipal)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    }

    @Test
    void withdraw() throws Exception
    {
        Map<String, String> map = new HashMap<>();
        map.put("password", "123");
        String content = objectMapper.writeValueAsString(map);
        Principal mockPrincipal = mock(Principal.class);

        // mocking
        given(userService.withdraw(any(), any())).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/withdraw")
                        .principal(mockPrincipal)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}
