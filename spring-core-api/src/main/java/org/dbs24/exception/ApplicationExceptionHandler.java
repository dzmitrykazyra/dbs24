/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.HandlerMapping;
//
//import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//https://stackoverflow.com/questions/50504918/custom-controlleradvice-in-spring-for-exception-handling
//https://medium.com/@jovannypcg/understanding-springs-controlleradvice-cd96a364033f
//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public abstract class ApplicationExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<StudentException> handleGenericException(Exception e){
//        StudentException studentException = new StudentException(100, "Students are not found");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(studentException);
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<StudentException> handleRunTimeException(RuntimeException e, HttpServletRequest request){
//        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//        StudentException studentException = new StudentException(101, String.format("Student with %s is not found", pathVariables.get("id")));
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(studentException);
//    }    
    

//    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public @ResponseBody
//    String elementNotFound(IncorrectResultSizeDataAccessException e) {
//        return "Could not find element! " + e.getMessage();
//    }
//
//    @ExceptionHandler(OptimisticLockingFailureException.class)
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public @ResponseBody
//    String elementNotFound(OptimisticLockingFailureException e) {
//        return "Conflict! " + e.getMessage();
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public @ResponseBody
//    List<ErrorMessage> badRequest(MethodArgumentNotValidException e) {
//        return e.getBindingResult().getAllErrors().stream()
//                .map(err -> new ErrorMessage(err))
//                .collect(Collectors.toList());
//
//    }    
    
//        @ExceptionHandler(CustomGenericException.class)
//    public ModelAndView handleCustomException(CustomGenericException ex) {
//
//        ModelAndView model = new ModelAndView("error/generic_error");
//        model.addObject("errCode", ex.getErrCode());
//        model.addObject("errMsg", ex.getErrMsg());
//
//        return model;
//
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleAllException(Exception ex) {
//
//        ModelAndView model = new ModelAndView("error/generic_error");
//        model.addObject("errMsg", "this is Exception.class");
//
//        return model;
//
//    }
}
