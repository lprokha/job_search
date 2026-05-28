<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<#assign contactInfos = resume.contactInfos![]>
<#assign educationInfos = resume.educationInfos![]>
<#assign workExperienceInfos = resume.workExperienceInfos![]>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">

                    <h2 class="mb-4">
                        <#if resumeId??>
                            <@spring.message "resume.editTitle"/>
                        <#else>
                            <@spring.message "resume.createTitle"/>
                        </#if>
                    </h2>

                    <form id="resume-form"
                          action="<#if resumeId??>/resumes/edit/${resumeId}<#else>/resumes/create</#if>"
                          method="post"
                          data-contact-count="${contactInfos?size}"
                          data-education-count="${educationInfos?size}"
                          data-work-count="${workExperienceInfos?size}"
                          data-contact-block="<@spring.message 'resume.contactBlock'/>"
                          data-contact-type="<@spring.message 'resume.contactType'/>"
                          data-contact-value="<@spring.message 'resume.contact'/>"
                          data-select-contact-type="<@spring.message 'resume.selectContactType'/>"
                          data-education-block="<@spring.message 'resume.educationBlock'/>"
                          data-education-institution="<@spring.message 'resume.institution'/>"
                          data-education-program="<@spring.message 'resume.program'/>"
                          data-education-start-date="<@spring.message 'resume.startDate'/>"
                          data-education-end-date="<@spring.message 'resume.endDate'/>"
                          data-education-degree="<@spring.message 'resume.degree'/>"
                          data-work-block="<@spring.message 'resume.workBlock'/>"
                          data-work-years="<@spring.message 'resume.years'/>"
                          data-work-company="<@spring.message 'resume.company'/>"
                          data-work-position="<@spring.message 'resume.position'/>"
                          data-work-responsibilities="<@spring.message 'resume.responsibilities'/>"
                          data-remove-text="<@spring.message 'button.remove'/>">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.name"/>
                            </label>

                            <input type="text"
                                   name="name"
                                   class="form-control"
                                   value="${resume.name!''}">

                            <@spring.bind "resume.name"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "label.category"/>
                            </label>

                            <select name="categoryId" class="form-select">
                                <option value="">
                                    <@spring.message "category.select"/>
                                </option>

                                <#list categories as category>
                                    <option value="${category.id}"
                                            <#if resume.categoryId?? && resume.categoryId == category.id>selected</#if>>
                                        ${category.name}
                                    </option>
                                </#list>
                            </select>

                            <@spring.bind "resume.categoryId"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "vacancy.salary"/>
                            </label>

                            <input type="number"
                                   step="0.01"
                                   name="salary"
                                   class="form-control"
                                   value="<#if resume.salary??>${resume.salary?c}</#if>">

                            <@spring.bind "resume.salary"/>
                            <#if spring.status.error>
                                <div class="text-danger small mt-1">
                                    <#list spring.status.errorMessages as error>
                                        <div>${error}</div>
                                    </#list>
                                </div>
                            </#if>
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.contacts"/>
                        </h4>

                        <div id="contact-list">
                            <#if contactInfos?size gt 0>
                                <#list contactInfos as contact>
                                    <div class="contact-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0">
                                                <@spring.message "resume.contactBlock"/>
                                            </h6>

                                            <button type="button"
                                                    class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.contactType"/>
                                            </label>

                                            <select name="contactInfos[${contact_index}].typeId"
                                                    class="form-select contact-type-input">
                                                <option value="">
                                                    <@spring.message "resume.selectContactType"/>
                                                </option>

                                                <#list contactTypes as contactType>
                                                    <option value="${contactType.id}"
                                                            <#if contact.typeId?? && contact.typeId == contactType.id>selected</#if>>
                                                        ${contactType.type}
                                                    </option>
                                                </#list>
                                            </select>

                                            <@spring.bind "resume.contactInfos[${contact_index}].typeId"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.contact"/>
                                            </label>

                                            <input type="text"
                                                   name="contactInfos[${contact_index}].contactValue"
                                                   class="form-control contact-value-input"
                                                   value="${contact.contactValue!''}">

                                            <@spring.bind "resume.contactInfos[${contact_index}].contactValue"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="contact-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0">
                                            <@spring.message "resume.contactBlock"/>
                                        </h6>

                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.contactType"/>
                                        </label>

                                        <select name="contactInfos[0].typeId"
                                                class="form-select contact-type-input">
                                            <option value="">
                                                <@spring.message "resume.selectContactType"/>
                                            </option>

                                            <#list contactTypes as contactType>
                                                <option value="${contactType.id}">
                                                    ${contactType.type}
                                                </option>
                                            </#list>
                                        </select>

                                        <@spring.bind "resume.contactInfos[0].typeId"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.contact"/>
                                        </label>

                                        <input type="text"
                                               name="contactInfos[0].contactValue"
                                               class="form-control contact-value-input">

                                        <@spring.bind "resume.contactInfos[0].contactValue"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button"
                                id="add-contact"
                                class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addContact"/>
                        </button>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.education"/>
                        </h4>

                        <div id="education-list">
                            <#if educationInfos?size gt 0>
                                <#list educationInfos as education>
                                    <div class="education-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0">
                                                <@spring.message "resume.educationBlock"/>
                                            </h6>

                                            <button type="button"
                                                    class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.institution"/>
                                            </label>

                                            <input type="text"
                                                   name="educationInfos[${education_index}].institution"
                                                   class="form-control education-institution-input"
                                                   value="${education.institution!''}">

                                            <@spring.bind "resume.educationInfos[${education_index}].institution"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.program"/>
                                            </label>

                                            <input type="text"
                                                   name="educationInfos[${education_index}].program"
                                                   class="form-control education-program-input"
                                                   value="${education.program!''}">

                                            <@spring.bind "resume.educationInfos[${education_index}].program"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.startDate"/>
                                            </label>

                                            <input type="date"
                                                   name="educationInfos[${education_index}].startDate"
                                                   class="form-control education-start-date-input"
                                                   value="${education.startDate!''}">

                                            <@spring.bind "resume.educationInfos[${education_index}].startDate"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.endDate"/>
                                            </label>

                                            <input type="date"
                                                   name="educationInfos[${education_index}].endDate"
                                                   class="form-control education-end-date-input"
                                                   value="${education.endDate!''}">

                                            <@spring.bind "resume.educationInfos[${education_index}].endDate"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.degree"/>
                                            </label>

                                            <input type="text"
                                                   name="educationInfos[${education_index}].degree"
                                                   class="form-control education-degree-input"
                                                   value="${education.degree!''}">

                                            <@spring.bind "resume.educationInfos[${education_index}].degree"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="education-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0">
                                            <@spring.message "resume.educationBlock"/>
                                        </h6>

                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.institution"/>
                                        </label>

                                        <input type="text"
                                               name="educationInfos[0].institution"
                                               class="form-control education-institution-input">

                                        <@spring.bind "resume.educationInfos[0].institution"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.program"/>
                                        </label>

                                        <input type="text"
                                               name="educationInfos[0].program"
                                               class="form-control education-program-input">

                                        <@spring.bind "resume.educationInfos[0].program"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.startDate"/>
                                        </label>

                                        <input type="date"
                                               name="educationInfos[0].startDate"
                                               class="form-control education-start-date-input">

                                        <@spring.bind "resume.educationInfos[0].startDate"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.endDate"/>
                                        </label>

                                        <input type="date"
                                               name="educationInfos[0].endDate"
                                               class="form-control education-end-date-input">

                                        <@spring.bind "resume.educationInfos[0].endDate"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.degree"/>
                                        </label>

                                        <input type="text"
                                               name="educationInfos[0].degree"
                                               class="form-control education-degree-input">

                                        <@spring.bind "resume.educationInfos[0].degree"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button"
                                id="add-education"
                                class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addEducation"/>
                        </button>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.workExperience"/>
                        </h4>

                        <div id="work-list">
                            <#if workExperienceInfos?size gt 0>
                                <#list workExperienceInfos as work>
                                    <div class="work-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0">
                                                <@spring.message "resume.workBlock"/>
                                            </h6>

                                            <button type="button"
                                                    class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.years"/>
                                            </label>

                                            <input type="number"
                                                   name="workExperienceInfos[${work_index}].years"
                                                   class="form-control work-years-input"
                                                   value="<#if work.years??>${work.years?c}</#if>">

                                            <@spring.bind "resume.workExperienceInfos[${work_index}].years"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.company"/>
                                            </label>

                                            <input type="text"
                                                   name="workExperienceInfos[${work_index}].companyName"
                                                   class="form-control work-company-input"
                                                   value="${work.companyName!''}">

                                            <@spring.bind "resume.workExperienceInfos[${work_index}].companyName"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.position"/>
                                            </label>

                                            <input type="text"
                                                   name="workExperienceInfos[${work_index}].position"
                                                   class="form-control work-position-input"
                                                   value="${work.position!''}">

                                            <@spring.bind "resume.workExperienceInfos[${work_index}].position"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">
                                                <@spring.message "resume.responsibilities"/>
                                            </label>

                                            <textarea name="workExperienceInfos[${work_index}].responsibilities"
                                                      class="form-control work-responsibilities-input"
                                                      rows="4">${work.responsibilities!''}</textarea>

                                            <@spring.bind "resume.workExperienceInfos[${work_index}].responsibilities"/>
                                            <#if spring.status.error>
                                                <div class="text-danger small mt-1">
                                                    <#list spring.status.errorMessages as error>
                                                        <div>${error}</div>
                                                    </#list>
                                                </div>
                                            </#if>
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="work-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0">
                                            <@spring.message "resume.workBlock"/>
                                        </h6>

                                        <button type="button"
                                                class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.years"/>
                                        </label>

                                        <input type="number"
                                               name="workExperienceInfos[0].years"
                                               class="form-control work-years-input">

                                        <@spring.bind "resume.workExperienceInfos[0].years"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.company"/>
                                        </label>

                                        <input type="text"
                                               name="workExperienceInfos[0].companyName"
                                               class="form-control work-company-input">

                                        <@spring.bind "resume.workExperienceInfos[0].companyName"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.position"/>
                                        </label>

                                        <input type="text"
                                               name="workExperienceInfos[0].position"
                                               class="form-control work-position-input">

                                        <@spring.bind "resume.workExperienceInfos[0].position"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">
                                            <@spring.message "resume.responsibilities"/>
                                        </label>

                                        <textarea name="workExperienceInfos[0].responsibilities"
                                                  class="form-control work-responsibilities-input"
                                                  rows="4"></textarea>

                                        <@spring.bind "resume.workExperienceInfos[0].responsibilities"/>
                                        <#if spring.status.error>
                                            <div class="text-danger small mt-1">
                                                <#list spring.status.errorMessages as error>
                                                    <div>${error}</div>
                                                </#list>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button"
                                id="add-work"
                                class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addWork"/>
                        </button>

                        <#if resumeId??>
                            <div class="mb-3">
                                <label class="form-label">
                                    <@spring.message "label.activity"/>
                                </label>

                                <select name="isActive" class="form-select">
                                    <option value="true"
                                            <#if resume.isActive?? && resume.isActive>selected</#if>>
                                        <@spring.message "activity.active"/>
                                    </option>

                                    <option value="false"
                                            <#if resume.isActive?? && !resume.isActive>selected</#if>>
                                        <@spring.message "activity.inactive"/>
                                    </option>
                                </select>

                                <@spring.bind "resume.isActive"/>
                                <#if spring.status.error>
                                    <div class="text-danger small mt-1">
                                        <#list spring.status.errorMessages as error>
                                            <div>${error}</div>
                                        </#list>
                                    </div>
                                </#if>
                            </div>
                        </#if>

                        <button type="submit" class="btn btn-dark">
                            <#if resumeId??>
                                <@spring.message "button.save"/>
                            <#else>
                                <@spring.message "button.create"/>
                            </#if>
                        </button>

                        <a href="/resumes" class="btn btn-outline-secondary ms-2">
                            <@spring.message "button.back"/>
                        </a>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="/static/js/resume-form.js?v=1000"></script>
</@main.layout>