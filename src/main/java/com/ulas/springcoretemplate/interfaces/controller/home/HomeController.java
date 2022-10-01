package com.ulas.springcoretemplate.interfaces.controller.home;

import com.ulas.springcoretemplate.interfaces.controller.common.BaseController;
import com.ulas.springcoretemplate.interfaces.service.home.HomeService;
import com.ulas.springcoretemplate.model.common.PageRequest;
import com.ulas.springcoretemplate.model.request.DocRequest;
import com.ulas.springcoretemplate.model.request.LanguageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Api(tags = "HomeController", value = "HomeController")
public interface HomeController extends BaseController<HomeService> {
    @GetMapping("/home/lang")
    @ApiOperation("this service provides get language")
    <Res> ResponseEntity<Res> getLanguage(@Valid @ModelAttribute LanguageRequest languageRequest);

    @GetMapping("/home/document")
    @ApiOperation("this service provides get legal documents just like kvkk")
    <Res> ResponseEntity<Res> getDoc(@Valid @ModelAttribute DocRequest docRequest);

    @GetMapping("/home/configs")
    @ApiOperation("this service provides get configs etc commission or fee")
    <Res> ResponseEntity<Res> getConfigs();

    @GetMapping("/home/getSeoXml")
    @ApiOperation("this service provides get all lounges and user's urls as xml format")
    <Res> ResponseEntity<Res> getSeoXml();

    @GetMapping("/home/getContact")
    <Res> ResponseEntity<Res> getContact();

    @GetMapping("/home/aws/health")
    ResponseEntity<String> checkServerHealth();

    @GetMapping("/home/getTestDummyData")
    <Res> ResponseEntity<Res> getTestDummyData(@Valid @ModelAttribute PageRequest pageRequest);

}
