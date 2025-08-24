package com.launcehub.controller;

import com.launcehub.Model.Projects;
import com.launcehub.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping("/projects")
    public List<Projects> testProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Server is running!";
    }

    @GetMapping("/mappings")
    public Map<RequestMappingInfo, HandlerMethod> getMappings() {
        return requestMappingHandlerMapping.getHandlerMethods();
    }
}
