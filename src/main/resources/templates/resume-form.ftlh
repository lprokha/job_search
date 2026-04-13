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
                            <label class="form-label">Category ID</label>
                            <input type="number" name="categoryId" class="form-control" value="${resume.categoryId!''}">
                            <#if errors?has_content && errors.hasFieldErrors("categoryId")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("categoryId").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Зарплата</label>
                            <input type="number" step="0.01" name="salary" class="form-control" value="${resume.salary!''}">
                            <#if errors?has_content && errors.hasFieldErrors("salary")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("salary").defaultMessage}
                                </div>
                            </#if>
                        </div>

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