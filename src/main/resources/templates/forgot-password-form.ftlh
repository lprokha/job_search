<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-6">
            <div class="card shadow-sm border-0">
                <div class="card-body">

                    <h2 class="mb-3">
                        <@spring.message "forgotPassword.title"/>
                    </h2>

                    <p class="text-muted">
                        <@spring.message "forgotPassword.description"/>
                    </p>

                    <#if errorCode??>
                        <div class="alert alert-danger">
                            <@spring.message errorCode/>
                        </div>
                    </#if>

                    <#if messageCode??>
                        <div class="alert alert-success">
                            <@spring.message messageCode/>
                        </div>
                    </#if>

                    <#if resetLink??>
                        <div class="alert alert-light border">
                            <p class="mb-2">
                                <@spring.message "forgotPassword.generatedLink"/>
                            </p>

                            <a href="${resetLink}" class="link-dark fw-semibold text-decoration-underline">
                                ${resetLink}
                            </a>
                        </div>
                    </#if>

                    <form action="/forgot-password" method="post">

                        <#if _csrf??>
                            <input type="hidden"
                                   name="${(_csrf.parameterName)!'csrf'}"
                                   value="${(_csrf.token)!''}">
                        </#if>

                        <div class="mb-3">
                            <label for="email" class="form-label">
                                <@spring.message "label.email"/>
                            </label>

                            <input type="email"
                                   id="email"
                                   name="email"
                                   class="form-control"
                                   placeholder="<@spring.message 'placeholder.email'/>"
                                   required
                                   autofocus>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-dark">
                                <@spring.message "forgotPassword.send"/>
                            </button>

                            <a href="/login" class="btn btn-outline-dark">
                                <@spring.message "forgotPassword.back"/>
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@main.layout>