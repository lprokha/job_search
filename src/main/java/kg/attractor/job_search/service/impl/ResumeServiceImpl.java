package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.CreateResumeDto;
import kg.attractor.job_search.dto.UpdateResumeDto;
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

        User applicant = userRepository.findById(applicantId).orElse(null);
        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

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

        saveOrUpdateAdditionalInfo(savedResume, dto);

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
        Optional<Resume> existingResume = resumeRepository.findById(id);
        if (existingResume.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);

        Resume resume = existingResume.get();
        resume.setName(dto.getName());
        resume.setCategory(category);
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        resume.setUpdateTime(LocalDateTime.now());

        Resume updatedResume = resumeRepository.save(resume);

        saveOrUpdateAdditionalInfo(updatedResume, dto);

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

    private void saveOrUpdateAdditionalInfo(Resume resume, CreateResumeDto dto) {
        saveOrUpdateContactInfo(resume, dto.getContactTypeId(), dto.getContactValue());
        saveOrUpdateEducationInfo(resume, dto.getInstitution(), dto.getProgram(), dto.getStartDate(), dto.getEndDate(), dto.getDegree());
        saveOrUpdateWorkExperienceInfo(resume, dto.getYears(), dto.getCompanyName(), dto.getPosition(), dto.getResponsibilities());
    }

    private void saveOrUpdateAdditionalInfo(Resume resume, UpdateResumeDto dto) {
        saveOrUpdateContactInfo(resume, dto.getContactTypeId(), dto.getContactValue());
        saveOrUpdateEducationInfo(resume, dto.getInstitution(), dto.getProgram(), dto.getStartDate(), dto.getEndDate(), dto.getDegree());
        saveOrUpdateWorkExperienceInfo(resume, dto.getYears(), dto.getCompanyName(), dto.getPosition(), dto.getResponsibilities());
    }

    private void saveOrUpdateContactInfo(Resume resume, Integer contactTypeId, String contactValue) {
        ContactInfo contactInfo = contactInfoRepository.findByResumeId(resume.getId()).stream().findFirst().orElse(new ContactInfo());
        ContactType contactType = contactTypeRepository.findById(contactTypeId).orElse(null);

        contactInfo.setResume(resume);
        contactInfo.setType(contactType);
        contactInfo.setContactValue(contactValue);

        contactInfoRepository.save(contactInfo);
    }

    private void saveOrUpdateEducationInfo(Resume resume, String institution, String program, String startDate, String endDate, String degree) {
        EducationInfo educationInfo = educationInfoRepository.findByResumeId(resume.getId()).stream().findFirst().orElse(new EducationInfo());

        educationInfo.setResume(resume);
        educationInfo.setInstitution(institution);
        educationInfo.setProgram(program);
        educationInfo.setStartDate(parseDate(startDate));
        educationInfo.setEndDate(parseDate(endDate));
        educationInfo.setDegree(degree);

        educationInfoRepository.save(educationInfo);
    }

    private void saveOrUpdateWorkExperienceInfo(Resume resume, Integer years, String companyName, String position, String responsibilities) {
        WorkExperienceInfo workExperienceInfo = workExperienceInfoRepository.findByResumeId(resume.getId()).stream().findFirst().orElse(new WorkExperienceInfo());

        workExperienceInfo.setResume(resume);
        workExperienceInfo.setYears(years);
        workExperienceInfo.setCompanyName(companyName);
        workExperienceInfo.setPosition(position);
        workExperienceInfo.setResponsibilities(responsibilities);

        workExperienceInfoRepository.save(workExperienceInfo);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value);
    }
}