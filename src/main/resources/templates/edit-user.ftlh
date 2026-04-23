<#import "layouts/layout.ftlh" as main>
<#assign errors = .data_model["org.springframework.validation.BindingResult.user"]!>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="mb-4">Редактирование профиля</h2>

                    <form action="/profile/edit" method="post">
                        <#if _csrf??>
                            <input
                                    type="hidden"
                                    name="${(_csrf.parameterName)!'csrf'}"
                                    value="${(_csrf.token)!''}">
                        </#if>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Имя</label>
                                <input type="text" name="name" class="form-control" value="${user.name!''}">
                                <#if errors?has_content && errors.hasFieldErrors("name")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("name").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Фамилия</label>
                                <input type="text" name="surname" class="form-control" value="${user.surname!''}">
                                <#if errors?has_content && errors.hasFieldErrors("surname")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("surname").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" name="email" class="form-control" value="${user.email!''}">
                                <#if errors?has_content && errors.hasFieldErrors("email")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("email").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label">Номер телефона</label>
                                <input type="text" name="phoneNumber" class="form-control" value="${user.phoneNumber!''}">
                                <#if errors?has_content && errors.hasFieldErrors("phoneNumber")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("phoneNumber").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Возраст</label>
                                <input type="number" name="age" class="form-control" value="${user.age!''}">
                                <#if errors?has_content && errors.hasFieldErrors("age")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("age").defaultMessage}
                                    </div>
                                </#if>
                            </div>
                        </div>

                        <button type="submit" class="btn btn-dark">Сохранить</button>
                        <a href="/profile" class="btn btn-outline-secondary ms-2">Назад</a>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>