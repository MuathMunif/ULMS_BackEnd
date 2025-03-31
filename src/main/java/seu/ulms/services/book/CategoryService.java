package seu.ulms.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.entities.book.CategoryEntity;
import seu.ulms.repositoies.book.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // إنشاء تصنيف جديد
    public CategoryEntity createCategory(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    // جلب جميع التصنيفات
    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }

    // جلب تصنيف باستخدام ID
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Category not found with ID: " + id));
    }

    // جلب تصنيف باستخدام الاسم (title)
    public CategoryEntity getCategoryByTitle(String title) {
        return categoryRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new RuntimeException("❌ Category not found with title: " + title));
    }

    // البحث الجزئي عن تصنيفات بالاسم
    public List<CategoryEntity> searchCategoriesByTitle(String title) {
        return categoryRepository.findByTitleContainingIgnoreCase(title);
    }

    // حذف تصنيف
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("❌ Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // تحديث تصنيف
    public CategoryEntity updateCategory(Long id, CategoryEntity categoryDetails) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Category not found with ID: " + id));

        category.setTitle(categoryDetails.getTitle());
        return categoryRepository.save(category);
    }
}
