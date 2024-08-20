package com.matdang.seatdang.common.exception_handler;

import com.matdang.seatdang.common.exception.ReservationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 전체 예외를 대상으로 하는 예외핸들러
     * - 구체적인 예외핸들러가 없다면, 이 핸들러를 사용한다.
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, Model model){
        log.info("@ControllerAdvice#exceptionHandler");
        log.error(e.getMessage(), e); // 예외로깅
        model.addAttribute("message", e.getMessage());
        return "customer/main";
    }

    @ExceptionHandler(ReservationException.class)
    public String handleReservationException(ReservationException e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("@ControllerAdvice#ReservationException");
        log.error(e.getMessage(), e); // 예외로깅
        String referer = request.getHeader("Referer");  // 이전 페이지의 URL 가져오기
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:" + referer;  // 이전 페이지로 리다이렉트
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(NoResourceFoundException e, Model model) {
        log.info("@ControllerAdvice#handleNotFound");
        log.error(e.getMessage(), e); // 예외로깅
        model.addAttribute("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");
        return "customer/error/404";  // 404 에러 페이지로 이동
    }
}
