package seu.ulms.controllers.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import seu.ulms.dto.book.CategoryDto;
import seu.ulms.services.book.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // إنشاء تصنيف جديد (فقط للأدمن)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    // جلب جميع التصنيفات
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // جلب تصنيف باستخدام ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // البحث عن تصنيف باستخدام العنوان (title)
    @GetMapping("/search")
    public ResponseEntity<CategoryDto> getCategoryByTitle(@RequestParam String title) {
        return ResponseEntity.ok(categoryService.getCategoryByTitle(title));
    }

    // البحث الجزئي (LIKE %title%)
    @GetMapping("/search-by-title")
    public ResponseEntity<List<CategoryDto>> searchCategoriesByTitle(@RequestParam String title) {
        return ResponseEntity.ok(categoryService.searchCategoriesByTitle(title));
    }

    // حذف تصنيف (فقط للأدمن)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // تحديث تصنيف (فقط للأدمن)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDetails) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDetails));
    }
}