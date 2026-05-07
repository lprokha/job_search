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

                    <form action="<#if resumeId??>/resumes/edit/${resumeId}<#else>/resumes/create</#if>"
                          method="post">

                        <#if _csrf??>
                            <input
                                    type="hidden"
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

                            <#if errors?has_content && errors.hasFieldErrors("name")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("name").defaultMessage}
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

                            <#if errors?has_content && errors.hasFieldErrors("categoryId")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("categoryId").defaultMessage}
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

                            <#if errors?has_content && errors.hasFieldErrors("salary")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("salary").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.contacts"/>
                        </h4>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.contactType"/>
                            </label>

                            <select name="contactTypeId" class="form-select">
                                <option value="">
                                    <@spring.message "resume.selectContactType"/>
                                </option>

                                <#list contactTypes as contactType>
                                    <option value="${contactType.id}"
                                            <#if resume.contactTypeId?? && resume.contactTypeId == contactType.id>selected</#if>>
                                        ${contactType.type}
                                    </option>
                                </#list>
                            </select>

                            <#if errors?has_content && errors.hasFieldErrors("contactTypeId")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("contactTypeId").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.contact"/>
                            </label>

                            <input type="text"
                                   name="contactValue"
                                   class="form-control"
                                   value="${resume.contactValue!''}">

                            <#if errors?has_content && errors.hasFieldErrors("contactValue")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("contactValue").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.education"/>
                        </h4>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.institution"/>
                            </label>

                            <input type="text"
                                   name="institution"
                                   class="form-control"
                                   value="${resume.institution!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.program"/>
                            </label>

                            <input type="text"
                                   name="program"
                                   class="form-control"
                                   value="${resume.program!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.startDate"/>
                            </label>

                            <input type="date"
                                   name="startDate"
                                   class="form-control"
                                   value="${resume.startDate!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.endDate"/>
                            </label>

                            <input type="date"
                                   name="endDate"
                                   class="form-control"
                                   value="${resume.endDate!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.degree"/>
                            </label>

                            <input type="text"
                                   name="degree"
                                   class="form-control"
                                   value="${resume.degree!''}">
                        </div>

                        <hr class="my-4">

                        <h4 class="mb-3">
                            <@spring.message "resume.workExperience"/>
                        </h4>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.years"/>
                            </label>

                            <input type="number"
                                   name="years"
                                   class="form-control"
                                   value="<#if resume.years??>${resume.years?c}</#if>">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.company"/>
                            </label>

                            <input type="text"
                                   name="companyName"
                                   class="form-control"
                                   value="${resume.companyName!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.position"/>
                            </label>

                            <input type="text"
                                   name="position"
                                   class="form-control"
                                   value="${resume.position!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "resume.responsibilities"/>
                            </label>

                            <textarea name="responsibilities"
                                      class="form-control"
                                      rows="4">${resume.responsibilities!''}</textarea>
                        </div>

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
                            </div>
                        </#if>

                        <button type="submit" class="btn btn-dark">
                            <#if resumeId??>
                                <@spring.message "button.save"/>
                            <#else>
                                <@spring.message "button.create"/>
                            </#if>
                        </button>

                        <a href="/resumes"
                           class="btn btn-outline-secondary ms-2">
                            <@spring.message "button.back"/>
                        </a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>