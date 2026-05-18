<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<#assign errors = .data_model["org.springframework.validation.BindingResult.resume"]!>

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
                          data-education-institution="<@spring.message 'resume.institution'/>"
                          data-education-program="<@spring.message 'resume.program'/>"
                          data-education-start-date="<@spring.message 'resume.startDate'/>"
                          data-education-end-date="<@spring.message 'resume.endDate'/>"
                          data-education-degree="<@spring.message 'resume.degree'/>"
                          data-work-years="<@spring.message 'resume.years'/>"
                          data-work-company="<@spring.message 'resume.company'/>"
                          data-work-position="<@spring.message 'resume.position'/>"
                          data-work-responsibilities="<@spring.message 'resume.responsibilities'/>"
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

                        <div class="mb-3">
                            <label class="form-label"><@spring.message "resume.contactType"/></label>
                            <select name="contactTypeId" class="form-select">
                                <option value=""><@spring.message "resume.selectContactType"/></option>
                                <#list contactTypes as contactType>
                                    <option value="${contactType.id}"
                                            <#if resume.contactTypeId?? && resume.contactTypeId == contactType.id>selected</#if>>
                                        ${contactType.type}
                                    </option>
                                </#list>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label"><@spring.message "resume.contact"/></label>
                            <input type="text" name="contactValue" class="form-control" value="${resume.contactValue!''}">
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3"><@spring.message "resume.education"/></h4>

                        <#if resumeId??>
                            <div class="education-block border rounded p-3 mb-3">
                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.institution"/></label>
                                    <input type="text" name="institution" class="form-control" value="${resume.institution!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.program"/></label>
                                    <input type="text" name="program" class="form-control" value="${resume.program!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.startDate"/></label>
                                    <input type="date" name="startDate" class="form-control" value="${resume.startDate!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.endDate"/></label>
                                    <input type="date" name="endDate" class="form-control" value="${resume.endDate!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.degree"/></label>
                                    <input type="text" name="degree" class="form-control" value="${resume.degree!''}">
                                </div>
                            </div>
                        <#else>
                            <div id="education-list">
                                <div class="education-block border rounded p-3 mb-3">
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
                            </div>

                            <button type="button" id="add-education" class="btn btn-outline-dark btn-sm mb-3">
                                + <@spring.message "resume.addEducation"/>
                            </button>
                        </#if>

                        <hr class="my-4">

                        <h4 class="mb-3"><@spring.message "resume.workExperience"/></h4>

                        <#if resumeId??>
                            <div class="work-block border rounded p-3 mb-3">
                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.years"/></label>
                                    <input type="number" name="years" class="form-control"
                                           value="<#if resume.years??>${resume.years?c}</#if>">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.company"/></label>
                                    <input type="text" name="companyName" class="form-control" value="${resume.companyName!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.position"/></label>
                                    <input type="text" name="position" class="form-control" value="${resume.position!''}">
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><@spring.message "resume.responsibilities"/></label>
                                    <textarea name="responsibilities" class="form-control" rows="4">${resume.responsibilities!''}</textarea>
                                </div>
                            </div>
                        <#else>
                            <div id="work-list">
                                <div class="work-block border rounded p-3 mb-3">
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
                                        <textarea name="workExperienceInfos[0].responsibilities" class="form-control" rows="4"></textarea>
                                    </div>
                                </div>
                            </div>

                            <button type="button" id="add-work" class="btn btn-outline-dark btn-sm mb-3">
                                + <@spring.message "resume.addWork"/>
                            </button>
                        </#if>

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

    <#if !resumeId??>
        <script src="/static/js/resume-form.js"></script>
    </#if>
</@main.layout>