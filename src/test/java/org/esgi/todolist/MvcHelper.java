package org.esgi.todolist;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MvcHelper {
    private final MockMvc mvc;
    private final String basePath;

    public MvcHelper(Object controller, String basePath) {
        this.mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        this.basePath = basePath;
    }

    public ResultActions invokePostMethod(String path, String body) throws Exception {
        return mvc.perform(post("http://<base_url>/" + basePath + "/" + path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public ResultActions invokeGetMethod(String path) throws Exception {
        return mvc.perform(get("http://<base_url>/" + basePath + "/" + path)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public ResultActions invokePutMethod(String path, String body) throws Exception {
        return mvc.perform(put("http://<base_url>/" + basePath + "/" + path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    public ResultActions invokeDeleteMethod(String path, String body) throws Exception {
        return mvc.perform(delete("http://<base_url>/" + basePath + "/" + path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
