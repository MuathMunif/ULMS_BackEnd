package seu.ulms.services.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seu.ulms.dto.book.CategoryDto;
import seu.ulms.entities.book.CategoryEntity;
import seu.ulms.entities.universty.UniversityEntity;
import seu.ulms.mapper.book.CategoryMapper;
import seu.ulms.repositoies.book.CategoryRepository;
import seu.ulms.repositoies.universty.UniversityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UniversityRepository universityRepository;

    // إنشاء تصنيف جديد
    public CategoryDto createCategory(CategoryDto dto) {
        CategoryEntity category = categoryMapper.toEntity(dto);

        // تأكد من أن الجامعة موجودة
        UniversityEntity university = universityRepository.findById(dto.getUniversityId())
                .orElseThrow(() -> new RuntimeException(" University not found with ID: " + dto.getUniversityId()));

        category.setUniversity(university);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    // جلب جميع التصنيفات
    public List<CategoryDto> getAllCategories() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    // جلب تصنيف باستخدام ID
    public CategoryDto getCategoryById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" Category not found with ID: " + id));
        return categoryMapper.toDto(category);
    }

    // جلب تصنيف باستخدام الاسم
    public CategoryDto getCategoryByTitle(String title) {
        CategoryEntity category = categoryRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new RuntimeException(" Category not found with title: " + title));
        return categoryMapper.toDto(category);
    }

    // البحث الجزئي عن التصنيفات
    public List<CategoryDto> searchCategoriesByTitle(String title) {
        return categoryRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // حذف تصنيف
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException(" Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // تحديث تصنيف
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(" Category not found with ID: " + id));

        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(category));
    }
}
