<#import "layouts/layout.ftlh" as main>
<#assign errors = .data_model["org.springframework.validation.BindingResult.vacancy"]!>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-4">
                        <#if vacancyId??>
                            Редактирование вакансии
                        <#else>
                            Создание вакансии
                        </#if>
                    </h2>

                    <form action="<#if vacancyId??>/my-vacancies/edit/${vacancyId}<#else>/my-vacancies/create</#if>" method="post">
                        <div class="mb-3">
                            <label class="form-label">Название вакансии</label>
                            <input type="text" name="name" class="form-control" value="${vacancy.name!''}">
                            <#if errors?has_content && errors.hasFieldErrors("name")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("name").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Описание</label>
                            <textarea name="description" class="form-control" rows="4">${vacancy.description!''}</textarea>
                            <#if errors?has_content && errors.hasFieldErrors("description")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("description").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Категория</label>
                            <select name="categoryId" class="form-select">
                                <option value="">Выберите категорию</option>
                                <#list categories as category>
                                    <option value="${category.id}"
                                            <#if vacancy.categoryId?? && vacancy.categoryId == category.id>selected</#if>>
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
                            <input type="number" step="0.01" name="salary" class="form-control" value="${vacancy.salary!''}">
                            <#if errors?has_content && errors.hasFieldErrors("salary")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("salary").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Опыт от</label>
                                <input type="number" name="expFrom" class="form-control" value="${vacancy.expFrom!''}">
                                <#if errors?has_content && errors.hasFieldErrors("expFrom")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("expFrom").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Опыт до</label>
                                <input type="number" name="expTo" class="form-control" value="${vacancy.expTo!''}">
                                <#if errors?has_content && errors.hasFieldErrors("expTo")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("expTo").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Активность</label>
                            <select name="isActive" class="form-select">
                                <option value="true" <#if vacancy.isActive?? && vacancy.isActive>selected</#if>>Активна</option>
                                <option value="false" <#if vacancy.isActive?? && !vacancy.isActive>selected</#if>>Неактивна</option>
                            </select>
                            <#if errors?has_content && errors.hasFieldErrors("isActive")>
                                <div class="text-danger small mt-1">
                                    ${errors.getFieldError("isActive").defaultMessage}
                                </div>
                            </#if>
                        </div>

                        <button type="submit" class="btn btn-dark">
                            <#if vacancyId??>
                                Сохранить
                            <#else>
                                Создать
                            </#if>
                        </button>
                        <a href="/my-vacancies" class="btn btn-outline-secondary ms-2">Назад</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>