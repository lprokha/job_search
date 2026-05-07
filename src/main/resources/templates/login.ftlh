<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <div class="card shadow-sm border-0">
                <div class="card-body p-4">
                    <h2 class="text-center mb-4">
                        <@spring.message "login.title"/>
                    </h2>

                    <#if loginError??>
                        <div class="alert alert-danger">
                            <@spring.message "login.error"/>
                        </div>
                    </#if>

                    <form action="/login" method="post">
                        <#if _csrf??>
                            <input
                                    type="hidden"
                                    name="${(_csrf.parameterName)!'csrf'}"
                                    value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label class="form-label">
                                <@spring.message "label.email"/>
                            </label>

                            <input type="email"
                                   name="username"
                                   class="form-control"
                                   required>
                        </div>

                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center">
                                <label class="form-label mb-0">
                                    <@spring.message "label.password"/>
                                </label>

                                <a href="/forgot-password"
                                   class="small text-muted text-decoration-none">
                                    <@spring.message "login.forgotPassword"/>
                                </a>
                            </div>

                            <input type="password"
                                   name="password"
                                   class="form-control mt-1"
                                   required>
                        </div>

                        <button type="submit" class="btn btn-dark w-100">
                            <@spring.message "button.login"/>
                        </button>
                    </form>

                    <div class="text-center mt-3">
                        <a href="/register"
                           class="text-decoration-none text-dark">
                            <@spring.message "login.createAccount"/>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>