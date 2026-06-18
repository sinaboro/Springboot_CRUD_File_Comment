package com.example.jpasecurity.controller;

import com.example.jpasecurity.dto.RegisterDto;
import com.example.jpasecurity.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // 로그인 화면 — SecurityConfig의 loginPage("/auth/login")와 매핑됨
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    // 회원가입 화면
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "auth/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        memberService.register(dto);

        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");

        return "redirect:/auth/login";
    }
}