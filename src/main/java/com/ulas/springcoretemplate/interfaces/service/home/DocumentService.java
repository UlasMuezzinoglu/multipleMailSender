package com.ulas.springcoretemplate.interfaces.service.home;

import com.ulas.springcoretemplate.model.request.DocRequest;

public interface DocumentService {

    <Res> Res getDoc(DocRequest docRequest);

}
