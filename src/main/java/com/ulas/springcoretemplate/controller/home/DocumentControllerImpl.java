package com.ulas.springcoretemplate.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.home.DocumentController;
import com.ulas.springcoretemplate.interfaces.service.home.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DocumentControllerImpl implements DocumentController {

    @Autowired
    private DocumentService documentService;

    @Override
    public DocumentService getService() {
        return documentService;
    }
}
