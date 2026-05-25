<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<#assign errors = .data_model["org.springframework.validation.BindingResult.resume"]!>
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
                          data-contact-block="<@spring.message 'resume.contactBlock'/>"
                          data-contact-type="<@spring.message 'resume.contactType'/>"
                          data-contact-value="<@spring.message 'resume.contact'/>"
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
                          data-remove-text="<@spring.message 'button.remove'/>"
                          data-save-error="<@spring.message 'resume.saveError'/>"
                          data-request-error="<@spring.message 'resume.requestError'/>">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message "resume.name"/></label>
                            <input type="text" name="name" class="form-control" value="${resume.name!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message "label.category"/></label>
                            <select name="categoryId" class="form-select">
                                <option value=""><@spring.message "category.select"/></option>
                                <#list categories as category>
                                    <option value="${category.id}"
                                            <#if resume.categoryId?? && resume.categoryId == category.id>selected</#if>>
                                        ${category.name}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message "vacancy.salary"/></label>
                            <input type="number" step="0.01" name="salary" class="form-control"
                                   value="<#if resume.salary??>${resume.salary?c}</#if>">
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3"><@spring.message "resume.contacts"/></h4>

                        <div id="contact-list">
                            <#if contactInfos?size gt 0>
                                <#list contactInfos as contact>
                                    <div class="contact-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0"><@spring.message "resume.contactBlock"/></h6>
                                            <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.contactType"/></label>
                                            <select name="contactInfos[${contact_index}].typeId" class="form-select">
                                                <option value=""><@spring.message "resume.selectContactType"/></option>
                                                <#list contactTypes as contactType>
                                                    <option value="${contactType.id}"
                                                            <#if contact.typeId?? && contact.typeId == contactType.id>selected</#if>>
                                                        ${contactType.type}
                                                    </option>
                                                </#list>
                                            </select>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.contact"/></label>
                                            <input type="text"
                                                   name="contactInfos[${contact_index}].contactValue"
                                                   class="form-control"
                                                   value="${contact.contactValue!''}">
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="contact-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0"><@spring.message "resume.contactBlock"/></h6>
                                        <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.contactType"/></label>
                                        <select name="contactInfos[0].typeId" class="form-select">
                                            <option value=""><@spring.message "resume.selectContactType"/></option>
                                            <#list contactTypes as contactType>
                                                <option value="${contactType.id}">
                                                    ${contactType.type}
                                                </option>
                                            </#list>
                                        </select>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.contact"/></label>
                                        <input type="text" name="contactInfos[0].contactValue" class="form-control">
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button" id="add-contact" class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addContact"/>
                        </button>

                        <hr class="my-4">

                        <h4 class="mb-3"><@spring.message "resume.education"/></h4>

                        <div id="education-list">
                            <#if educationInfos?size gt 0>
                                <#list educationInfos as education>
                                    <div class="education-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0"><@spring.message "resume.educationBlock"/></h6>
                                            <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.institution"/></label>
                                            <input type="text"
                                                   name="educationInfos[${education_index}].institution"
                                                   class="form-control"
                                                   value="${education.institution!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.program"/></label>
                                            <input type="text"
                                                   name="educationInfos[${education_index}].program"
                                                   class="form-control"
                                                   value="${education.program!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.startDate"/></label>
                                            <input type="date"
                                                   name="educationInfos[${education_index}].startDate"
                                                   class="form-control"
                                                   value="${education.startDate!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.endDate"/></label>
                                            <input type="date"
                                                   name="educationInfos[${education_index}].endDate"
                                                   class="form-control"
                                                   value="${education.endDate!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.degree"/></label>
                                            <input type="text"
                                                   name="educationInfos[${education_index}].degree"
                                                   class="form-control"
                                                   value="${education.degree!''}">
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="education-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0"><@spring.message "resume.educationBlock"/></h6>
                                        <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.institution"/></label>
                                        <input type="text" name="educationInfos[0].institution" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.program"/></label>
                                        <input type="text" name="educationInfos[0].program" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.startDate"/></label>
                                        <input type="date" name="educationInfos[0].startDate" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.endDate"/></label>
                                        <input type="date" name="educationInfos[0].endDate" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.degree"/></label>
                                        <input type="text" name="educationInfos[0].degree" class="form-control">
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button" id="add-education" class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addEducation"/>
                        </button>

                        <hr class="my-4">

                        <h4 class="mb-3"><@spring.message "resume.workExperience"/></h4>

                        <div id="work-list">
                            <#if workExperienceInfos?size gt 0>
                                <#list workExperienceInfos as work>
                                    <div class="work-block border rounded p-3 mb-3">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h6 class="mb-0"><@spring.message "resume.workBlock"/></h6>
                                            <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                                <@spring.message "button.remove"/>
                                            </button>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.years"/></label>
                                            <input type="number"
                                                   name="workExperienceInfos[${work_index}].years"
                                                   class="form-control"
                                                   value="<#if work.years??>${work.years?c}</#if>">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.company"/></label>
                                            <input type="text"
                                                   name="workExperienceInfos[${work_index}].companyName"
                                                   class="form-control"
                                                   value="${work.companyName!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.position"/></label>
                                            <input type="text"
                                                   name="workExperienceInfos[${work_index}].position"
                                                   class="form-control"
                                                   value="${work.position!''}">
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label"><@spring.message "resume.responsibilities"/></label>
                                            <textarea name="workExperienceInfos[${work_index}].responsibilities"
                                                      class="form-control"
                                                      rows="4">${work.responsibilities!''}</textarea>
                                        </div>
                                    </div>
                                </#list>
                            <#else>
                                <div class="work-block border rounded p-3 mb-3">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h6 class="mb-0"><@spring.message "resume.workBlock"/></h6>
                                        <button type="button" class="btn btn-outline-danger btn-sm remove-block">
                                            <@spring.message "button.remove"/>
                                        </button>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.years"/></label>
                                        <input type="number" name="workExperienceInfos[0].years" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.company"/></label>
                                        <input type="text" name="workExperienceInfos[0].companyName" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.position"/></label>
                                        <input type="text" name="workExperienceInfos[0].position" class="form-control">
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label"><@spring.message "resume.responsibilities"/></label>
                                        <textarea name="workExperienceInfos[0].responsibilities"
                                                  class="form-control"
                                                  rows="4"></textarea>
                                    </div>
                                </div>
                            </#if>
                        </div>

                        <button type="button" id="add-work" class="btn btn-outline-dark btn-sm mb-3">
                            + <@spring.message "resume.addWork"/>
                        </button>

                        <#if resumeId??>
                            <div class="mb-3">
                                <label class="form-label"><@spring.message "label.activity"/></label>
                                <select name="isActive" class="form-select">
                                    <option value="true" <#if resume.isActive?? && resume.isActive>selected</#if>>
                                        <@spring.message "activity.active"/>
                                    </option>
                                    <option value="false" <#if resume.isActive?? && !resume.isActive>selected</#if>>
                                        <@spring.message "activity.inactive"/>
                                    </option>
                                </select>
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

    <script src="/static/js/resume-form.js"></script>
</@main.layout>