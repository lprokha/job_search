package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
import kg.attractor.job_search.exception.BadRequestException;
import kg.attractor.job_search.exception.CategoryNotFoundException;
import kg.attractor.job_search.exception.ContactTypeNotFoundException;
import kg.attractor.job_search.exception.ResumeNotFoundException;
import kg.attractor.job_search.exception.UserNotFoundException;
import kg.attractor.job_search.model.Category;
import kg.attractor.job_search.model.ContactInfo;
import kg.attractor.job_search.model.ContactType;
import kg.attractor.job_search.model.EducationInfo;
import kg.attractor.job_search.model.Resume;
import kg.attractor.job_search.model.User;
import kg.attractor.job_search.model.WorkExperienceInfo;
import kg.attractor.job_search.repository.CategoryRepository;
import kg.attractor.job_search.repository.ContactInfoRepository;
import kg.attractor.job_search.repository.ContactTypeRepository;
import kg.attractor.job_search.repository.EducationInfoRepository;
import kg.attractor.job_search.repository.ResumeRepository;
import kg.attractor.job_search.repository.UserRepository;
import kg.attractor.job_search.repository.WorkExperienceInfoRepository;
import kg.attractor.job_search.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final EducationInfoRepository educationInfoRepository;
    private final WorkExperienceInfoRepository workExperienceInfoRepository;

    @Override
    public Resume create(CreateResumeDto dto, Integer applicantId) {
        log.info("Creating resume for applicantId={}", applicantId);

        LocalDateTime now = LocalDateTime.now();

        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> {
                    log.warn("User not found for resume creation, id={}", applicantId);
                    return new UserNotFoundException();
                });

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    log.warn("Category not found for resume creation, id={}", dto.getCategoryId());
                    return new CategoryNotFoundException();
                });

        Resume resume = Resume.builder()
                .applicant(applicant)
                .name(dto.getName())
                .category(category)
                .salary(dto.getSalary())
                .isActive(dto.getIsActive())
                .createdDate(now)
                .updateTime(now)
                .build();

        Resume savedResume = resumeRepository.save(resume);

        saveContactInfos(savedResume, dto.getContactInfos());
        saveEducationInfos(savedResume, dto.getEducationInfos());
        saveWorkExperienceInfos(savedResume, dto.getWorkExperienceInfos());

        return savedResume;
    }

    @Override
    public Optional<Resume> getById(Integer id) {
        return resumeRepository.findById(id);
    }

    @Override
    public List<Resume> getAll() {
        return resumeRepository.findAll();
    }

    @Override
    public List<Resume> getByCategory(Integer categoryId) {
        return resumeRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Resume> getByApplicantId(Integer applicantId) {
        return resumeRepository.findByApplicant_Id(applicantId);
    }

    @Override
    public Page<Resume> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return resumeRepository.findAll(pageable);
    }

    @Override
    public Page<Resume> getByApplicantId(Integer applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return resumeRepository.findByApplicant_Id(applicantId, pageable);
    }

    @Override
    public Page<Resume> getByCategory(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return resumeRepository.findByCategory_Id(categoryId, pageable);
    }

    @Override
    public List<Resume> getApplicantsByVacancyId(Integer vacancyId) {
        return resumeRepository.findApplicantsByVacancyId(vacancyId);
    }

    @Override
    public Optional<Resume> update(Integer id, UpdateResumeDto dto) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Resume not found for update, id={}", id);
                    return new ResumeNotFoundException();
                });

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    log.warn("Category not found, id={}", dto.getCategoryId());
                    return new CategoryNotFoundException();
                });

        resume.setName(dto.getName());
        resume.setCategory(category);
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        Resume updatedResume = resumeRepository.save(resume);

        contactInfoRepository.deleteByResumeId(id);
        educationInfoRepository.deleteByResumeId(id);
        workExperienceInfoRepository.deleteByResumeId(id);

        saveUpdateContactInfos(updatedResume, dto.getContactInfos());
        saveUpdateEducationInfos(updatedResume, dto.getEducationInfos());
        saveUpdateWorkExperienceInfos(updatedResume, dto.getWorkExperienceInfos());

        return Optional.of(updatedResume);
    }

    @Override
    public boolean delete(Integer id) {
        if (!resumeRepository.existsById(id)) {
            return false;
        }

        contactInfoRepository.deleteByResumeId(id);
        educationInfoRepository.deleteByResumeId(id);
        workExperienceInfoRepository.deleteByResumeId(id);
        resumeRepository.deleteById(id);
        return true;
    }

    private void saveContactInfos(Resume resume, List<CreateResumeDto.ContactDto> contactDtos) {
        if (contactDtos == null) {
            return;
        }

        for (CreateResumeDto.ContactDto dto : contactDtos) {
            if (dto.getTypeId() == null || dto.getContactValue() == null || dto.getContactValue().isBlank()) {
                continue;
            }

            saveContactInfo(resume, dto.getTypeId(), dto.getContactValue());
        }
    }

    private void saveUpdateContactInfos(Resume resume, List<UpdateResumeDto.ContactDto> contactDtos) {
        if (contactDtos == null) {
            return;
        }

        for (UpdateResumeDto.ContactDto dto : contactDtos) {
            if (dto.getTypeId() == null || dto.getContactValue() == null || dto.getContactValue().isBlank()) {
                continue;
            }

            saveContactInfo(resume, dto.getTypeId(), dto.getContactValue());
        }
    }

    private void saveContactInfo(Resume resume, Integer contactTypeId, String contactValue) {
        ContactType contactType = contactTypeRepository.findById(contactTypeId)
                .orElseThrow(() -> {
                    log.warn("Contact type not found, id={}", contactTypeId);
                    return new ContactTypeNotFoundException();
                });

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setResume(resume);
        contactInfo.setType(contactType);
        contactInfo.setContactValue(contactValue);

        contactInfoRepository.save(contactInfo);
    }

    private void saveEducationInfos(Resume resume, List<CreateResumeDto.EducationDto> educationDtos) {
        if (educationDtos == null) {
            return;
        }

        for (CreateResumeDto.EducationDto dto : educationDtos) {
            if (dto.getInstitution() == null || dto.getInstitution().isBlank()) {
                continue;
            }

            EducationInfo educationInfo = new EducationInfo();
            educationInfo.setResume(resume);
            educationInfo.setInstitution(dto.getInstitution());
            educationInfo.setProgram(dto.getProgram());
            educationInfo.setStartDate(parseDate(dto.getStartDate()));
            educationInfo.setEndDate(parseDate(dto.getEndDate()));
            educationInfo.setDegree(dto.getDegree());

            educationInfoRepository.save(educationInfo);
        }
    }

    private void saveUpdateEducationInfos(Resume resume, List<UpdateResumeDto.EducationDto> educationDtos) {
        if (educationDtos == null) {
            return;
        }

        for (UpdateResumeDto.EducationDto dto : educationDtos) {
            if (dto.getInstitution() == null || dto.getInstitution().isBlank()) {
                continue;
            }

            EducationInfo educationInfo = new EducationInfo();
            educationInfo.setResume(resume);
            educationInfo.setInstitution(dto.getInstitution());
            educationInfo.setProgram(dto.getProgram());
            educationInfo.setStartDate(parseDate(dto.getStartDate()));
            educationInfo.setEndDate(parseDate(dto.getEndDate()));
            educationInfo.setDegree(dto.getDegree());

            educationInfoRepository.save(educationInfo);
        }
    }

    private void saveWorkExperienceInfos(Resume resume, List<CreateResumeDto.WorkExperienceDto> workExperienceDtos) {
        if (workExperienceDtos == null) {
            return;
        }

        for (CreateResumeDto.WorkExperienceDto dto : workExperienceDtos) {
            if ((dto.getCompanyName() == null || dto.getCompanyName().isBlank())
                    && (dto.getPosition() == null || dto.getPosition().isBlank())) {
                continue;
            }

            WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
            workExperienceInfo.setResume(resume);
            workExperienceInfo.setYears(dto.getYears());
            workExperienceInfo.setCompanyName(dto.getCompanyName());
            workExperienceInfo.setPosition(dto.getPosition());
            workExperienceInfo.setResponsibilities(dto.getResponsibilities());

            workExperienceInfoRepository.save(workExperienceInfo);
        }
    }

    private void saveUpdateWorkExperienceInfos(Resume resume, List<UpdateResumeDto.WorkExperienceDto> workExperienceDtos) {
        if (workExperienceDtos == null) {
            return;
        }

        for (UpdateResumeDto.WorkExperienceDto dto : workExperienceDtos) {
            if ((dto.getCompanyName() == null || dto.getCompanyName().isBlank())
                    && (dto.getPosition() == null || dto.getPosition().isBlank())) {
                continue;
            }

            WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
            workExperienceInfo.setResume(resume);
            workExperienceInfo.setYears(dto.getYears());
            workExperienceInfo.setCompanyName(dto.getCompanyName());
            workExperienceInfo.setPosition(dto.getPosition());
            workExperienceInfo.setResponsibilities(dto.getResponsibilities());

            workExperienceInfoRepository.save(workExperienceInfo);
        }
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception e) {
            throw new BadRequestException("Некорректный формат даты");
        }
    }
}