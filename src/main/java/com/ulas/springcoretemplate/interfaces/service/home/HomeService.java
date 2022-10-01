package com.ulas.springcoretemplate.interfaces.service.home;

import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.DocRequest;
import com.ulas.springcoretemplate.model.request.LanguageRequest;

public interface HomeService {
    <Res> Res getLanguage(LanguageRequest languageRequest);

    <Res> Res getDoc(DocRequest docRequest);

    <Res> Res getConfigs();

    <Res> Res getSeoXml();

    <Res> Res getContact();

    <Res> Res getTestDummyData(PageRequest pageRequest);
}
