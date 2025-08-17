package com.example.minierp.controller;

import com.example.minierp.dto.MaterialDto;
import com.example.minierp.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialViewController {

    private final MaterialService materialService;

    /**
     * 자재 목록 화면
     *
     * @return material/list.html
     */
    @GetMapping
    public String listPage() {
        return "material/list";
    }

    /**
     * 자재 등록 화면
     * - 신규 등록 모드
     *
     * @param model
     * @return material/form.html
     */
    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("material", new MaterialDto());
        model.addAttribute("mode", "create");
        return "material/form";
    }

    /**
     * 자재 수정 화면
     *
     * @param id 자재 ID
     * @param model
     * @return material/form.html
     */
    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        model.addAttribute("material", materialService.findById(id));
        model.addAttribute("mode", "edit");
        return "material/form";
    }

    /**
     * 자재 이력 화면
     *
     * @param id 자재 ID
     * @param model materialId 전달
     * @return material/history.html
     */
    @GetMapping("/{id}/history")
    public String historyPage(@PathVariable Long id, Model model) {
        model.addAttribute("materialId", id);
        return "material/history";
    }

    /**
     * 자재 입고 화면
     *
     * @param id 자재 ID
     * @param model materialId 전달
     * @return material/receive.html
     */
    @GetMapping("/{id}/receive")
    public String receivePage(@PathVariable Long id, Model model) {
        model.addAttribute("materialId", id);
        return "material/receive";
    }

    /**
     * 자재 출고 화면
     *
     * @param id 자재 ID
     * @param model materialId 전달
     * @return material/release.html
     */
    @GetMapping("/{id}/release")
    public String releasePage(@PathVariable Long id, Model model) {
        model.addAttribute("materialId", id);
        return "material/release";
    }
}
