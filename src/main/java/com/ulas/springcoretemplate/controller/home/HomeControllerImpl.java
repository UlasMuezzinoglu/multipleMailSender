package com.ulas.springcoretemplate.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.home.HomeController;
import com.ulas.springcoretemplate.interfaces.service.home.HomeService;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.DocRequest;
import com.ulas.springcoretemplate.model.request.LanguageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.ulas.springcoretemplate.constant.SuccessMessageConstants.SUCCESS;

@RestController
@RequiredArgsConstructor
public class HomeControllerImpl implements HomeController {

    private final HomeService homeService;

    @Override
    public HomeService getService() {
        return homeService;
    }

    @Override
    public <Res> ResponseEntity<Res> getLanguage(LanguageRequest languageRequest) {
        return ResponseEntity.ok(getService().getLanguage(languageRequest));
    }

    @Override
    public <Res> ResponseEntity<Res> getDoc(DocRequest docRequest) {
        return ResponseEntity.ok(getService().getDoc(docRequest));
    }

    @Override
    public <Res> ResponseEntity<Res> getConfigs() {
        return ResponseEntity.ok(getService().getConfigs());
    }

    @Override
    public <Res> ResponseEntity<Res> getSeoXml() {
        return ResponseEntity.ok(getService().getSeoXml());
    }

    @Override
    public <Res> ResponseEntity<Res> getContact() {
        return ResponseEntity.ok(getService().getContact());
    }

    @Override
    public ResponseEntity<String> checkServerHealth() {
        return ResponseEntity.ok(SUCCESS);
    }

    @Override
    public <Res> ResponseEntity<Res> getTestDummyData(PageRequest pageRequest) {
        return ResponseEntity.ok(getService().getTestDummyData(pageRequest));
    }
}
