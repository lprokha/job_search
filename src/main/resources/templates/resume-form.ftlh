<#import "layouts/layout.ftlh" as main>
<#assign errors = .data_model["org.springframework.validation.BindingResult.resume"]!>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-4">
                        <#if resumeId??>
                            Редактирование резюме
                        <#else>
                            Создание резюме
                        </#if>
                    </h2>

                    <form action="<#if resumeId??>/resumes/edit/${resumeId}<#else>/resumes/create</#if>" method="post">
                        <#if _csrf??>
                            <input
                                    type="hidden"
                                    name="${(_csrf.parameterName)!'csrf'}"
                                    value="${(_csrf.token)!''}">
                        </#if>
                        <div class="mb-3">
                            <label class="form-label">Название резюме</label>
                            <input type="text" name="name" class="form-control" value="${resume.name!''}">
                            <#if errors?has_content && errors.hasFieldErrors("name")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("name").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Категория</label>
                            <select name="categoryId" class="form-select">
                                <option value="">Выберите категорию</option>
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
                            <label class="form-label">Зарплата</label>
                            <input type="number" step="0.01" name="salary" class="form-control"
                                   value="<#if resume.salary??>${resume.salary?c}</#if>">
                            <#if errors?has_content && errors.hasFieldErrors("salary")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("salary").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <hr class="my-4">
                        <h4 class="mb-3">Контактная информация</h4>

                        <div class="mb-3">
                            <label class="form-label">Тип контакта</label>
                            <select name="contactTypeId" class="form-select">
                                <option value="">Выберите тип контакта</option>
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
                            <label class="form-label">Контакт</label>
                            <input type="text" name="contactValue" class="form-control" value="${resume.contactValue!''}">
                            <#if errors?has_content && errors.hasFieldErrors("contactValue")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("contactValue").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <hr class="my-4">
                        <h4 class="mb-3">Образование</h4>

                        <div class="mb-3">
                            <label class="form-label">Учебное заведение</label>
                            <input type="text" name="institution" class="form-control" value="${resume.institution!''}">
                            <#if errors?has_content && errors.hasFieldErrors("institution")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("institution").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Программа</label>
                            <input type="text" name="program" class="form-control" value="${resume.program!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Дата начала</label>
                            <input type="date" name="startDate" class="form-control" value="${resume.startDate!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Дата окончания</label>
                            <input type="date" name="endDate" class="form-control" value="${resume.endDate!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Степень</label>
                            <input type="text" name="degree" class="form-control" value="${resume.degree!''}">
                        </div>

                        <hr class="my-4">
                        <h4 class="mb-3">Опыт работы</h4>

                        <div class="mb-3">
                            <label class="form-label">Стаж в годах</label>
                            <input type="number" name="years" class="form-control"
                                   value="<#if resume.years??>${resume.years?c}</#if>">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Компания</label>
                            <input type="text" name="companyName" class="form-control" value="${resume.companyName!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Должность</label>
                            <input type="text" name="position" class="form-control" value="${resume.position!''}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Обязанности</label>
                            <textarea name="responsibilities" class="form-control" rows="4">${resume.responsibilities!''}</textarea>
                        </div>

                        <#if resumeId??>
                            <div class="mb-3">
                                <label class="form-label">Активность</label>
                                <select name="isActive" class="form-select">
                                    <option value="true" <#if resume.isActive?? && resume.isActive>selected</#if>>Активно</option>
                                    <option value="false" <#if resume.isActive?? && !resume.isActive>selected</#if>>Неактивно</option>
                                </select>
                                <#if errors?has_content && errors.hasFieldErrors("isActive")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("isActive").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </#if>

                        <button type="submit" class="btn btn-dark">
                            <#if resumeId??>
                                Сохранить
                            <#else>
                                Создать
                            </#if>
                        </button>
                        <a href="/resumes" class="btn btn-outline-secondary ms-2">Назад</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>