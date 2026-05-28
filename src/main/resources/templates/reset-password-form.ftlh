<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<#assign errors = .data_model["org.springframework.validation.BindingResult.form"]!>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">
                    <h2 class="mb-3">
                        <@spring.message "resetPassword.title"/>
                    </h2>

                    <#if message??>
                        <div class="alert alert-success">
                            ${message}
                        </div>

                        <a href="/login" class="btn btn-dark">
                            <@spring.message "nav.login"/>
                        </a>
                    <#elseif error??>
                        <div class="alert alert-danger">${error}</div>

                        <a href="/forgot-password" class="btn btn-outline-dark">
                            <@spring.message "forgotPassword.title"/>
                        </a>
                    <#else>
                        <p class="text-muted">
                            <@spring.message "resetPassword.description"/>
                        </p>

                        <form action="/reset-password" method="post">
                            <#if _csrf??>
                                <input type="hidden"
                                       name="${(_csrf.parameterName)!'csrf'}"
                                       value="${(_csrf.token)!''}">
                            </#if>

                            <input type="hidden" name="token" value="${form.token!}">

                            <div class="mb-3">
                                <label for="password" class="form-label">
                                    <@spring.message "resetPassword.newPassword"/>
                                </label>

                                <input type="password"
                                       id="password"
                                       name="password"
                                       class="form-control"
                                       placeholder="<@spring.message 'resetPassword.placeholder'/>"
                                       required
                                       autofocus>

                                <#if errors?has_content && errors.hasFieldErrors("password")>
                                    <div class="text-danger small mt-1">
                                        ${errors.getFieldError("password").defaultMessage}
                                    </div>
                                </#if>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-dark">
                                    <@spring.message "resetPassword.submit"/>
                                </button>

                                <a href="/login" class="btn btn-outline-dark">
                                    <@spring.message "forgotPassword.back"/>
                                </a>
                            </div>
                        </form>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</@main.layout>