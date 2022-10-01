package com.ulas.springcoretemplate.interfaces.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.home.DocumentService;
import io.swagger.annotations.Api;

@Api(value = "DocumentController", tags = "DocumentController")
public interface DocumentController extends BaseController<DocumentService> {

}
