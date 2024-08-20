package com.matdang.seatdang.common.exception_handler;

import com.matdang.seatdang.common.exception.ReservationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String handleReservationException(ReservationException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");  // 이전 페이지의 URL 가져오기
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:" + referer;  // 이전 페이지로 리다이렉트
    }
}
