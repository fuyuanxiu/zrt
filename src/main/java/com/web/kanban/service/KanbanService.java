package com.web.kanban.service;

import com.app.base.data.ApiResponseResult;

import java.util.List;

import org.springframework.data.domain.PageRequest;

public interface KanbanService {

	ApiResponseResult getLineList() throws Exception;

	ApiResponseResult getSmtKanbanData(String lineNo) throws Exception;
}
